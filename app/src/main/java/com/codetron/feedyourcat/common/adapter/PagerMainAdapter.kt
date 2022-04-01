package com.codetron.feedyourcat.common.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.codetron.feedyourcat.features.main.ListCatFragment
import com.codetron.feedyourcat.features.main.ListFeedFragment

class PagerMainAdapter(f: Fragment) : FragmentStateAdapter(f) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ListFeedFragment()
            1 -> ListCatFragment()
            else -> throw IllegalStateException()
        }
    }

}