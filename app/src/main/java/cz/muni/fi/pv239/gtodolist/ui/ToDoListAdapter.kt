package cz.muni.fi.pv239.gtodolist.ui


import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cz.muni.fi.pv239.gtodolist.R
import cz.muni.fi.pv239.gtodolist.model.Category
import cz.muni.fi.pv239.gtodolist.model.ToDo
import kotlinx.android.synthetic.main.todo_list_item.view.*
import org.w3c.dom.Text
import kotlin.coroutines.coroutineContext


class ToDoListAdapter(val context: Context) : BaseAdapter(){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var todos = emptyList<ToDo>()
    private val TAG = ToDoListAdapter::class.java.simpleName

    val importanceImages = listOf(R.drawable.stars_one, R.drawable.stars_two, R.drawable.stars_three,
                    R.drawable.stars_four, R.drawable.stars_five, R.drawable.stars_six, R.drawable.stars_seven,
                    R.drawable.stars_eight, R.drawable.stars_nine, R.drawable.stars_ten)


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
        var view: View?
        var todo: ToDo = todos[position]
        var viewHolder: ViewHolder

        if(convertView == null){
            view = LayoutInflater.from(parent!!.context).inflate(R.layout.todo_list_item, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        }else{
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.name.text = todo.name
        // this is for textView with background
        //viewHolder.categoryDot.background.setTint(context.resources.getColor(R.color.categoryTravel))
        // this is for imageView
        //viewHolder.categoryDot.setColorFilter(color)
        viewHolder.dateAdded.text = todo.dateAdded
        viewHolder.categoryName.text = todo.category.name
        viewHolder.importance.setImageDrawable(context.resources.getDrawable(importanceImages[todo.importance.toInt() - 1]))
        viewHolder.importance.setColorFilter(Color.parseColor(todo.category.color))
        viewHolder.categoryDot.setColorFilter(Color.parseColor(todo.category.color))

        return view!!
    }

    internal class ViewHolder(view: View){
        var name = view.findViewById(R.id.todo_name) as TextView
        var dateAdded = view.findViewById(R.id.todo_date_added) as TextView
        var categoryDot = view.findViewById(R.id.todo_category_dot) as ImageView
        var categoryName = view.findViewById(R.id.todo_category_name) as TextView
        var importance = view.findViewById(R.id.todo_importance) as ImageView
        init{
            view.tag = this
        }
    }


    internal fun setTodos(todos: List<ToDo>){
        this.todos = todos
                    Log.d(TAG, "DATA SET CHANGED IN ADAPTER")
        this.notifyDataSetChanged()
    }

    fun getTodos(): List<ToDo>{
        return this.todos
    }

}


