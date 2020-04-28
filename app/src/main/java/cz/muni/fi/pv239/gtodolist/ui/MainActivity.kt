package cz.muni.fi.pv239.gtodolist.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import cz.muni.fi.pv239.gtodolist.R
import cz.muni.fi.pv239.gtodolist.api.ToDoViewModel
import cz.muni.fi.pv239.gtodolist.model.ToDo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_todo_list.*
import java.util.logging.Logger

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName
    private lateinit var todoViewModel: ToDoViewModel
    private val newToDoActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


//        add_todo_fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }

        // create view model so even when activity is destroyed model persists,
        // and when activity is recreated same model will be returned
        todoViewModel = ViewModelProvider(this).get(ToDoViewModel::class.java)


        add_todo_fab.setOnClickListener{
            Log.d(TAG, "ADD TODO FAB CLICKED")
            val intent = Intent(this, NewToDoActivity::class.java)
            startActivityForResult(intent, newToDoActivityRequestCode)
        }

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            //R.id.sort_category ->
            //R.id.sort_date_added ->
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newToDoActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.let {data ->
                val newName = data.getStringExtra("name")
                val newDescription = data.getStringExtra("description")
                val newCategory = data.getStringExtra("category")
                val newImportance = data.getIntExtra("importance", 0)
                todoViewModel.insert(ToDo(newName, newDescription, false, newCategory, newImportance.toLong()))
            }

        } else {
            Toast.makeText(
                applicationContext,
                "Name not filled, dropping todo.",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}