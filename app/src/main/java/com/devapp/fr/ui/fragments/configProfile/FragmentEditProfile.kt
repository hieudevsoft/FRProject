package com.devapp.fr.ui.fragments.configProfile

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.devapp.fr.R
import com.devapp.fr.adapters.InformationAdapter
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.data.models.items.InformationItem
import com.devapp.fr.databinding.FragmentEditProfileBinding
import com.devapp.fr.ui.viewmodels.SharedViewModel
import com.devapp.fr.util.DataHelper
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@AndroidEntryPoint
class FragmentEditProfile : BaseFragment<FragmentEditProfileBinding>() {
    val TAG = "FragmentEditProfile"
    private lateinit var informationAdapter: InformationAdapter
    private val sharedViewModel: SharedViewModel by activityViewModels()
    var listTemp = mutableListOf<InformationItem>()
    override fun onSetupView() {
        setupRecyclerViewInformation()
        handleEventElement()
        subscriberObserver()
    }

    private fun subscriberObserver() {

        sharedViewModel.getListItemInformation().observe(viewLifecycleOwner){
            informationAdapter.submitList(it)
        }

        launchRepeatOnLifeCycleWhenStarted {
            sharedViewModel.getSharedFlowTall()
                .distinctUntilChanged()
                .collectLatest {
                    val data = "${
                        if(it==91) "<91"
                        else if(it==220) ">220"
                        else it.toString()
                    } cm"
                    sharedViewModel.getPositionInformation().value?.let { pos ->
                        if(pos==0) {
                            resetAdapter(pos,if(it!=-1) data else "")
                            sharedViewModel.setListItemInformation(listTemp)
                        }
                    }
                }
        }

        launchRepeatOnLifeCycleWhenStarted {
            sharedViewModel.getSharedFlowChild()
                .distinctUntilChanged()
                .collectLatest {
                    if(it!=-1){
                        val listChild = DataHelper.getListChild()
                        val data = listChild[it].text
                        sharedViewModel.getPositionInformation().value?.let { pos ->
                            if(pos==1){
                                resetAdapter(pos,if(it==listChild.size-1) "" else data)
                                sharedViewModel.setListItemInformation(listTemp)
                            }
                        }
                    }
                }
        }

        launchRepeatOnLifeCycleWhenStarted {
            sharedViewModel.getSharedFlowDrink()
                .distinctUntilChanged()
                .collectLatest {
                    if(it!=-1){
                        val listDrink = DataHelper.getListDrink()
                        val data = listDrink[it].text
                        sharedViewModel.getPositionInformation().value?.let { pos ->
                            if(pos==2){
                                resetAdapter(pos,if(it==listDrink.size-1) "" else data)
                                sharedViewModel.setListItemInformation(listTemp)
                            }
                        }
                    }
                }
        }
    }

    private fun resetAdapter(pos:Int, data:String){
        listTemp = sharedViewModel.getListItemInformation().value!!.toMutableList()
        listTemp[pos].content = data
    }

    private fun handleEventElement() {
        informationAdapter.setOnItemClickListener {
            when(it){
                0-> {
                    sharedViewModel.setPositionInformation(0)
                    findNavController().navigate(FragmentEditProfileDirections.actionFragmentEditProfileToFragmentTall(true))
                }
                1-> {
                    sharedViewModel.setPositionInformation(1)
                    findNavController().navigate(FragmentEditProfileDirections.actionFragmentEditProfileToFragmentChild(true))
                }
                2-> {
                    sharedViewModel.setPositionInformation(2)
                    findNavController().navigate(FragmentEditProfileDirections.actionFragmentEditProfileToFragmentDrink(true))
                }
            }
        }
    }

    private fun setupRecyclerViewInformation() {
        informationAdapter = InformationAdapter()
        binding.rcInformation.adapter = informationAdapter
        sharedViewModel.setListItemInformation(getListItemInformation())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnBack.setOnClickWithAnimationListener { findNavController().popBackStack() }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getListItemInformation() = listOf(
        InformationItem(R.drawable.ic_tall,"Cao",""),
        InformationItem(R.drawable.ic_kid,"Trẻ con",""),
        InformationItem(R.drawable.ic_beer,"Rượu bia",""),
        InformationItem(R.drawable.ic_status_marry,"Về hôn nhân",""),
        InformationItem(R.drawable.ic_gender,"Giới tính",""),
        InformationItem(R.drawable.ic_smoking,"Hút thuốc",""),
        InformationItem(R.drawable.ic_pet,"Thú cưng",""),
        InformationItem(R.drawable.ic_religion,"Tôn giáo",""),
        InformationItem(R.drawable.ic_certificate,"Học vấn",""),
        InformationItem(R.drawable.ic_personality,"Tính cách",""),
    )

}