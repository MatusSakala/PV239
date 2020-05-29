package cz.muni.fi.pv239.gtodolist.ui

import android.content.Context
import android.util.TypedValue
import androidx.core.content.ContextCompat
import com.baoyz.swipemenulistview.SwipeMenu
import com.baoyz.swipemenulistview.SwipeMenuCreator
import com.baoyz.swipemenulistview.SwipeMenuItem
import cz.muni.fi.pv239.gtodolist.R

class ToDoSwipeMenu: SwipeMenuCreator{

    override fun create(menu: SwipeMenu?) {
        if(menu?.context != null){
            val calendarItem = SwipeMenuItem(menu.context)
            calendarItem.background = ContextCompat.getDrawable(menu.context, R.drawable.ripple_bg)
            calendarItem.width = calculateWidth(menu.context)
            calendarItem.setIcon(R.drawable.export_event)
            menu.addMenuItem(calendarItem)

            val doneItem = SwipeMenuItem(menu.context)
            doneItem.background = ContextCompat.getDrawable(menu.context, R.drawable.ripple_bg)
            doneItem.width = calculateWidth(menu.context)
            doneItem.setIcon(R.drawable.done)
            menu.addMenuItem(doneItem)
        }
    }

    private fun calculateWidth(context: Context): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            context.resources.getInteger(R.integer.swipe_menu_item_dp_width).toFloat(),
            context.resources.displayMetrics)
            .toInt()
    }
}