package cz.muni.fi.pv239.gtodolist.ui

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import cz.muni.fi.pv239.gtodolist.R
import cz.muni.fi.pv239.gtodolist.api.CalendarExporter
import cz.muni.fi.pv239.gtodolist.api.ToDoService
import cz.muni.fi.pv239.gtodolist.api.ToDoViewModel
import cz.muni.fi.pv239.gtodolist.model.Category
import kotlinx.android.synthetic.main.create_todo.*
import kotlinx.android.synthetic.main.fragment_todo_info.*
import java.util.*

/**
 * Fragment that contains information of specific TODO task.
 */
class ToDoInfoFragment : Fragment() {

    private val TAG = ToDoInfoFragment::class.java.simpleName
    private lateinit var todoViewModel: ToDoViewModel
    private lateinit var activeCategory: String

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.update_todo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "CREATIGN VIEW MODEL")
        todoViewModel = ViewModelProvider(this).get(ToDoViewModel::class.java)

        todoViewModel.allTodos.observe(viewLifecycleOwner, Observer {todos ->
            Log.d(TAG, "GOT ALL TODOS")
            // update cached copy of words in adapter
            Log.d(TAG, todos.toString())
            val id = arguments?.getLong("id")!!
            val todo = todoViewModel.getTodoWithId(id)
            activeCategory = todo.category

            Log.d(TAG, "FOUND TODO $todo")

            todo_name_content.setText(todo.name)
            todo_description_content.setText(todo.description)
            when(todo.category){
                Category.NONE.toString() -> category_none.isChecked = true
                Category.OTHER.toString() -> category_other.isChecked = true
                Category.TRAVEL.toString() -> category_travel.isChecked = true
                Category.WORK.toString() -> category_work.isChecked = true
                Category.PERSONAL.toString() -> category_personal.isChecked = true
            }

            seekBar.progress = todo.importance.toInt()

            // sorry for this piece of code, i didnt find other way to do it
            category_none.setOnClickListener{
                if(category_none.isChecked){
                    activeCategory = Category.NONE.toString()
                    category_other.isChecked = false
                    category_personal.isChecked = false
                    category_work.isChecked = false
                    category_travel.isChecked = false
                }else{
                    category_none.isChecked = true
                    activeCategory = Category.NONE.toString()
                }
            }

            category_personal.setOnClickListener{
                if(category_personal.isChecked){
                    activeCategory = Category.PERSONAL.toString()
                    category_none.isChecked = false
                    category_other.isChecked = false
                    category_travel.isChecked = false
                    category_work.isChecked = false
                }else{
                    category_none.isChecked = true
                }
            }

            category_work.setOnClickListener{
                if(category_work.isChecked){
                    activeCategory = Category.WORK.toString()
                    category_none.isChecked = false
                    category_other.isChecked = false
                    category_travel.isChecked = false
                    category_personal.isChecked = false
                }else{
                    category_none.isChecked = true
                }
            }

            category_travel.setOnClickListener{
                if(category_travel.isChecked){
                    activeCategory = Category.TRAVEL.toString()
                    category_none.isChecked = false
                    category_other.isChecked = false
                    category_personal.isChecked = false
                    category_work.isChecked = false
                }else{
                    category_none.isChecked = true
                }
            }

            category_other.setOnClickListener{
                if(category_other.isChecked){
                    activeCategory = Category.OTHER.toString()
                    category_none.isChecked = false
                    category_personal.isChecked = false
                    category_travel.isChecked = false
                    category_work.isChecked = false
                }else{
                    category_none.isChecked = true
                }
            }

            button_done.setOnClickListener {
                todo.done = true
                todoViewModel.update(todo)
                findNavController().navigate(R.id.action_to_todo_list)
            }

            button_export.setOnClickListener {
                startActivity(CalendarExporter.createIntent(todo))
            }

            button_back.setOnClickListener {
                findNavController().navigate(R.id.action_to_todo_list)
            }

            add_todo_button.setOnClickListener{
                todo.name = todo_name_content.text.toString()
                todo.description = todo_description_content.text.toString()
                todo.category = activeCategory
                todo.importance = seekBar.progress.toLong()
                todoViewModel.update(todo)
                findNavController().navigate(R.id.action_to_todo_list)
            }
        })
    }
}
