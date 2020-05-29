package cz.muni.fi.pv239.gtodolist.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.baoyz.swipemenulistview.SwipeMenu
import com.baoyz.swipemenulistview.SwipeMenuListView
import cz.muni.fi.pv239.gtodolist.R
import cz.muni.fi.pv239.gtodolist.api.CalendarExporter
import cz.muni.fi.pv239.gtodolist.api.ToDoViewModel
import cz.muni.fi.pv239.gtodolist.model.ToDo
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_todo_list.*
import kotlinx.android.synthetic.main.welcome_screen.view.*


/**
 * Fragment that contains list of TODO tasks.
 */
class ToDoListFragment : Fragment(), SwipeMenuListView.OnMenuItemClickListener, AdapterView.OnItemClickListener  {

    private val TAG = ToDoListFragment::class.java.simpleName
    private lateinit var todoViewModel: ToDoViewModel
    private var todosLoaded = false
    private lateinit var adapter: ToDoListAdapter
    private lateinit var sharedPreferences: SharedPreferences

    fun sortTodosByCategory(todos: List<ToDo>): List<ToDo>{
        var categoryNames = ArrayList<String>()
        for(t:ToDo in todos){
            if(!categoryNames.contains(t.category.name)){
                categoryNames.add(t.category.name)
            }
        }
        var result = ArrayList<ToDo>()
        for(c:String in categoryNames){
            for(t:ToDo in todos){
                if(t.category.name == c){
                    result.add(t)
                }
            }
        }
        return result
    }

    fun sortByDateAdded(todos: List<ToDo>): List<ToDo>{
        var result = ArrayList<ToDo>()
        todos.sortedWith(compareBy<ToDo>{it.dateAdded.split("/")[2].toInt()}.thenBy
                                     {it.dateAdded.split("/")[1].toInt()}.thenBy
                                     {it.dateAdded.split("/")[0].toInt()}).forEach({result.add(it)})

        return result.reversed()
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
        adapter = ToDoListAdapter(context!!)

        Log.d(TAG, "CREATED FRAGMENT VIEW")
        sharedPreferences = this.activity!!.getPreferences(Context.MODE_PRIVATE)

        view.rootView.sort_button.setOnClickListener{
            val builder =  AlertDialog.Builder(this.context!!)
            builder.setView(R.layout.pick_sort_type)

            val dialog = builder.create()
            dialog.show()
            var radioButtonId = R.id.sort_importance
            val sort = sharedPreferences.getString("sort-type", null)
            when(sort){
                "sort-importance" -> {radioButtonId = R.id.sort_importance}
                "sort-added" -> {radioButtonId = R.id.sort_date_added}
                "sort-category" -> {radioButtonId = R.id.sort_category}
            }
            dialog.findViewById<RadioButton>(radioButtonId)!!.isChecked = true

            val radioGroup = dialog.findViewById<RadioGroup>(R.id.sort_group)
            radioGroup!!.setOnCheckedChangeListener(
                RadioGroup.OnCheckedChangeListener{ group, checkedId ->
                    var sharedPreferencesSort = ""
                    when(checkedId){
                        R.id.sort_importance -> {
                            sharedPreferencesSort = "sort-importance"
                            adapter.setTodos(sortTodosByImportance(adapter.getTodos(), true))
                        }
                        R.id.sort_category -> {
                            sharedPreferencesSort = "sort-category"
                            adapter.setTodos(sortTodosByCategory(sortTodosByImportance(adapter.getTodos(), true)))
                        }
                        R.id.sort_date_added -> {
                            sharedPreferencesSort = "sort-added"
                            adapter.setTodos(sortByDateAdded(adapter.getTodos()))
                        }
                    }
                    sharedPreferences.edit().remove("sort-type").putString("sort-type", sharedPreferencesSort).apply()
                })
        }

        Log.d(TAG, "SHARED PREF VALUE = " + sharedPreferences.getString("sort-type", null))
        todoViewModel = ViewModelProvider(this).get(ToDoViewModel::class.java)

        todoViewModel.allNotDone.observe(viewLifecycleOwner, Observer {todos ->
            Log.d(TAG, "FILLING ADAPTER WITH TODOS")
            // update cached copy of words in adapter
            Log.d(TAG,"ALL TODOS = " + todos.toString())
            var validTodos = todoViewModel.getTodosOfCategory("all")
            Log.d(TAG, validTodos.toString())
            var sorted: List<ToDo> = sortTodosByImportance(validTodos, true)
            when(sharedPreferences.getString("sort-type", null)){
                "sort-importance" -> {sorted = sortTodosByImportance(validTodos, true)}
                "sort-category" -> {sorted = sortTodosByCategory(sortTodosByImportance(validTodos, true))}
                "sort-added" -> {sorted = sortByDateAdded(validTodos)}
            }
            Log.d(TAG, "SORTED TODOS = " + sorted.toString())
            adapter.setTodos(sorted)
            if(sorted.size == 0){
                view.rootView.empty_list_message.visibility = View.VISIBLE
                view.rootView.search_bar.visibility = View.GONE
                view.rootView.add_todo_fab.show()
                view.rootView.sort_button.visibility = View.GONE
            }else{
                view.rootView.empty_list_message.visibility = View.GONE
                view.rootView.search_bar.visibility = View.VISIBLE
                view.rootView.sort_button.visibility = View.VISIBLE
                view.rootView.add_todo_fab.show()
            }
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
                    var todos = todoViewModel.getTodosOfCategory("all")
                    var validTodos = todos
                    if(str!!.isEmpty()){
                        validTodos = todos
                    }else{
                        var tmp = ArrayList<ToDo>()
                        for (t in todos) {
                            if (t.name.contains(str as CharSequence, ignoreCase = true)) {
                                tmp.add(t)
                            }
                        }
                        validTodos = tmp.toList()
                    }
                    when(sharedPreferences.getString("sort-type", null)){
                        "sort-importance" -> {
                            adapter.setTodos(sortTodosByImportance(validTodos, true))
                        }
                        "sort-category" -> {
                            adapter.setTodos(sortTodosByCategory(sortTodosByImportance(validTodos, true)))
                        }
                        "sort-added" -> {
                            adapter.setTodos(sortByDateAdded(validTodos))
                        }
                    }
                }
                return false
            }
        })

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
            }
        }
        return false
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val bundle = bundleOf("id" to id)
        Log.d(TAG, "CLICKED ON TODO WITH ID $id")
        findNavController().navigate(R.id.action_to_todo_info, bundle)

    }
}
