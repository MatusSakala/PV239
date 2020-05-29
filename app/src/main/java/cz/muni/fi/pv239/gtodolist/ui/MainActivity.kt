package cz.muni.fi.pv239.gtodolist.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cz.muni.fi.pv239.gtodolist.R
import cz.muni.fi.pv239.gtodolist.api.CategoryViewModel
import cz.muni.fi.pv239.gtodolist.api.ToDoViewModel
import cz.muni.fi.pv239.gtodolist.model.ToDo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.welcome_screen.*
import java.util.*

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

        add_todo_fab.show()

        // on first start there is no entry in shared preferences make it default to sort by importance
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        val sort = sharedPreferences.getString("sort-type", null)
        if (sort == null) {
            sharedPreferences.edit().putString("sort-type", "sort-importance").apply()
        }

        add_todo_fab.setOnClickListener {
            Log.d(TAG, "ADD TODO FAB CLICKED")
            val intent = Intent(this, NewToDoActivity::class.java)
            startActivityForResult(intent, newToDoActivityRequestCode)
        }

        search_bar.setOnClickListener {
            search_bar.isIconified = false
        }

    }


    override fun onResume() {
        search_bar.visibility = View.VISIBLE
        add_todo_fab.show()
        sort_button.visibility = View.VISIBLE
        super.onResume()
    }

    override fun onStart() {
        search_bar.visibility = View.VISIBLE
        add_todo_fab.show()
        sort_button.visibility = View.VISIBLE
        super.onStart()
    }

    override fun onBackPressed() {
        search_bar.visibility = View.VISIBLE
        add_todo_fab.show()
        sort_button.visibility = View.VISIBLE
        super.onBackPressed()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        search_bar.visibility = View.VISIBLE
        add_todo_fab.show()

        if (requestCode == newToDoActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.let { data ->
                val newName = data.getStringExtra("name")
                val newDescription = data.getStringExtra("description")
                val categoryName = data.getStringExtra("category")
                val newImportance = data.getIntExtra("importance", 0)
                categoryViewModel.userCategories.observe(this, Observer { categories ->
                    val newCategory = categoryViewModel.findByName(categoryName)
                    if (newCategory.name != "") {
                        var calendar = Calendar.getInstance()
                        var day = calendar.get(Calendar.DAY_OF_MONTH)
                        var month =
                            calendar.get(Calendar.MONTH) + 1 // calendar returns months as 0-11 not 1-12
                        var year = calendar.get(Calendar.YEAR)
                        var date = day.toString() + "/" + month.toString() + "/" + year.toString()
                        todoViewModel.insert(
                            ToDo(
                                newName,
                                newDescription,
                                false,
                                newCategory,
                                newImportance.toLong(),
                                date
                            )
                        )
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