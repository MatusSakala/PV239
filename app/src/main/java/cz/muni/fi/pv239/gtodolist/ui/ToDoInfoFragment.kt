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

        // TODO !!! takto nacitas todo objekt
        val todo = arguments?.getString("position")?.toInt()?.let { ToDoService().listAll()[it] }




        view.findViewById<Button>(R.id.button_second).setOnClickListener {
            // TODO !!! akcia na vratenie spat na todo list - zmen ako potrebujes
            findNavController().navigate(R.id.action_to_todo_list)
        }
    }
}
