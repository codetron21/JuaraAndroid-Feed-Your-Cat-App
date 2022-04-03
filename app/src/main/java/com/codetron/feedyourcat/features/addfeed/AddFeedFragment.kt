package com.codetron.feedyourcat.features.addfeed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.codetron.feedyourcat.databinding.FragmentAddFeedBinding

class AddFeedFragment : Fragment() {

    private var _binding: FragmentAddFeedBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddFeedBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback {

        }

        buttonListeners()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun buttonListeners() {

        binding?.buttonAddTime?.setOnClickListener {

        }

        binding?.buttonSubmit?.setOnClickListener {

        }

        binding?.toolbar?.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

}