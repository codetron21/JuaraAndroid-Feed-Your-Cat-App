package com.codetron.feedyourcat.features.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.codetron.feedyourcat.common.adapter.PagerMainAdapter
import com.codetron.feedyourcat.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding

    private val pagerAdapter by lazy { PagerMainAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    private fun setupView() {
        binding?.pagerMenu?.adapter = pagerAdapter
        binding?.pagerMenu?.let { binding?.switchTab?.attachWithViewPager2(it) }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}