package cz.muni.fi.pv239.gtodolist.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import cz.muni.fi.pv239.gtodolist.R
import cz.muni.fi.pv239.gtodolist.model.Category


class NewToDoActivity : AppCompatActivity() {

    private val TAG = NewToDoActivity::class.java.simpleName

    // current version
    private lateinit var todoName: EditText
    private lateinit var todoDescription: EditText
    // for later update
    private lateinit var todoCategoryNone: CheckBox
    private lateinit var todoCategoryPersonal: CheckBox
    private lateinit var todoCategoryWork: CheckBox
    private lateinit var todoCategoryTravel: CheckBox
    private lateinit var todoCategoryOther: CheckBox
    private var activeCategory =  Category.NONE
    private var activeImportance = 0
    private lateinit var todoImportance: SeekBar

    private lateinit var addButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_todo)
        todoName = findViewById<EditText>(R.id.todo_name_content)
        todoDescription = findViewById<EditText>(R.id.todo_description_content)
        todoCategoryNone = findViewById<CheckBox>(R.id.category_none)
        todoCategoryPersonal = findViewById<CheckBox>(R.id.category_personal)
        todoCategoryWork = findViewById<CheckBox>(R.id.category_work)
        todoCategoryTravel = findViewById<CheckBox>(R.id.category_travel)
        todoCategoryOther = findViewById<CheckBox>(R.id.category_other)

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

        // sorry for this piece of code, i didnt find other way to do it
        todoCategoryNone.setOnClickListener{
            if(todoCategoryNone.isChecked){
                activeCategory = Category.NONE
                todoCategoryOther.isChecked = false
                todoCategoryPersonal.isChecked = false
                todoCategoryWork.isChecked = false
                todoCategoryTravel.isChecked = false
            }else{
                todoCategoryNone.isChecked = true
                activeCategory = Category.NONE
            }
        }

        todoCategoryPersonal.setOnClickListener{
            if(todoCategoryPersonal.isChecked){
                activeCategory = Category.PERSONAL
                todoCategoryNone.isChecked = false
                todoCategoryOther.isChecked = false
                todoCategoryTravel.isChecked = false
                todoCategoryWork.isChecked = false
            }else{
                todoCategoryNone.isChecked = true
            }
        }

        todoCategoryWork.setOnClickListener{
            if(todoCategoryWork.isChecked){
                activeCategory = Category.WORK
                todoCategoryNone.isChecked = false
                todoCategoryOther.isChecked = false
                todoCategoryTravel.isChecked = false
                todoCategoryPersonal.isChecked = false
            }else{
                todoCategoryNone.isChecked = true
            }
        }

        todoCategoryTravel.setOnClickListener{
            if(todoCategoryTravel.isChecked){
                activeCategory = Category.TRAVEL
                todoCategoryNone.isChecked = false
                todoCategoryOther.isChecked = false
                todoCategoryPersonal.isChecked = false
                todoCategoryWork.isChecked = false
            }else{
                todoCategoryNone.isChecked = true
            }
        }

        todoCategoryOther.setOnClickListener{
            if(todoCategoryOther.isChecked){
                activeCategory = Category.OTHER
                todoCategoryNone.isChecked = false
                todoCategoryPersonal.isChecked = false
                todoCategoryTravel.isChecked = false
                todoCategoryWork.isChecked = false
            }else{
                todoCategoryNone.isChecked = true
            }
        }
        // nightmare ends...

        addButton = findViewById(R.id.add_todo_button)

        addButton.setOnClickListener{
            val replyIntent = Intent()
            if(TextUtils.isEmpty(todoName.text)){
                setResult(Activity.RESULT_CANCELED, replyIntent)
            }else{
                replyIntent.putExtra("name", todoName.text.toString())
                replyIntent.putExtra("description", todoDescription.text.toString())
                replyIntent.putExtra("category", activeCategory.toString())
                replyIntent.putExtra("importance", activeImportance)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }
}