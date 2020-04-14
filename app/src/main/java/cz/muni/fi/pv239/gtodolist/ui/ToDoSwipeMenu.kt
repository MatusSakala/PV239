package cz.muni.fi.pv239.gtodolist.ui

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.TypedValue
import androidx.core.content.ContextCompat
import com.baoyz.swipemenulistview.SwipeMenu
import com.baoyz.swipemenulistview.SwipeMenuCreator
import com.baoyz.swipemenulistview.SwipeMenuItem
import cz.muni.fi.pv239.gtodolist.R

class ToDoSwipeMenu: SwipeMenuCreator{

    override fun create(menu: SwipeMenu?) {
        val calendarItem = SwipeMenuItem(menu?.context!!)
        calendarItem.background = ColorDrawable(ContextCompat.getColor(menu.context, R.color.exportEventIconBackground))
        calendarItem.width = calculateWidth(menu.context)
        calendarItem.setIcon(R.drawable.export_event)
        menu.addMenuItem(calendarItem)

        val doneItem = SwipeMenuItem(menu.context)
        doneItem.background = ColorDrawable(ContextCompat.getColor(menu.context, R.color.doneIconBackground))
        doneItem.width = calculateWidth(menu.context)
        doneItem.setIcon(R.drawable.done)
        menu.addMenuItem(doneItem)
    }

    private fun calculateWidth(context: Context): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            context.resources.getInteger(R.integer.swipe_menu_item_dp_width).toFloat(),
            context.resources.displayMetrics)
            .toInt()
    }
}