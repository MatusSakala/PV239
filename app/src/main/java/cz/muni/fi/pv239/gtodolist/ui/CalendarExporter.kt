package cz.muni.fi.pv239.gtodolist.ui

import android.content.Intent
import cz.muni.fi.pv239.gtodolist.model.ToDo

object CalendarExporter {

    fun createIntent(todo: ToDo): Intent {
        val intent = Intent(Intent.ACTION_EDIT)
        intent.type = "vnd.android.cursor.item/event"

        intent.putExtra("title", todo.name)
        intent.putExtra("description", todo.description)

        return intent
    }
}