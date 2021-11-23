package com.devapp.fr.adapters

import androidx.collection.SparseArrayCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.lang.ref.WeakReference

class MainViewPagerAdapter(
    private val listFragment:List<Fragment>,
    fm:FragmentManager,
    lifecycle:Lifecycle
):FragmentStateAdapter(fm,lifecycle) {
    private val holder : SparseArrayCompat<WeakReference<Fragment>> = SparseArrayCompat()

    override fun getItemCount(): Int {
        return listFragment.size
    }

    override fun createFragment(position: Int): Fragment {
        holder.put(position, WeakReference(listFragment[position]))
        return holder[position]?.get()!!
    }

    private fun getFragmentAtPosition(position:Int):Fragment{
        return holder[position]?.get()!!
    }
}