package com.devapp.fr.ui.fragments.configProfile

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.devapp.fr.R
import com.devapp.fr.adapters.InformationAdapter
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.data.models.items.InformationItem
import com.devapp.fr.databinding.FragmentEditProfileBinding
import com.devapp.fr.ui.viewmodels.SharedViewModel
import com.devapp.fr.ui.widgets.SexualityBottomDialogFragment
import com.devapp.fr.util.DataHelper
import com.devapp.fr.util.DataHelper.getListItemInformation
import com.devapp.fr.util.DataHelper.getListSexuality
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import com.devapp.fr.util.extensions.composeElementsSameClickEvent
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import java.util.*

@AndroidEntryPoint
class FragmentEditProfile : BaseFragment<FragmentEditProfileBinding>() {
    val TAG = "FragmentEditProfile"
    private lateinit var informationAdapter: InformationAdapter
    private val sharedViewModel: SharedViewModel by activityViewModels()
    var listTemp = mutableListOf<InformationItem>()
    private var currentPosChild = -1
    private var currentPosDrink = -1
    private var currentPosMaritalStatus = -1
    private var currentChooseGender = -1
    private var currentPosSmoke = -1
    private var currentPosPet = -1
    private var currentCertificate = -1
    private var currentPosReligion = -1
    private val args:FragmentEditProfileArgs by navArgs()
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
            sharedViewModel.getSharedFlowBasicInformation()
                .distinctUntilChanged()
                .collectLatest {
                    binding.apply {
                        val age = Calendar.getInstance().get(Calendar.YEAR)-(it[1] as String).split("/").last().toInt()+1
                        tvUsernameAge.text = "${it[0] as String}, $age"
                        val gender = if((it[3] as Int)==0) "Nữ" else "Nam"
                        tvGenderAddress.text = "${gender}, ${it[2] as String}"
                    }
                }
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
                    currentPosChild = it
                    Log.d(TAG, "subscriberObserver: $it")
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
                    currentPosDrink = it
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

        launchRepeatOnLifeCycleWhenStarted {
            sharedViewModel.getSharedFlowMaritalStatus()
                .distinctUntilChanged()
                .collectLatest {
                    currentPosMaritalStatus = it
                    if(it!=-1){
                        val listMaritalStatus = DataHelper.getListMaritalStatus()
                        val data = listMaritalStatus[it].text
                        sharedViewModel.getPositionInformation().value?.let { pos ->
                            if(pos == 3){
                                resetAdapter(pos,if(it==listMaritalStatus.size-1) "" else data)
                                sharedViewModel.setListItemInformation(listTemp)
                            }
                        }
                    }
                }
        }

        launchRepeatOnLifeCycleWhenStarted {
            sharedViewModel.getSharedFlowChooseGender()
                .distinctUntilChanged()
                .collectLatest {
                    currentChooseGender = it
                    if(it!=-1){
                        val listChooseGender = DataHelper.getListGender()
                        val data = listChooseGender[it].text
                        sharedViewModel.getPositionInformation().value?.let { pos ->
                            if(pos == 4){
                                resetAdapter(pos,if(it==listChooseGender.size-1) "" else data)
                                sharedViewModel.setListItemInformation(listTemp)
                            }
                        }
                    }
                }
        }

        launchRepeatOnLifeCycleWhenStarted {
            sharedViewModel.getSharedFlowSmoke()
                .distinctUntilChanged()
                .collectLatest {
                    currentPosSmoke = it
                    if(it!=-1){
                        val listSmoke = DataHelper.getListSmoke()
                        val data = listSmoke[it].text
                        sharedViewModel.getPositionInformation().value?.let { pos ->
                            if(pos == 5){
                                resetAdapter(pos,if(it==listSmoke.size-1) "" else data)
                                sharedViewModel.setListItemInformation(listTemp)
                            }
                        }
                    }
                }
        }

        launchRepeatOnLifeCycleWhenStarted {
            sharedViewModel.getSharedFlowPet()
                .distinctUntilChanged()
                .collectLatest {
                    currentPosPet = -1
                    if(it!=-1){
                        val listPet = DataHelper.getListPet()
                        val data = listPet[it].text
                        sharedViewModel.getPositionInformation().value?.let { pos ->
                            if(pos == 6){
                                resetAdapter(pos,if(it==listPet.size-1) "" else data)
                                sharedViewModel.setListItemInformation(listTemp)
                            }
                        }
                    }
                }
        }

        launchRepeatOnLifeCycleWhenStarted {
            sharedViewModel.getSharedFlowReligion()
                .distinctUntilChanged()
                .collectLatest {
                    currentPosReligion = it
                    if(it!=-1){
                        val listReligion = DataHelper.getListReligion()
                        val data = listReligion[it].text
                        sharedViewModel.getPositionInformation().value?.let { pos ->
                            if(pos == 7){
                                resetAdapter(pos,if(it==listReligion.size-1) "" else data)
                                sharedViewModel.setListItemInformation(listTemp)
                            }
                        }
                    }
                }
        }

        launchRepeatOnLifeCycleWhenStarted {
            sharedViewModel.getSharedFlowCertificate()
                .distinctUntilChanged()
                .collectLatest {
                    currentCertificate = it
                    if(it!=-1){
                        val listCertificate = DataHelper.getListCertificate()
                        val data = listCertificate[it].text
                        sharedViewModel.getPositionInformation().value?.let { pos ->
                            if(pos == 8){
                                resetAdapter(pos,if(it==listCertificate.size-1) "" else data)
                                sharedViewModel.setListItemInformation(listTemp)
                            }
                        }
                    }
                }
        }

        launchRepeatOnLifeCycleWhenStarted {
            sharedViewModel.getSharedFlowIntroduce()
                .distinctUntilChanged()
                .collectLatest {
                    binding.tvDesYourself.apply {
                        text = it.ifEmpty { "Cho chúng tôi biết thêm về bạn..." }
                        setTextColor(ContextCompat.getColor(requireContext(), if(it.isNotEmpty()) R.color.background_dark_mode else R.color.color_blue_500)  )
                    }

                }
        }

        launchRepeatOnLifeCycleWhenStarted {
            val listInterest = resources.getStringArray(R.array.hobby)
            sharedViewModel.getSharedFlowInterest()
                .distinctUntilChanged()
                .map { it.map { "#"+listInterest[it] } }
                .collectLatest {
                    binding.tvDetailHobby.apply {
                        text = it.joinToString(", ").ifEmpty { "Cho chúng tôi biết thêm về bạn..." }
                        setTextColor(ContextCompat.getColor(requireContext(), if(it.isNotEmpty()) R.color.background_dark_mode else R.color.color_blue_500)  )
                    }
                }
        }

        launchRepeatOnLifeCycleWhenStarted {
            sharedViewModel.getSharedFlowSexuality()
                .distinctUntilChanged()
                .collectLatest {
                    Log.d(TAG, "subscriberObserver: $it")
                    if(it==-1){
                        binding.tvDesPurpose.text = "Cho chúng tôi biết thêm về bạn..."
                    } else binding.tvDesPurpose.text = getListSexuality()[it].text
                    binding.tvDesPurpose.apply {
                        setTextColor(ContextCompat.getColor(requireContext(), if(it!=-1) R.color.background_dark_mode else R.color.color_blue_500)  )
                    }

                }
        }

        launchRepeatOnLifeCycleWhenStarted {
            sharedViewModel.getSharedFlowJob()
                .distinctUntilChanged()
                .collectLatest {
                    binding.tvDetailJob.apply {
                        text = it.ifEmpty { "Thêm công việc và học vấn" }
                        setTextColor(ContextCompat.getColor(requireContext(), if(it.isNotEmpty()) R.color.background_dark_mode else R.color.color_blue_500)  )
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
                    findNavController().navigate(FragmentEditProfileDirections.actionFragmentEditProfileToFragmentChild(true,currentPosChild))
                }

                2-> {
                    sharedViewModel.setPositionInformation(2)
                    findNavController().navigate(FragmentEditProfileDirections.actionFragmentEditProfileToFragmentDrink(true,currentPosDrink))
                }

                3-> {
                    sharedViewModel.setPositionInformation(3)
                    findNavController().navigate(FragmentEditProfileDirections.actionFragmentEditProfileToFragmentMaritalStatus(true,currentPosMaritalStatus))
                }

                4-> {
                    sharedViewModel.setPositionInformation(4)
                    findNavController().navigate(FragmentEditProfileDirections.actionFragmentEditProfileToFragmentChooseGender(true,currentChooseGender))
                }

                5-> {
                    sharedViewModel.setPositionInformation(5)
                    findNavController().navigate(FragmentEditProfileDirections.actionFragmentEditProfileToFragmentSmoke(true,currentPosSmoke))
                }

                6-> {
                    sharedViewModel.setPositionInformation(6)
                    findNavController().navigate(FragmentEditProfileDirections.actionFragmentEditProfileToFragmentPet(true,currentPosPet))
                }

                7-> {
                    sharedViewModel.setPositionInformation(7)
                    findNavController().navigate(FragmentEditProfileDirections.actionFragmentEditProfileToFragmentReligion(true,currentPosReligion))
                }

                8-> {
                    sharedViewModel.setPositionInformation(8)
                    findNavController().navigate(FragmentEditProfileDirections.actionFragmentEditProfileToFragmentCertificate(true,currentCertificate))
                }
            }
        }
        binding.apply {
            composeElementsSameClickEvent(icNext4,lyHobby,tvDesHobby){openFragmentHobby()}
            composeElementsSameClickEvent(icNext5,lyDescribeYourself, tvDescribeYourself){openFragmentIntroduce()}
            composeElementsSameClickEvent(icNext3,lySexuality,tvDesPurpose){openBottomSexuality()}
            composeElementsSameClickEvent(icNext,lyJob,tvDetailJob){openFragmentJob()}
            composeElementsSameClickEvent(icNextUserBasicInformation,lyBasicInformation){openFragmentBasicInformation()}
        }
    }

    private fun openBottomSexuality() {
        SexualityBottomDialogFragment().show(childFragmentManager,args.id)
    }

    private fun openFragmentBasicInformation() {
        findNavController().navigate(FragmentEditProfileDirections.actionFragmentEditProfileToFragmentBasicInformation(args.id))
    }

    private fun openFragmentJob() {
        findNavController().navigate(FragmentEditProfileDirections.actionFragmentEditProfileToFragmentUserJob(isSingleNavigate = true,id=args.id))
    }

    private fun openFragmentHobby() {
        findNavController().navigate(FragmentEditProfileDirections.actionFragmentEditProfileToFragmentInterest(isSingleNavigate = true,id=args.id))
    }

    private fun openFragmentIntroduce() {
        findNavController().navigate(FragmentEditProfileDirections.actionFragmentEditProfileToFragmentIntroduce(isSingleNavigate = true,id=args.id))
    }

    private fun setupRecyclerViewInformation() {
        informationAdapter = InformationAdapter()
        binding.rcInformation.adapter = informationAdapter
        binding.rcInformation.isNestedScrollingEnabled = false
        sharedViewModel.setListItemInformation(getListItemInformation())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            btnBack.setOnClickWithAnimationListener { findNavController().popBackStack() }
            nestedScrollView.setOnScrollChangeListener {
                    _, _, scrollY, _, _ ->
                sharedViewModel.currentNestedScrollPosition = scrollY
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {

        binding.nestedScrollView.apply {
            isSmoothScrollingEnabled = true
            startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)
            smoothScrollTo(0,sharedViewModel.currentNestedScrollPosition)
        }
        super.onResume()
    }
}