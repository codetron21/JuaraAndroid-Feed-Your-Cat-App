package com.codetron.feedyourcat.features.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.codetron.feedyourcat.databinding.FragmentListMainBinding

class ListCatFragment : Fragment() {

    private var _binding: FragmentListMainBinding? = null
    private val binding get() = _binding

    private val viewModel by activityViewModels<MainViewModel> {
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

        observeViewModel()

        binding?.buttonSort?.setOnClickListener {
            viewModel.onButtonSortCatsClicked()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun observeViewModel() {
        viewModel.textSortCats.observe(viewLifecycleOwner) { resString ->
            binding?.buttonSort?.setText(resString)
        }
    }

}