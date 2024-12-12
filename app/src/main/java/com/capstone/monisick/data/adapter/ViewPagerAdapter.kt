package com.capstone.monisick.data.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.capstone.monisick.ui.history.log.LogFragment
import com.capstone.monisick.ui.history.makan.MakanFragment
import com.capstone.monisick.ui.history.obat.ObatFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ObatFragment()
            1 -> MakanFragment()
            2 -> LogFragment()
            else -> throw IllegalStateException("Invalid position $position")
        }
    }
}
