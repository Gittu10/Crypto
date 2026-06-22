package com.example.cryptoapp.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cryptoapp.TopLossFragment

class TopLossGainPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = TopLossFragment()
        val bundle = Bundle()
        bundle.putInt("position", position)
        fragment.arguments = bundle
        return fragment
    }
}