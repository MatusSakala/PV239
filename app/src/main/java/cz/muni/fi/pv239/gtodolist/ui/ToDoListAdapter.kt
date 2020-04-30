package cz.muni.fi.pv239.gtodolist.ui


import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cz.muni.fi.pv239.gtodolist.R
import cz.muni.fi.pv239.gtodolist.model.ToDo
import kotlinx.android.synthetic.main.todo_list_item.view.*
import kotlin.coroutines.coroutineContext


class ToDoListAdapter(val context: Context) : BaseAdapter(){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var todos = emptyList<ToDo>()
    private val TAG = ToDoListAdapter::class.java.simpleName

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
        if(todos[position].category == "PERSONAL"){
            viewHolder.item.setBackgroundResource(R.color.categoryPersonal)
        }else if(todos[position].category == "NONE"){
            viewHolder.item.setBackgroundResource(R.color.categoryNone)
        }else if(todos[position].category == "WORK"){
            viewHolder.item.setBackgroundResource(R.color.categoryWork)
        }else if(todos[position].category == "TRAVEL"){
            viewHolder.item.setBackgroundResource(R.color.categoryTravel)
        }else{
            viewHolder.item.setBackgroundResource(R.color.categoryOther)
        }
        return view
    }

    internal class ViewHolder(view: View) {
        var item: TextView = view.findViewById(R.id.todo_item) as TextView
        init {
            view.tag = this
        }
    }

    internal fun setTodos(todos: List<ToDo>){
        this.todos = todos
        Log.d(TAG, "DATA SET CHANGED IN ADAPTER")
        this.notifyDataSetChanged()
    }

}


