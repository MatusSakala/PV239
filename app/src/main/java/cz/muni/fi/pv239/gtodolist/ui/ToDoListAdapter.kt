package cz.muni.fi.pv239.gtodolist.ui


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import cz.muni.fi.pv239.gtodolist.R
import cz.muni.fi.pv239.gtodolist.model.ToDo


class ToDoListAdapter(val todos: List<ToDo>) : BaseAdapter() {

    override fun getItem(position: Int): Any {
        return todos[position]
    }

    override fun getItemId(position: Int): Long {
        return todos[position].id
    }

    override fun getCount(): Int {
        return todos.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View?
        val viewHolder: ViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(parent!!.context).inflate(R.layout.todo_list_item, parent, false)
            viewHolder = ViewHolder(view!!)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.item.text = todos[position].name
        return view
    }

    internal class ViewHolder(view: View) {
        var item: TextView = view.findViewById(R.id.todo_item) as TextView

        init {
            view.tag = this
        }
    }
}


