package com.codetron.feedyourcat.features.main

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.Fragment
import com.codetron.feedyourcat.R
import com.codetron.feedyourcat.common.adapter.PagerMainAdapter
import com.codetron.feedyourcat.databinding.FragmentMainBinding

class MainFragment : Fragment(), ActionMode.Callback {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding

    private val pagerAdapter by lazy { PagerMainAdapter(this) }

    private var menuContextListener: MenuContextListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val appCompatActivity = requireActivity() as MainActivity
        appCompatActivity.setSupportActionBar(binding?.toolbar)
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

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_delete) {
            menuContextListener?.onActionItemClicked(mode, item)
            return true
        }
        return false
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        menuContextListener?.onDestroyActionMode(mode)
    }

    fun setMenuContextListener(listener: MenuContextListener) {
        this.menuContextListener = listener
    }

    fun disableView(){
        binding?.switchTab?.setEnabledTab(false)
        binding?.pagerMenu?.isUserInputEnabled = false
    }

    fun enabledView(){
        binding?.switchTab?.setEnabledTab(true)
        binding?.pagerMenu?.isUserInputEnabled = true
    }

    private fun setupView() {
        binding?.pagerMenu?.adapter = pagerAdapter
        binding?.pagerMenu?.let { binding?.switchTab?.attachWithViewPager2(it) }
    }

    interface MenuContextListener {
        fun onActionItemClicked(mode: ActionMode?, item: MenuItem?)
        fun onDestroyActionMode(mode: ActionMode?)
    }
}