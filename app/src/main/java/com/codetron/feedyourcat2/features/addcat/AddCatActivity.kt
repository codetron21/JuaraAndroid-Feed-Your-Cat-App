package com.codetron.feedyourcat2.features.addcat

import androidx.appcompat.app.AppCompatActivity
import com.codetron.feedyourcat2.R

class AddCatActivity : AppCompatActivity(R.layout.activity_add_cat) {

    fun getId(): Long? = intent.extras?.getLong(KEY_ID)

    companion object {
        const val KEY_ID = "KEY_ID"
    }

}