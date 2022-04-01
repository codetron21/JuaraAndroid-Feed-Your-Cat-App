package com.codetron.feedyourcat.features.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.codetron.feedyourcat.common.adapter.ListSortAdapter
import com.codetron.feedyourcat.common.dialog.SortDialog
import com.codetron.feedyourcat.databinding.ActivityMainBinding
import com.codetron.feedyourcat.features.main.MainViewModel.Companion.State

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding

    private val viewModel by viewModels<MainViewModel> {
        MainViewModel.factory
    }

    private var sortDialog: SortDialog? = null

    private lateinit var listSortAdapter: ListSortAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        listSortAdapter = ListSortAdapter { id ->
            when (viewModel.state) {
                State.FEED -> {
                    viewModel.setSelectedSortFeed(id)
                }
                State.CATS -> {
                    viewModel.setSelectedSortCats(id)
                }
            }
            sortDialog?.dismiss()
        }

        viewModel.showDialog.observe(this) { data ->
            if (data.isEmpty()) return@observe

            listSortAdapter.setData(data)
            sortDialog = SortDialog(listSortAdapter)
            sortDialog?.show(supportFragmentManager, SortDialog.TAG)
        }

    }

    override fun onDestroy() {
        sortDialog?.dismiss()
        _binding = null
        super.onDestroy()
    }

}