package com.codetron.feedyourcat.common.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.codetron.feedyourcat.common.adapter.ListSortAdapter
import com.codetron.feedyourcat.databinding.LayoutBottomSheetSortBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SortDialog(
    private val listAdapter: ListSortAdapter
) : BottomSheetDialogFragment() {

    private var _binding: LayoutBottomSheetSortBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LayoutBottomSheetSortBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setupView() {
        binding?.listSortBy?.adapter = listAdapter
        binding?.listSortBy?.layoutManager = LinearLayoutManager(requireContext())
        binding?.listSortBy?.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayout.VERTICAL
            )
        )
    }

    companion object {
        const val TAG = "SortDialog"
    }

}