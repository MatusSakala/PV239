package cz.muni.fi.pv239.gtodolist.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import cz.muni.fi.pv239.gtodolist.R

class DisplayCategoryActivity : AppCompatActivity() {

    private val TAG = DisplayCategoryActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.display_category)
        Log.d(TAG, "STARTED DISPLAY CATEGORY ACTIVITY")
    }
}