package com.devapp.fr.ui.fragments.information

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.devapp.fr.R
import com.devapp.fr.adapters.InterestAdapter
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.data.models.items.InterestItem
import com.devapp.fr.databinding.FragmentInterestBinding
import com.google.android.flexbox.*
import kotlin.random.Random

class FragmentInterest : BaseFragment<FragmentInterestBinding>() {
    val TAG = "FragmentInterest"
    private lateinit var interestAdapter: InterestAdapter
    override fun onSetupView() {
        val flexLayoutManager = FlexboxLayoutManager(requireContext(),FlexDirection.ROW,FlexWrap.WRAP)
        flexLayoutManager.justifyContent = JustifyContent.SPACE_BETWEEN
        flexLayoutManager.alignItems = AlignItems.CENTER
        interestAdapter = InterestAdapter()
        binding.rcvInterest.apply {
            layoutManager = flexLayoutManager
            itemAnimator = null
            adapter = interestAdapter
        }
        interestAdapter.submitList(getListInterest())

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getListInterest(): List<InterestItem> {
        val result = mutableListOf<InterestItem>()
        val listColorSelected = resources.getStringArray(R.array.tag_color)
        val listColorDefault = resources.getStringArray(R.array.tag_color_hint)
        val listInterest = resources.getStringArray(R.array.hobby)
        listInterest.forEach {
            val indexColor = Random.nextInt(0, listColorDefault.size - 1)
            result.add(
                InterestItem(
                    "#"+it,
                    false,
                    Color.parseColor(listColorDefault[indexColor]),
                    Color.parseColor(listColorSelected[indexColor])
                )
            )
        }
        return result.toList()
    }

    fun getListInterestSelected() = interestAdapter.listIndexSelected.sorted()
}