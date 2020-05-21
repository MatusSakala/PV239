package cz.muni.fi.pv239.gtodolist.ui

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cz.muni.fi.pv239.gtodolist.R
import cz.muni.fi.pv239.gtodolist.api.CategoryViewModel
import cz.muni.fi.pv239.gtodolist.model.Category
import kotlinx.android.synthetic.main.create_category.*
import kotlinx.android.synthetic.main.create_todo.*
import java.sql.Time
import java.util.*


class NewToDoActivity : AppCompatActivity() {

    private val TAG = NewToDoActivity::class.java.simpleName

    // current version
    private lateinit var todoName: EditText
    private lateinit var todoDescription: EditText
    private var activeImportance = 0
    private lateinit var todoImportance: SeekBar
    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var addButton: Button

    private lateinit var adapter: CategorySpinnerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_todo)
        todoName = findViewById<EditText>(R.id.todo_name_content)
        todoDescription = findViewById<EditText>(R.id.todo_description_content)

        todoImportance = findViewById<SeekBar>(R.id.seekBar)

        todoImportance.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                activeImportance = i
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something
                //Toast.makeText(applicationContext,"start tracking",Toast.LENGTH_SHORT).show()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Do something
                //Toast.makeText(applicationContext,"stop tracking",Toast.LENGTH_SHORT).show()
            }
        })

        addButton = findViewById(R.id.add_todo_button)

        addButton.setOnClickListener{
            val replyIntent = Intent()
            if(TextUtils.isEmpty(todoName.text)){
                setResult(Activity.RESULT_CANCELED, replyIntent)
            }else{
                replyIntent.putExtra("name", todoName.text.toString())
                replyIntent.putExtra("description", todoDescription.text.toString())
                var category = category_spinner.selectedItem as Category
                replyIntent.putExtra("category", category.name)
                replyIntent.putExtra("importance", activeImportance)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }

        categoryViewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)
        categoryViewModel.userCategories.observe(this, Observer {categories ->
            Log.d(TAG, "Filling spinner with categories")
            Log.d(TAG, categories.toString())

            adapter.setCategories(categoryViewModel.getAllCategories())
        })

        adapter  = CategorySpinnerAdapter(applicationContext)
        category_spinner.adapter =  adapter
        add_category_button.setOnClickListener{
            val builder =  AlertDialog.Builder(this)
            builder.setView(R.layout.create_category)

            val dialog = builder.create()
            dialog.show()
            dialog.findViewById<Button>(R.id.cancel_category_create)!!.setOnClickListener{
                Log.d(TAG, "CLICKED CANCEL")
                dialog.dismiss()
            }
            dialog.findViewById<Button>(R.id.create_category)!!.setOnClickListener{
                Log.d(TAG, "CLICKED CREATE")
                var categoryName = dialog.findViewById<EditText>(R.id.category_name)!!.text
                if(categoryName.length == 0){
                    // make toast that category was not added
                    Toast.makeText(
                        applicationContext,
                        "Fill category name",
                        Toast.LENGTH_SHORT
                    ).show()
                }else{
                    // add category
                    var categoryName = dialog.findViewById<EditText>(R.id.category_name)!!.text.toString()
                    var rnd = Random()
                    var color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)).toString(16).replace('-', '#')
                    categoryViewModel.insert(Category(categoryName, color))
                    Toast.makeText(
                        applicationContext,
                        "Category added",
                        Toast.LENGTH_SHORT
                    ).show()
                    dialog.dismiss()
                }

            }
        }
    }


    companion object {
        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }
}