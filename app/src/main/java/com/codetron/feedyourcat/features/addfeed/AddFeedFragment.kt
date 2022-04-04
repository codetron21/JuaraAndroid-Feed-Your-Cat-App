package com.codetron.feedyourcat.features.addfeed

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codetron.feedyourcat.R
import com.codetron.feedyourcat.common.adapter.ListCatSelectedAdapter
import com.codetron.feedyourcat.common.adapter.ListTimeAdapter
import com.codetron.feedyourcat.common.dialog.LoadingDialog
import com.codetron.feedyourcat.common.dialog.TimeDialog
import com.codetron.feedyourcat.databinding.FragmentAddFeedBinding
import com.google.android.material.snackbar.Snackbar

class AddFeedFragment : Fragment() {

    private var _binding: FragmentAddFeedBinding? = null
    private val binding get() = _binding

    private var snackbar: Snackbar? = null
    private var timeDialog: TimeDialog? = null
    private var alertDialog: AlertDialog? = null
    private var loadingDialog: LoadingDialog? = null

    private val listCatSelectedAdapter by lazy {
        ListCatSelectedAdapter(viewModel::setSelectedCat)
    }

    private val listTimeAdapter by lazy {
        ListTimeAdapter(viewModel::removeTimeItem)
    }

    private val viewModel by viewModels<AddFeedViewModel> {
        AddFeedViewModel.factory(requireContext())
    }

    private val timeCallback = { hour: Int, minute: Int ->
        viewModel.setTime(hour * 60 + minute)
    }

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
            viewModel.onButtonExitClicked()
        }

        setupView()
        observeViewModel()
        buttonListeners()
    }

    override fun onDestroyView() {
        loadingDialog?.dismiss()
        alertDialog?.dismiss()
        timeDialog?.dismiss()
        snackbar?.dismiss()
        _binding = null
        super.onDestroyView()
    }

    private fun setupView() {
        binding?.listCat?.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding?.listCat?.adapter = listCatSelectedAdapter

        binding?.listTime?.layoutManager = LinearLayoutManager(requireContext())
        binding?.listTime?.adapter = listTimeAdapter
    }

    private fun observeViewModel() {
        viewModel.listTime.observe(viewLifecycleOwner) {
            listTimeAdapter.submitList(it)
        }

        viewModel.listCats.observe(viewLifecycleOwner) {
            listCatSelectedAdapter.setData(it)
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                loadingDialog = LoadingDialog()
                loadingDialog?.show(parentFragmentManager, LoadingDialog.TAG)
            } else {
                loadingDialog?.dismiss()
            }
        }

        viewModel.showTimeDialog.observe(viewLifecycleOwner) { event ->
            event.get() ?: return@observe
            timeDialog = TimeDialog(timeCallback)
            timeDialog?.show(parentFragmentManager, TimeDialog.TAG)
        }

        viewModel.showExitDialog.observe(viewLifecycleOwner) { event ->
            event.get() ?: return@observe
            alertDialog = AlertDialog.Builder(requireContext())
                .setMessage(R.string.message_exit)
                .setPositiveButton(R.string.action_yes) { di, _ ->
                    requireActivity().finish()
                    di.dismiss()
                }
                .setNegativeButton(R.string.action_no) { di, _ ->
                    di.dismiss()
                }
                .create()
            alertDialog?.show()
        }

        viewModel.showMessage.observe(viewLifecycleOwner) { event ->
            val resString = event.get() ?: return@observe
            snackbar = binding?.root?.let { Snackbar.make(it, resString, Snackbar.LENGTH_LONG) }
            snackbar?.show()
        }
    }

    private fun buttonListeners() {
        binding?.buttonAddTime?.setOnClickListener {
            viewModel.onButtonAddClicked()
        }

        binding?.buttonSubmit?.setOnClickListener {
            viewModel.onButtonSubmitClicked {
                requireActivity().finish()
            }
        }

        binding?.toolbar?.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

}