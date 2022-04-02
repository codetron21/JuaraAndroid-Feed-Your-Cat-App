package com.codetron.feedyourcat.features.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codetron.feedyourcat.R
import com.codetron.feedyourcat.common.adapter.ListCatAdapter
import com.codetron.feedyourcat.databinding.FragmentListMainBinding
import com.codetron.feedyourcat.features.addcat.AddCatActivity

class ListCatFragment : Fragment() {

    private var _binding: FragmentListMainBinding? = null
    private val binding get() = _binding

    private val listCatAdapter by lazy {
        ListCatAdapter { id ->
            val bundle = bundleOf(AddCatActivity.KEY_ID to id)
            findNavController().navigate(R.id.nav_main_to_add_cat, bundle)
        }
    }

    private val viewModel by viewModels<ListCatViewModel>() {
        ListCatViewModel.factory(requireContext())
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

        if (savedInstanceState == null) {
            viewModel.getAllCats()
        }

        setupView()
        observeViewModel()

        binding?.buttonSort?.setOnClickListener {
            sharedViewModel.onButtonSortCatsClicked()
        }

        binding?.buttonAdd?.setOnClickListener {
            findNavController().navigate(R.id.nav_main_to_add_cat)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun observeViewModel() {
        viewModel.data.observe(viewLifecycleOwner) { data ->
            binding?.textEmpty?.isVisible = data.isEmpty()
            binding?.buttonSort?.isVisible = data.isNotEmpty()
            binding?.listData?.isVisible = data.isNotEmpty()
            if (data.isNotEmpty()) {
                listCatAdapter.submitList(data)
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding?.progressBar?.isVisible = loading
        }

        sharedViewModel.textSortCats.observe(viewLifecycleOwner) { resString ->
            binding?.buttonSort?.setText(resString)
        }
    }

    private fun setupView() {
        binding?.textEmpty?.setCompoundDrawablesWithIntrinsicBounds(
            0,
            R.drawable.ic_empty_cats,
            0,
            0
        )

        binding?.listData?.layoutManager =
            GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        binding?.listData?.adapter = listCatAdapter
    }

}