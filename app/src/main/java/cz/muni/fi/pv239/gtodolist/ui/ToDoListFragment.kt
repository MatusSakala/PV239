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

        todoViewModel.allNotDone.observe(viewLifecycleOwner, Observer {todos ->
            Log.d(TAG, "FILLING ADAPTER WITH TODOS")
            // update cached copy of words in adapter
            Log.d(TAG, todos.toString())
            todos?.let {adapter.setTodos(todoViewModel.getSortedByCategory())}
        })

        adapter.registerDataSetObserver(object : DataSetObserver() {
            override fun onChanged() {
                Log.d(TAG, "CHANGING LIST")
                listView.adapter = adapter
            }
        })

        listView.adapter = adapter
        listView.setMenuCreator(ToDoSwipeMenu())
        listView.setOnMenuItemClickListener(this)
        listView.setOnItemClickListener(this)
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT)
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
