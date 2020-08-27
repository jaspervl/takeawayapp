package com.jvl.assignment.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jvl.assignment.R

/**
 * This application will only be run with a single activity as it relies on the new navigation component
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}