package cz.muni.fi.pv239.gtodolist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cz.muni.fi.pv239.gtodolist.R
import cz.muni.fi.pv239.gtodolist.api.ToDoService
import kotlinx.android.synthetic.main.fragment_todo_list.*

/**
 * Fragment that contains list of TODO tasks.
 */
class ToDoListFragment : Fragment(), RecyclerViewClickListener {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_todo_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        todo_list.adapter = ToDoListAdapter(ToDoService().listAll(), this)
        todo_list.layoutManager = LinearLayoutManager(this.context)
    }

    override fun recyclerViewListClicked(v: View?, position: Int) {
        val bundle = bundleOf("position" to position.toString())
        findNavController().navigate(R.id.action_to_todo_info, bundle)
    }
}
