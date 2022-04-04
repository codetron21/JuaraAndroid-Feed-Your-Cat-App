package com.codetron.feedyourcat.features.main.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.codetron.feedyourcat.R
import com.codetron.feedyourcat.common.adapter.ListFeedCatAdapter
import com.codetron.feedyourcat.databinding.FragmentListMainBinding
import com.codetron.feedyourcat.features.main.MainViewModel

class ListFeedFragment : Fragment() {

    private var _binding: FragmentListMainBinding? = null
    private val binding get() = _binding

    private val listFeedCatAdapter by lazy { ListFeedCatAdapter() }

    private val viewModel by viewModels<ListFeedViewModel> {
        ListFeedViewModel.factory(requireContext())
    }

    private val sharedViewModel by activityViewModels<MainViewModel> {
        MainViewModel.factory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListMainBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        observeViewModel()
        buttonListeners()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setupView() {
        binding?.textEmpty?.setCompoundDrawablesWithIntrinsicBounds(
            0,
            R.drawable.ic_empty_feed,
            0,
            0
        )

        binding?.listData?.adapter = listFeedCatAdapter
        binding?.listData?.layoutManager =
            StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
    }

    private fun observeViewModel() {
        viewModel.data.observe(viewLifecycleOwner) { data ->
            binding?.textEmpty?.isVisible = data.isEmpty()
            binding?.buttonSort?.isVisible = data.isNotEmpty()
            binding?.listData?.isVisible = data.isNotEmpty()
            if (data.isNotEmpty()) {
                listFeedCatAdapter.submitList(data)
            }
        }

        viewModel.showAddButton.observe(viewLifecycleOwner) {
            binding?.buttonAdd?.isVisible = it
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding?.progressBar?.isVisible = loading
        }

        sharedViewModel.textSortFeed.observe(viewLifecycleOwner) { resString ->
            binding?.buttonSort?.setText(resString)
        }

        sharedViewModel.sortIdFeeds.observe(viewLifecycleOwner) { id ->
            viewModel.getAll(id)
        }
    }

    private fun buttonListeners() {
        binding?.buttonSort?.setOnClickListener {
            sharedViewModel.onButtonSortFeedClicked()
        }

        binding?.buttonAdd?.setOnClickListener {
            findNavController().navigate(R.id.nav_main_to_add_feed)
        }
    }

}