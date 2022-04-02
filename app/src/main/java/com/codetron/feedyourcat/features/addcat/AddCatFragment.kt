package com.codetron.feedyourcat.features.addcat

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.codetron.feedyourcat.R
import com.codetron.feedyourcat.common.dialog.DateDialog
import com.codetron.feedyourcat.common.dialog.LoadingDialog
import com.codetron.feedyourcat.databinding.FragmentAddCatBinding
import com.codetron.feedyourcat.model.Cat
import com.codetron.feedyourcat.model.StateCat
import com.codetron.feedyourcat.utils.formatString
import com.google.android.material.snackbar.Snackbar
import java.util.*

class AddCatFragment : Fragment() {

    private var _binding: FragmentAddCatBinding? = null
    private val binding get() = _binding

    private val viewModel by viewModels<AddCatViewModel> { AddCatViewModel.factory(requireContext()) }

    private var snackbar: Snackbar? = null
    private var dateDialog: DateDialog? = null
    private var alertDialog: AlertDialog? = null
    private var loadingDialog: LoadingDialog? = null

    private var id: Long = 0L
    private var date: Date? = null

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

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.onButtonExitClicked()
        }

        initState()
        observeViewModel()
        buttonListeners()
    }

    override fun onDestroyView() {
        loadingDialog?.dismiss()
        alertDialog?.dismiss()
        dateDialog?.dismiss()
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
                date = data.birthDate

                binding?.inputCatName?.setText(data.name)
                binding?.buttonBirthDate?.text = data.birthDate.formatString()
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
                    requireActivity().finish()
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
                    requireActivity().finish()
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
        binding?.imageCat?.setOnClickListener {

        }

        binding?.toolbar?.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        binding?.buttonDelete?.setOnClickListener {
            viewModel.onButtonDeleteClicked()
        }

        binding?.buttonUpsert?.setOnClickListener {
            val name = binding?.inputCatName?.text?.toString()?.trim()

            if (name.isNullOrEmpty()) {
                snackbar = Snackbar.make(it, R.string.message_name_error, Snackbar.LENGTH_LONG)
                snackbar?.show()
                return@setOnClickListener
            }

            if (date == null) {
                snackbar =
                    Snackbar.make(it, R.string.message_birth_date_error, Snackbar.LENGTH_LONG)
                snackbar?.show()
                return@setOnClickListener
            }

            val cat = if (id > 0) {
                Cat(id, name, "", date!!)
            } else {
                Cat(name, "", date!!)
            }

            viewModel.upsertData(cat)
            if (viewModel.state == StateCat.UPDATE) {
                viewModel.onCancelEditClicked()
            }
        }

        binding?.toolbar?.setOnMenuItemClickListener { menu ->
            if (menu.itemId == R.id.action_edit) {
                viewModel.onMenuClicked()
            }
            false
        }

        binding?.buttonBirthDate?.setOnClickListener {
            dateDialog =
                when {
                    date != null -> {
                        val cal = Calendar.getInstance()
                        cal.time = date!!
                        val defaultYear = cal[Calendar.YEAR]
                        val defaultMonth = cal[Calendar.MONTH]
                        val defaultDay = cal[Calendar.DAY_OF_MONTH]

                        DateDialog(defaultYear, defaultMonth, defaultDay) { year, month, day ->
                            (it as Button).text =
                                getString(R.string.birth_format, day, month + 1, year)
                            val c = Calendar.getInstance()
                            c[Calendar.YEAR] = year
                            c[Calendar.MONTH] = month
                            c[Calendar.DAY_OF_MONTH] = day
                            date = c.time
                        }
                    }
                    viewModel.data.value != null -> {
                        val cal = Calendar.getInstance()
                        val birthDate = viewModel.data.value!!
                        cal.time = birthDate.birthDate
                        val defaultYear = cal[Calendar.YEAR]
                        val defaultMonth = cal[Calendar.MONTH]
                        val defaultDay = cal[Calendar.DAY_OF_MONTH]

                        DateDialog(defaultYear, defaultMonth, defaultDay) { year, month, day ->
                            (it as Button).text =
                                getString(R.string.birth_format, day, month + 1, year)
                            val c = Calendar.getInstance()
                            c[Calendar.YEAR] = year
                            c[Calendar.MONTH] = month
                            c[Calendar.DAY_OF_MONTH] = day
                            date = c.time
                        }
                    }
                    else -> {
                        DateDialog { year, month, day ->
                            (it as Button).text =
                                getString(R.string.birth_format, day, month + 1, year)
                            val c = Calendar.getInstance()
                            c[Calendar.YEAR] = year
                            c[Calendar.MONTH] = month
                            c[Calendar.DAY_OF_MONTH] = day
                            date = c.time
                        }
                    }
                }

            dateDialog?.show(parentFragmentManager, DateDialog.TAG)
        }
    }

}