package cz.muni.fi.pv239.gtodolist.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cz.muni.fi.pv239.gtodolist.R
import cz.muni.fi.pv239.gtodolist.model.ToDo


class ToDoListAdapter(private val todos: List<ToDo>, private val listener: RecyclerViewClickListener): RecyclerView.Adapter<ToDoListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.todo_list_item, parent, false),
            listener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(todos[position])
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    class ViewHolder(itemView: View, private val listener: RecyclerViewClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var todoItem: TextView = itemView.findViewById(R.id.todo_item)

        fun bind(todo: ToDo) {
            todoItem.setOnClickListener(this)
            todoItem.text = todo.name
        }

        override fun onClick(v: View?) {
            listener.recyclerViewListClicked(v, this.adapterPosition)
        }
    }
}