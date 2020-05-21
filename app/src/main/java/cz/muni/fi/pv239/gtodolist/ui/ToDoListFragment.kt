package cz.muni.fi.pv239.gtodolist.ui

import android.content.Context
import android.database.DataSetObserver
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.baoyz.swipemenulistview.SwipeMenu
import com.baoyz.swipemenulistview.SwipeMenuListView
import cz.muni.fi.pv239.gtodolist.R
import cz.muni.fi.pv239.gtodolist.api.CalendarExporter
    import cz.muni.fi.pv239.gtodolist.api.ToDoService
import cz.muni.fi.pv239.gtodolist.api.ToDoViewModel
import cz.muni.fi.pv239.gtodolist.model.ToDo
import kotlinx.android.synthetic.main.fragment_todo_list.*
import kotlinx.android.synthetic.main.welcome_screen.*
import kotlinx.android.synthetic.main.welcome_screen.view.*


/**
 * Fragment that contains list of TODO tasks.
 */
class ToDoListFragment : Fragment(), SwipeMenuListView.OnMenuItemClickListener, AdapterView.OnItemClickListener  {

    private val TAG = ToDoListFragment::class.java.simpleName
    private lateinit var todoViewModel: ToDoViewModel
    private var todosLoaded = false

    fun getTodosOfImportance(todos:List<ToDo>,imp: Long): List<ToDo>{
        var result = ArrayList<ToDo>()
        for(t in todos){
            if(t.importance == imp){
                result.add(t)
            }
        }
        return result.toList()
    }

    fun sortTodosByImportance(todos: List<ToDo>, topToBottom: Boolean): List<ToDo>{
        var result = ArrayList<ToDo>()
        var startImportance = 10
        var endImportance = 1
        if(!topToBottom){
            startImportance = 1
            endImportance = 10
        }
        if(topToBottom){
            for(i in startImportance downTo endImportance){
                for(t in todos){
                    if(t.importance == i.toLong()){
                        result.add(t)
                    }
                }
            }
        }else{
            for(i in startImportance..endImportance){
                for(t in todos){
                    if(t.importance == i.toLong()){
                        result.add(t)
                    }
                }
            }
        }

        return result.toList()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_todo_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ToDoListAdapter(context!!)

        Log.d(TAG, "CREATED FRAGMENT VIEW")
        todoViewModel = ViewModelProvider(this).get(ToDoViewModel::class.java)

        var currentCategory = activity!!.intent.getStringExtra("category")
        if(currentCategory == null){
            currentCategory = "all"
        }

        Log.d(TAG, "GOT CATEGORY NAME FROM INTENT $currentCategory")

        todoViewModel.allNotDone.observe(viewLifecycleOwner, Observer {todos ->
            Log.d(TAG, "FILLING ADAPTER WITH TODOS")
            // update cached copy of words in adapter
            Log.d(TAG,"ALL TODOS = " + todos.toString())
            var validTodos = todoViewModel.getTodosOfCategory(currentCategory)
            Log.d(TAG, validTodos.toString())
            var sortedByImp = sortTodosByImportance(validTodos, true)
            Log.d(TAG, "SORTED TODOS = " + sortedByImp.toString())
            adapter.setTodos(sortedByImp)
            todosLoaded = true
        })

        listView.adapter = adapter
        listView.setMenuCreator(ToDoSwipeMenu())
        listView.setOnMenuItemClickListener(this)
        listView.onItemClickListener = this
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT)

        view.rootView.search_bar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(str: String?): Boolean {
                Log.d(TAG, str)
                return false
            }

            override fun onQueryTextChange(str: String?): Boolean {
                if(todosLoaded){
                    var todos = todoViewModel.getTodosOfCategory(currentCategory)
                    var validTodos = ArrayList<ToDo>()
                    for (t in todos){
                        if(t.name.contains(str as CharSequence, ignoreCase = true)){
                            validTodos.add(t)
                        }
                    }
                    adapter.setTodos(validTodos)
                }
                return false
            }
        })

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onMenuItemClick(position: Int, menu: SwipeMenu?, index: Int): Boolean {
        val todo: ToDo = listView.adapter.getItem(position) as ToDo

        Log.d(TAG, "PICKED "+ index.toString() +" ON TODO WITH ID " + todo.id.toString())
        when (index) {
            0 -> {
                startActivity(CalendarExporter.createIntent(todo))
            }
            1 -> {
                todo.done = true
                todoViewModel.update(todo)
                //ToDoService.markAsDone(todo.id)
                //listView.adapter = ToDoListAdapter(ToDoService.listAllNotDone())
            }
        }
        return false
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val bundle = bundleOf("id" to id)
        Log.d(TAG, "CLICKED ON TODO WITH ID $id")
        Log.d(TAG, bundle.toString())
        findNavController().navigate(R.id.action_to_todo_info, bundle)

    }
}
