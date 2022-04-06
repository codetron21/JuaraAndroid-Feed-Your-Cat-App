package com.codetron.feedyourcat.features.main.feed

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ActionMode
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.codetron.feedyourcat.R
import com.codetron.feedyourcat.common.adapter.FeedCatItemKeyProvider
import com.codetron.feedyourcat.common.adapter.FeedCatItemsDetailsLookup
import com.codetron.feedyourcat.common.adapter.ListFeedCatAdapter
import com.codetron.feedyourcat.common.alarm.AlarmReceiver
import com.codetron.feedyourcat.databinding.FragmentListMainBinding
import com.codetron.feedyourcat.features.main.MainActivity
import com.codetron.feedyourcat.features.main.MainFragment
import com.codetron.feedyourcat.features.main.MainViewModel
import com.codetron.feedyourcat.model.FeedCat
import com.codetron.feedyourcat.utils.combineId
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch

private const val TAG = "ListFeedFragment"

class ListFeedFragment : Fragment(), MainFragment.MenuContextListener {

    private var _binding: FragmentListMainBinding? = null
    private val binding get() = _binding

    private val listFeedCatAdapter by lazy { ListFeedCatAdapter() }

    private val viewModel by viewModels<ListFeedViewModel> {
        ListFeedViewModel.factory(requireContext())
    }

    private val sharedViewModel by activityViewModels<MainViewModel> {
        MainViewModel.factory
    }

    private val alarmReceiver by lazy { AlarmReceiver() }

    private var snackbar: Snackbar? = null
    private var actionMode: ActionMode? = null
    private var tracker: SelectionTracker<Long>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListMainBinding.inflate(layoutInflater, container, false)
        (parentFragment as? MainFragment)?.setMenuContextListener(this)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        observeViewModel()
        buttonListeners()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        tracker?.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        tracker?.onRestoreInstanceState(savedInstanceState)
        if (tracker?.hasSelection() == true) {
            actionMode =
                (activity as MainActivity).startSupportActionMode(parentFragment as MainFragment)
            actionMode?.title = getString(R.string.action_selected, tracker?.selection?.size())
        }
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onDestroyView() {
        snackbar?.dismiss()
        _binding = null
        super.onDestroyView()
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?) {
        val selectedList = listFeedCatAdapter.currentList.filter {
            tracker?.selection?.contains(it.feedId) ?: false
        }.toMutableList()

        lifecycleScope.launch {
            ensureActive()
            cancelAlarm(selectedList)
            viewModel.deleteListData(selectedList)
        }

        actionMode?.finish()
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        tracker?.clearSelection()
        actionMode = null

        val adapter = (binding?.listData?.adapter as ListFeedCatAdapter)
        binding?.listData?.adapter = adapter
        binding?.buttonAdd?.isEnabled = true
        binding?.buttonSort?.isEnabled = true
        (parentFragment as MainFragment).enabledView()
    }

    private fun cancelAlarm(list: List<FeedCat>) {
        list.forEach { feedCat ->
            feedCat.times.forEachIndexed { index, _ ->
                val id = feedCat.catId.combineId(index.toLong()).toInt()
                if (alarmReceiver.isAlarmSet(requireContext(), id)) {
                    Log.d(TAG, "remove notification id $id")
                    alarmReceiver.cancelAlarm(requireContext(), id)
                }
            }
        }
    }

    private fun setupView() {
        binding?.textEmpty?.setCompoundDrawablesWithIntrinsicBounds(
            0,
            R.drawable.ic_empty_feed,
            0,
            0
        )

        val rvData = binding?.listData ?: return

        rvData.adapter = listFeedCatAdapter
        rvData.layoutManager = StaggeredGridLayoutManager(
            2, RecyclerView.VERTICAL
        )

        tracker = SelectionTracker.Builder(
            "selectionItem",
            rvData,
            FeedCatItemKeyProvider(listFeedCatAdapter),
            FeedCatItemsDetailsLookup(rvData),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        listFeedCatAdapter.tracker = tracker

        tracker?.addObserver(
            object : SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    super.onSelectionChanged()
                    binding?.buttonAdd?.isEnabled = false
                    binding?.buttonSort?.isEnabled = false
                    (parentFragment as MainFragment).disableView()

                    if (actionMode == null) {
                        val currentActivity = requireActivity() as MainActivity
                        actionMode =
                            currentActivity.startSupportActionMode(parentFragment as MainFragment)
                    }

                    val items = tracker?.selection?.size() ?: 0
                    if (items > 0) {
                        actionMode?.title = getString(R.string.action_selected, items)
                    } else {
                        actionMode?.finish()
                    }
                }
            }
        )
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

        viewModel.message.observe(viewLifecycleOwner) { event ->
            val resultTotal = event.get() ?: return@observe

            snackbar = binding?.root?.let {
                Snackbar.make(
                    it,
                    getString(R.string.message_success_delete, resultTotal),
                    Snackbar.LENGTH_LONG
                )
            }
            snackbar?.show()
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