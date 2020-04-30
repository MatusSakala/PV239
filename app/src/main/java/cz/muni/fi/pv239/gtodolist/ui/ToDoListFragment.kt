package cz.muni.fi.pv239.gtodolist.ui

import android.database.DataSetObserver
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
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


/**
 * Fragment that contains list of TODO tasks.
 */
class ToDoListFragment : Fragment(), SwipeMenuListView.OnMenuItemClickListener, AdapterView.OnItemClickListener  {

    private val TAG = ToDoListFragment::class.java.simpleName
    private lateinit var todoViewModel: ToDoViewModel

    fun getTodosOfImportance(todos:List<ToDo>,imp: Long): List<ToDo>{
        var result = ArrayList<ToDo>()
        for(t in todos){
            if(t.importance == imp){
                result.add(t)
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
        val adapters = ArrayList<ToDoListAdapter>()
        val listViews = listOf(listView10, listView9, listView8, listView7,
            listView6, listView5, listView4, listView3, listView2, listView1)
        val importanceViews = listOf(stars_ten, stars_nine, stars_eight, stars_seven, stars_six,
            stars_five, stars_four, stars_three, stars_two, stars_one)

        for(i in 0..9){
            adapters.add(ToDoListAdapter(context!!))
        }

        Log.d(TAG, "CREATED FRAGMENT VIEW")
        todoViewModel = ViewModelProvider(this).get(ToDoViewModel::class.java)

        var currentCategory = activity!!.intent.getStringExtra("category")
        Log.d(TAG, "GOT CATEGORY NAME FROM INTENT $currentCategory")

        todoViewModel.allNotDone.observe(viewLifecycleOwner, Observer {todos ->
            Log.d(TAG, "FILLING ADAPTER WITH TODOS")
            // update cached copy of words in adapter
            var validTodos = todoViewModel.getTodosOfCategory(currentCategory)
            Log.d(TAG, validTodos.toString())
            for(i in 0..9){
                adapters[i].setTodos(getTodosOfImportance(validTodos, (10-i).toLong()))
            }
            //todos?.let {adapter.setTodos(validTodos)}
        })

        for(i in 0..9){
            adapters[i].registerDataSetObserver(object : DataSetObserver(){
                override fun onChanged() {
                    listViews[i].adapter = adapters[i]
                }
            })
        }
        var text = "PERSONAL"
        var color = resources.getColor(R.color.categoryPersonal)
        var drawable=resources.getDrawable(R.drawable.category_personal)

        when(currentCategory){
            "WORK" -> {
                text = "WORK"
                color = resources.getColor(R.color.categoryWork)
                drawable = resources.getDrawable(R.drawable.category_work)
            }
            "NONE" -> {
                text = "NONE"
                color = resources.getColor(R.color.categoryNone)
                drawable = resources.getDrawable(R.drawable.category_none)
            }
            "OTHER" -> {
                text = "OTHER"
                color = resources.getColor(R.color.categoryOther)
                drawable = resources.getDrawable(R.drawable.category_other)
            }
            "TRAVEL" -> {
                text = "TRAVEL"
                color = resources.getColor(R.color.categoryTravel)
                drawable = resources.getDrawable(R.drawable.category_travel)
            }
            "ALL" -> {
                text = "ALL"
                color = resources.getColor(R.color.categoryAll)
                drawable = resources.getDrawable(R.drawable.category_all)
            }
        }

        for(v in importanceViews){
            v.background.setTint(color)
        }

        list_category_headline.background.setTint(color)
        list_category_headline.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        list_category_headline.text = text
        list_category_headline.setTextColor(color)

        for(i in 0..9){
            listViews[i].adapter = adapters[i]
            listViews[i].setMenuCreator(ToDoSwipeMenu())
            listViews[i].setOnMenuItemClickListener{ pos: Int, swipeMenu: SwipeMenu, i1: Int ->
                val todo: ToDo = listViews[i].adapter.getItem(pos) as ToDo
                when(i1){
                    0 ->{
                        startActivity(CalendarExporter.createIntent(todo))
                    }
                    1 ->{
                        todo.done = true
                        todoViewModel.update(todo)
                    }
                }
                false

            }
            listViews[i].setOnItemClickListener(this)
            listViews[i].setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT)
        }
    }

    override fun onMenuItemClick(position: Int, menu: SwipeMenu?, index: Int): Boolean {
//        val todo: ToDo = listView1.adapter.getItem(position) as ToDo

//        Log.d(TAG, "PICKED "+ index.toString() +" ON TODO WITH ID " + todo.id.toString())
//        when (index) {
//            0 -> {
//                startActivity(CalendarExporter.createIntent(todo))
//            }
//            1 -> {
//                todo.done = true
//                todoViewModel.update(todo)
//                //ToDoService.markAsDone(todo.id)
//                //listView.adapter = ToDoListAdapter(ToDoService.listAllNotDone())
//            }
//        }
        return false
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val bundle = bundleOf("id" to id)
        Log.d(TAG, "CLICKED ON TODO WITH ID $id")
        Log.d(TAG, bundle.toString())
        findNavController().navigate(R.id.action_to_todo_info, bundle)

    }
}
