package com.devapp.fr.ui.fragments.information

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.devapp.fr.R
import com.devapp.fr.adapters.InterestAdapter
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.data.models.items.InterestItem
import com.devapp.fr.data.models.items.SlideItem
import com.devapp.fr.databinding.FragmentInterestBinding
import com.devapp.fr.network.ResourceRemote
import com.devapp.fr.ui.viewmodels.AuthAndProfileViewModel
import com.devapp.fr.ui.viewmodels.SharedViewModel
import com.devapp.fr.ui.widgets.LoadingDialog
import com.devapp.fr.util.UiHelper.toVisible
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenResumed
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenStarted
import com.devapp.fr.util.extensions.showToast
import com.devapp.fr.util.storages.SharedPreferencesHelper
import com.google.android.flexbox.*
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class FragmentInterest : BaseFragment<FragmentInterestBinding>() {
    val TAG = "FragmentInterest"
    private lateinit var interestAdapter: InterestAdapter
    private val args:FragmentInterestArgs by navArgs()
    private val sharedViewModel:SharedViewModel by activityViewModels()
    private val authAndProfileViewModel: AuthAndProfileViewModel by activityViewModels()
    @Inject
    lateinit var pref:SharedPreferencesHelper

    override fun onSetupView() {

        subscribeObserver()
        val flexLayoutManager = FlexboxLayoutManager(requireContext(),FlexDirection.ROW,FlexWrap.WRAP)
        flexLayoutManager.justifyContent = JustifyContent.CENTER
        flexLayoutManager.alignItems = AlignItems.CENTER
        interestAdapter = InterestAdapter()
        binding.rcvInterest.apply {
            layoutManager = flexLayoutManager
            itemAnimator = SlideInLeftAnimator()
            adapter = interestAdapter
        }
        interestAdapter.submitList(getListInterest())
        launchRepeatOnLifeCycleWhenResumed {
            sharedViewModel.getSharedFlowInterest()
                .distinctUntilChanged()
                .collectLatest {
                    Log.d(TAG, "onSetupView: $it")
                    if(it.isNotEmpty()){
                        it.forEach { pos->
                            interestAdapter.setItemSelected(pos)
                        }
                    }
                }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(args.isSingleNavigate) binding.ibBack.toVisible()
        binding.ibBack.setOnClickWithAnimationListener {
            Log.d(TAG, "onViewCreated: ${getListInterestSelected()}")
            authAndProfileViewModel.updateByFiledName(args.id,"interests",getListInterestSelected())
        }
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

    private fun getListInterestSelected() = interestAdapter.listIndexSelected.sorted()

    private fun subscribeObserver() {
        launchRepeatOnLifeCycleWhenStarted {
            authAndProfileViewModel.stateFieldName.collect {
                when (it) {
                    is ResourceRemote.Loading -> {
                        loadingDialog.show()
                    }

                    is ResourceRemote.Success -> {
                        loadingDialog.dismiss()
                        pref.saveInterest(getListInterestSelected().joinToString("&"))
                        sharedViewModel.setSharedFlowInterest(getListInterestSelected())
                        showToast("C???p nh???t th??nh c??ng ~")
                        findNavController().popBackStack()
                    }

                    is ResourceRemote.Error -> {
                        showToast("C?? l???i x???y ra ~")
                        findNavController().popBackStack()
                    }
                    else -> {

                    }
                }

            }
        }
    }

    override fun onDestroyView() {
        authAndProfileViewModel.resetStateFieldName()
        super.onDestroyView()
    }
}