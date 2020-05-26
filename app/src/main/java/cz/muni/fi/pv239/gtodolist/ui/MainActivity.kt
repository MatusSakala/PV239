package cz.muni.fi.pv239.gtodolist.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.Menu
import android.view.MenuItem
import android.view.View
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
import cz.muni.fi.pv239.gtodolist.api.CategoryViewModel
import cz.muni.fi.pv239.gtodolist.api.ToDoViewModel
import cz.muni.fi.pv239.gtodolist.model.ToDo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_todo_list.*
import kotlinx.android.synthetic.main.welcome_screen.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.logging.Logger

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName
    private lateinit var todoViewModel: ToDoViewModel
    private lateinit var categoryViewModel: CategoryViewModel
    private val newToDoActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        todoViewModel = ViewModelProvider(this).get(ToDoViewModel::class.java)
        categoryViewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)

//        search_bar.visibility = View.GONE
          add_todo_fab.show()

//        personal_todos_button.background.setTint(resources.getColor(R.color.categoryPersonal))
//        work_todos_button.background.setTint(resources.getColor(R.color.categoryWork))
//        travel_todos_button.background.setTint(resources.getColor(R.color.categoryTravel))
//        none_todos_button.background.setTint(resources.getColor(R.color.categoryNone))
//        other_todos_button.background.setTint(resources.getColor(R.color.categoryOther))
//        all_todos_button.background.setTint(resources.getColor(R.color.categoryAll))
//        personal_todos_button.setOnClickListener{
//            val intent = Intent(this, DisplayCategoryActivity::class.java)
//            intent.putExtra("category", "PERSONAL")
//            startActivity(intent)
//        }
//
//        work_todos_button.setOnClickListener{
//            val intent = Intent(this, DisplayCategoryActivity::class.java)
//            intent.putExtra("category", "WORK")
//            startActivity(intent)
//        }
//
//        travel_todos_button.setOnClickListener{
//            val intent = Intent(this, DisplayCategoryActivity::class.java)
//            intent.putExtra("category", "TRAVEL")
//            startActivity(intent)
//        }
//
//        none_todos_button.setOnClickListener{
//            val intent = Intent(this, DisplayCategoryActivity::class.java)
//            intent.putExtra("category", "None")
//            startActivity(intent)
//        }
//
//        other_todos_button.setOnClickListener{
//            val intent = Intent(this, DisplayCategoryActivity::class.java)
//            intent.putExtra("category", "OTHER")
//            startActivity(intent)
//        }
//
//        all_todos_button.setOnClickListener{
//            val intent = Intent(this, DisplayCategoryActivity::class.java)
//            intent.putExtra("category", "ALL")
//            startActivity(intent)
//        }

//        val adapter = ToDoListAdapter(this)
//        todoViewModel.allNotDone.observe(this, Observer {todos ->
//            var sortedByImp = sortTodosByImportance(todos, true)
//            Log.d(TAG, "SORTED TODOS = " + sortedByImp.toString())
//            adapter.setTodos(sortedByImp)
//        })


        add_todo_fab.setOnClickListener{
            Log.d(TAG, "ADD TODO FAB CLICKED")
            val intent = Intent(this, NewToDoActivity::class.java)
            startActivityForResult(intent, newToDoActivityRequestCode)
        }

        search_bar.setOnClickListener{
            search_bar.isIconified = false
        }

        categoryViewModel.userCategories.observe(this, Observer {categories ->

        })

    }


    override fun onResume() {
        search_bar.visibility = View.VISIBLE
        add_todo_fab.show()
        super.onResume()
    }

    override fun onStart() {
        search_bar.visibility = View.VISIBLE
        add_todo_fab.show()
        super.onStart()
    }

    override fun onBackPressed() {
        search_bar.visibility = View.VISIBLE
        add_todo_fab.show()
        super.onBackPressed()
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

        search_bar.visibility = View.VISIBLE
        add_todo_fab.show()

        if (requestCode == newToDoActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.let {data ->
                val newName = data.getStringExtra("name")
                val newDescription = data.getStringExtra("description")
                val categoryName = data.getStringExtra("category")
                val newImportance = data.getIntExtra("importance", 0)
                categoryViewModel.userCategories.observe(this, Observer {categories ->
                    val newCategory = categoryViewModel.findByName(categoryName)
                    if(newCategory.name != ""){
                        var calendar = Calendar.getInstance()
                        var day = calendar.get(Calendar.DAY_OF_MONTH)
                        var month = calendar.get(Calendar.MONTH)
                        var year = calendar.get(Calendar.YEAR)
                        var date = day.toString() + "/" + month.toString() + "/" + year.toString()
                        todoViewModel.insert(ToDo(newName, newDescription, false, newCategory, newImportance.toLong(), date))
                        categoryViewModel.userCategories.removeObservers(this)
                    }
                })

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