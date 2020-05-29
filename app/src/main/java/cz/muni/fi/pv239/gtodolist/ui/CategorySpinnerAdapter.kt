package cz.muni.fi.pv239.gtodolist.ui

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import cz.muni.fi.pv239.gtodolist.R
import cz.muni.fi.pv239.gtodolist.model.Category

class CategorySpinnerAdapter(val context: Context) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var categories = emptyList<Category>()
    private val TAG = CategorySpinnerAdapter::class.java.simpleName

    override fun getView(pos: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View?
        val category = categories[pos]
        var viewHolder: ViewHolder

        if (convertView == null) {
            view =
                LayoutInflater.from(parent!!.context).inflate(R.layout.category_item, parent, false)
            viewHolder = ViewHolder(view!!)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.categoryName.text = category.name
        viewHolder.categoryName.setTextColor(Color.parseColor(category.color))
        viewHolder.categoryIcon.setColorFilter(Color.parseColor(category.color))

        return view
    }

    internal class ViewHolder(view: View) {
        var categoryName = view.findViewById(R.id.spinner_category_name) as TextView
        var categoryIcon = view.findViewById(R.id.spinner_category_icon) as ImageView

        init {
            view.tag = this
        }
    }

    override fun getItem(pos: Int): Any {
        return categories[pos]
    }

    override fun getItemId(pos: Int): Long {
        return categories[pos].id
    }

    override fun getCount(): Int {
        return categories.size
    }

    internal fun setCategories(categories: List<Category>) {
        this.categories = categories
        Log.d(TAG, "DATA SET CHANGED IN SPINNER ADAPTER")
        this.notifyDataSetChanged()
    }
}