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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codetron.feedyourcat.R
import com.codetron.feedyourcat.databinding.FragmentListMainBinding
import com.codetron.feedyourcat.features.main.MainViewModel

class ListFeedFragment : Fragment() {

    private var _binding: FragmentListMainBinding? = null
    private val binding get() = _binding

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

        initView()
        observeViewModel()
        buttonListeners()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initView() {
        binding?.listData?.layoutManager =
            GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
    }

    private fun observeViewModel() {
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding?.progressBar?.isVisible = loading
        }

        sharedViewModel.textSortFeed.observe(viewLifecycleOwner) { resString ->
            binding?.buttonSort?.setText(resString)
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