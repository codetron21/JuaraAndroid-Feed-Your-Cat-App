package com.codetron.feedyourcat.features.addcat

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.codetron.feedyourcat.R
import com.codetron.feedyourcat.common.dialog.LoadingDialog
import com.codetron.feedyourcat.databinding.FragmentAddCatBinding
import com.codetron.feedyourcat.model.Cat
import com.google.android.material.snackbar.Snackbar
import java.util.*

class AddCatFragment : Fragment() {

    private var _binding: FragmentAddCatBinding? = null
    private val binding get() = _binding

    private val viewModel by viewModels<AddCatViewModel> { AddCatViewModel.factory(requireContext()) }

    private var snackbar: Snackbar? = null
    private var alertDialog: AlertDialog? = null
    private var loadingDialog: LoadingDialog? = null

    private var id: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddCatBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initState()
        observeViewModel()
        buttonListeners()
    }

    override fun onDestroyView() {
        loadingDialog?.dismiss()
        alertDialog?.dismiss()
        snackbar?.dismiss()
        _binding = null
        super.onDestroyView()
    }

    private fun initState() {
        val id = (requireActivity() as AddCatActivity).getId()
        binding?.buttonDelete?.isVisible = false
        if (id == null || id <= 0L) {
            viewModel.setStateCat(StateCat.ADD)
            binding?.toolbar?.menu?.findItem(R.id.action_edit)?.isVisible = false
            binding?.buttonUpsert?.setText(R.string.text_save)
        } else {
            this.id = id
            viewModel.getDataById(id)
            viewModel.setStateCat(StateCat.UPDATE)
            binding?.toolbar?.menu?.findItem(R.id.action_edit)?.isVisible = true
            binding?.buttonUpsert?.setText(R.string.text_update)
        }
    }

    private fun observeViewModel() {
        viewModel.data.observe(viewLifecycleOwner) { data ->
            if (data != null) {
                binding?.inputCatName?.setText(data.name)

                binding?.imageCat?.load(data.photo) {
                    crossfade(true)
                    placeholder(R.color.green_light_secondary)
                }
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                loadingDialog = LoadingDialog()
                loadingDialog?.show(parentFragmentManager, LoadingDialog.TAG)
            } else {
                loadingDialog?.dismiss()
            }
        }

        viewModel.message.observe(viewLifecycleOwner) { event ->
            val message = event.get() ?: return@observe
            snackbar = binding?.root?.let { Snackbar.make(it, message, Snackbar.LENGTH_LONG) }
            snackbar?.show()
        }

        viewModel.isEdited.observe(viewLifecycleOwner) { edit ->
            val menuItem = binding?.toolbar?.menu?.findItem(R.id.action_edit) ?: return@observe
            if (!menuItem.isVisible) return@observe

            binding?.buttonDelete?.isVisible = edit
            binding?.buttonUpsert?.isVisible = edit
            binding?.inputCatName?.isEnabled = edit
            binding?.buttonBirthDate?.isEnabled = edit

            if (edit) {
                menuItem.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_close)
            } else {
                menuItem.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_edit)
            }
        }

        viewModel.showExitDialog.observe(viewLifecycleOwner) { event ->
            event.get() ?: return@observe
            alertDialog = AlertDialog.Builder(requireContext())
                .setMessage(R.string.message_exit)
                .setPositiveButton(R.string.action_yes) { di, _ ->
                    requireActivity().onBackPressed()
                    di.dismiss()
                }
                .setNegativeButton(R.string.action_no) { di, _ ->
                    di.dismiss()
                }
                .create()
            alertDialog?.show()
        }

        viewModel.showCancelEditDialog.observe(viewLifecycleOwner) { event ->
            event.get() ?: return@observe
            alertDialog = AlertDialog.Builder(requireContext())
                .setMessage(R.string.message_cancel_edit)
                .setPositiveButton(R.string.action_yes) { di, _ ->
                    viewModel.onCancelEditClicked()
                    di.dismiss()
                }
                .setNegativeButton(R.string.action_no) { di, _ ->
                    di.dismiss()
                }
                .create()
            alertDialog?.show()
        }

        viewModel.showDeleteDialog.observe(viewLifecycleOwner) { event ->
            event.get() ?: return@observe
            alertDialog = AlertDialog.Builder(requireContext())
                .setMessage(R.string.message_delete_data)
                .setPositiveButton(R.string.action_yes) { di, _ ->
                    viewModel.deleteDataById(id)
                    requireActivity().onBackPressed()
                    di.dismiss()
                }
                .setNegativeButton(R.string.action_no) { di, _ ->
                    di.dismiss()
                }
                .create()
            alertDialog?.show()
        }
    }

    private fun buttonListeners() {
        binding?.buttonBirthDate?.setOnClickListener {

        }

        binding?.toolbar?.setNavigationOnClickListener {
            viewModel.onButtonExitClicked()
        }

        binding?.imageCat?.setOnClickListener {

        }

        binding?.buttonDelete?.setOnClickListener {
            viewModel.onButtonDeleteClicked()
        }

        binding?.buttonUpsert?.setOnClickListener {
            val name = binding?.inputCatName?.text?.toString()?.trim()

            if (name.isNullOrEmpty()) {
                snackbar = Snackbar.make(it, R.string.message_input_error, Snackbar.LENGTH_LONG)
                snackbar?.show()
                return@setOnClickListener
            }

            val cat = Cat(name, "", Date())
            viewModel.upsertData(cat)
        }

        binding?.toolbar?.setOnMenuItemClickListener { menu ->
            if (menu.itemId == R.id.action_edit) {
                viewModel.onMenuClicked()
            }
            false
        }

    }

}