package com.codetron.feedyourcat.features.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.codetron.feedyourcat.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }


}