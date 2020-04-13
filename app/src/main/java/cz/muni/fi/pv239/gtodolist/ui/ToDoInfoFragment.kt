package cz.muni.fi.pv239.gtodolist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cz.muni.fi.pv239.gtodolist.R
import cz.muni.fi.pv239.gtodolist.api.ToDoService
import cz.muni.fi.pv239.gtodolist.model.ToDo
import kotlinx.android.synthetic.main.fragment_todo_info.*
import kotlinx.android.synthetic.main.fragment_todo_info.view.*
import java.lang.NullPointerException

/**
 * Fragment that contains information of specific TODO task.
 */
class ToDoInfoFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_todo_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getInt("id")!!
        val todo = ToDoService.get(id)

        title.text = todo.name
        description.text = todo.description

        view.findViewById<Button>(R.id.button_finish).setOnClickListener {
            ToDoService.markAsDone(todo.id)
            findNavController().navigate(R.id.action_to_todo_list)
        }

        view.findViewById<Button>(R.id.button_export).setOnClickListener {
            //todo
        }

        view.findViewById<Button>(R.id.button_back).setOnClickListener {
            findNavController().navigate(R.id.action_to_todo_list)
        }
    }
}
