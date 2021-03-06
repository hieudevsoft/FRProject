package com.devapp.fr.ui.fragments.configProfile

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.devapp.fr.R
import com.devapp.fr.adapters.InformationAdapter
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.data.entities.UserProfile
import com.devapp.fr.data.models.items.InformationItem
import com.devapp.fr.databinding.FragmentEditProfileBinding
import com.devapp.fr.ui.fragments.quiz.FragmentQuiz
import com.devapp.fr.ui.fragments.quiz.FragmentQuizDirections
import com.devapp.fr.ui.viewmodels.SharedViewModel
import com.devapp.fr.ui.widgets.SexualityBottomDialogFragment
import com.devapp.fr.util.DataHelper
import com.devapp.fr.util.DataHelper.getListCertificate
import com.devapp.fr.util.DataHelper.getListChild
import com.devapp.fr.util.DataHelper.getListDrink
import com.devapp.fr.util.DataHelper.getListGender
import com.devapp.fr.util.DataHelper.getListItemInformation
import com.devapp.fr.util.DataHelper.getListMaritalStatus
import com.devapp.fr.util.DataHelper.getListPet
import com.devapp.fr.util.DataHelper.getListReligion
import com.devapp.fr.util.DataHelper.getListSexuality
import com.devapp.fr.util.DataHelper.getListSmoke
import com.devapp.fr.util.UiHelper.toGone
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import com.devapp.fr.util.extensions.composeElementsSameClickEvent
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import java.util.*

@AndroidEntryPoint
class FragmentEditProfile(private val userProfile:UserProfile?=null): BaseFragment<FragmentEditProfileBinding>() {
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
    private var currentPosQuiz = -1
    private val args:FragmentEditProfileArgs by navArgs()
    override fun onSetupView() {
        setupRecyclerViewInformation()
        if(userProfile!=null){
            informationAdapter.submitList(getListItemInformation())
            binding.apply {
                tvEditProfile.text = "Th??ng tin chi ti???t"
                root.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.background_dark_mode))
                appBarLayout.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.background_dark_mode))
                tvEditProfile.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                ViewCompat.setBackgroundTintList(btnBack, ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.white)))
                val age = Calendar.getInstance().get(Calendar.YEAR)-userProfile.dob.split("/").last().toInt()+1
                tvUsernameAge.text = "${userProfile.name}, $age"
                val gender = if(userProfile.gender==0) "N???" else "Nam"
                tvGenderAddress.text = "${gender}, ${userProfile.address}"
                binding.tvDetailJob.apply {
                    text = userProfile.job.ifEmpty { "Th??m c??ng vi???c v?? h???c v???n" }
                    setTextColor(ContextCompat.getColor(requireContext(), if(userProfile.job.isNotEmpty()) R.color.background_dark_mode else R.color.color_blue_500)  )
                }
                userProfile.purpose.also {
                    if(it==-1){
                        binding.tvDesPurpose.text = "Cho ch??ng t??i bi???t th??m v??? b???n..."
                    } else binding.tvDesPurpose.text = getListSexuality()[it].text
                    binding.tvDesPurpose.apply {
                        setTextColor(ContextCompat.getColor(requireContext(), if(it!=-1) R.color.background_dark_mode else R.color.color_blue_500)  )
                    }
                }
                lyHobby.toGone()
                userProfile.bio.also {
                    binding.tvDesYourself.apply {
                        text = it.ifEmpty { "Cho ch??ng t??i bi???t th??m v??? b???n..." }
                        setTextColor(ContextCompat.getColor(requireContext(), if(it.isNotEmpty()) R.color.background_dark_mode else R.color.color_blue_500)  )
                    }
                }
                userProfile.additionInformation?.let {
                    resetAdapterViewProfile(0,if(it.tall==-1) "" else it.tall.toString())
                    resetAdapterViewProfile(1,if(it.child==-1) "" else getListChild()[it.child].text)
                    resetAdapterViewProfile(2,if(it.drink==-1) "" else getListDrink()[it.drink].text)
                    resetAdapterViewProfile(3,if(it.maritalStatus==-1) "" else getListMaritalStatus()[it.maritalStatus].text)
                    resetAdapterViewProfile(4,if(it.trueGender==-1) "" else getListGender()[it.trueGender].text)
                    resetAdapterViewProfile(5,if(it.smoking==-1) "" else getListSmoke()[it.smoking].text)
                    resetAdapterViewProfile(6,if(it.pet==-1) "" else getListPet()[it.pet].text)
                    resetAdapterViewProfile(7,if(it.religion==-1) "" else getListReligion()[it.religion].text)
                    resetAdapterViewProfile(8,if(it.certificate==-1) "" else getListCertificate()[it.certificate].text)
                }
            }

        } else{
            handleEventElement()
            subscriberObserver()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            btnBack.setOnClickWithAnimationListener {
                if(userProfile==null)
                findNavController().popBackStack() else activity?.supportFragmentManager?.popBackStack() }
            nestedScrollView.setOnScrollChangeListener {
                    _, _, scrollY, _, _ ->
                sharedViewModel.currentNestedScrollPosition = scrollY
            }
        }

        super.onViewCreated(view, savedInstanceState)
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
                        val gender = if((it[3] as Int)==0) "N???" else "Nam"
                        tvGenderAddress.text = "${gender}, ${it[2] as String}"
                    }
                }
        }

        launchRepeatOnLifeCycleWhenStarted {
            sharedViewModel.getSharedFlowTall()
                .distinctUntilChanged()
                .collectLatest {
                    var trueData = it
                    if(trueData>1000) trueData -= 1001
                    val data = "${
                        when (trueData) {
                            91 -> "<91"
                            220 -> ">220"
                            else -> trueData.toString()
                        }
                    } cm"
                    sharedViewModel.getPositionInformation().value?.let { pos ->
                        if(pos==0 || it>1000) {
                            resetAdapter(0,if(trueData!=-1) data else "")
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
                    if(currentPosChild>=1000) currentPosChild-=1001
                    Log.d(TAG, "subscriberObserver: $it")
                    if(currentPosChild!=-1){
                        val listChild = DataHelper.getListChild()
                        val data = listChild[currentPosChild].text
                        sharedViewModel.getPositionInformation().value?.let { pos ->
                            if(pos==1||it>=1000){
                                resetAdapter(1,if(currentPosChild==listChild.size-1) "" else data)
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
                    if(currentPosDrink>=1000) currentPosDrink-=1001
                    if(currentPosDrink!=-1){
                        val listDrink = DataHelper.getListDrink()
                        val data = listDrink[currentPosDrink].text
                        sharedViewModel.getPositionInformation().value?.let { pos ->
                            if(pos==2||it>=1000){
                                resetAdapter(2,if(currentPosDrink==listDrink.size-1) "" else data)
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
                    if(currentPosMaritalStatus>=1000) currentPosMaritalStatus-=1001
                    if(currentPosMaritalStatus!=-1){
                        val listMaritalStatus = DataHelper.getListMaritalStatus()
                        val data = listMaritalStatus[currentPosMaritalStatus].text
                        sharedViewModel.getPositionInformation().value?.let { pos ->
                            if(pos == 3||it>=1000){
                                resetAdapter(3,if(currentPosMaritalStatus==listMaritalStatus.size-1) "" else data)
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
                    if(currentChooseGender>=1000) currentChooseGender-=1001
                    if(currentChooseGender!=-1){
                        val listChooseGender = DataHelper.getListGender()
                        val data = listChooseGender[currentChooseGender].text
                        sharedViewModel.getPositionInformation().value?.let { pos ->
                            if(pos == 4||it>=1000){
                                resetAdapter(4,if(currentChooseGender==listChooseGender.size-1) "" else data)
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
                    if(currentPosSmoke>=1000) currentPosSmoke-=1001
                    if(currentPosSmoke!=-1){
                        val listSmoke = DataHelper.getListSmoke()
                        val data = listSmoke[currentPosSmoke].text
                        sharedViewModel.getPositionInformation().value?.let { pos ->
                            if(pos == 5||it>=1000){
                                resetAdapter(5,if(currentPosSmoke==listSmoke.size-1) "" else data)
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
                    currentPosPet = it
                    if(currentPosPet>=1000) currentPosPet-=1001
                    if(currentPosPet!=-1){
                        val listPet = DataHelper.getListPet()
                        val data = listPet[currentPosPet].text
                        sharedViewModel.getPositionInformation().value?.let { pos ->
                            if(pos == 6||it>=1000){
                                resetAdapter(6,if(currentPosPet==listPet.size-1) "" else data)
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
                    if(currentPosReligion>=1000) currentPosReligion-=1001
                    if(currentPosReligion!=-1){
                        val listReligion = DataHelper.getListReligion()
                        val data = listReligion[currentPosReligion].text
                        sharedViewModel.getPositionInformation().value?.let { pos ->
                            if(pos == 7||it>=1000){
                                resetAdapter(7,if(currentPosReligion==listReligion.size-1) "" else data)
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
                    if(currentCertificate>=1000) currentCertificate-=1001
                    if(currentCertificate!=-1){
                        val listCertificate = DataHelper.getListCertificate()
                        val data = listCertificate[currentCertificate].text
                        sharedViewModel.getPositionInformation().value?.let { pos ->
                            if(pos == 8||it>=1000){
                                resetAdapter(8,if(currentCertificate==listCertificate.size-1) "" else data)
                                sharedViewModel.setListItemInformation(listTemp)
                            }
                        }
                    }
                }
        }

        launchRepeatOnLifeCycleWhenStarted {
            sharedViewModel.getSharedPersonality()
                .distinctUntilChanged()
                .collectLatest {
                    if(it!=-1){
                        val listPersonality = DataHelper.getListPersonality()
                        val data = listPersonality[it]
                        resetAdapter(9,if(it==listPersonality.size-1) "" else data)
                        sharedViewModel.setListItemInformation(listTemp)
                    }
                }
        }

        launchRepeatOnLifeCycleWhenStarted {
            sharedViewModel.getSharedFlowIntroduce()
                .distinctUntilChanged()
                .collectLatest {
                    binding.tvDesYourself.apply {
                        text = it.ifEmpty { "Cho ch??ng t??i bi???t th??m v??? b???n..." }
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
                        text = it.joinToString(", ").ifEmpty { "Cho ch??ng t??i bi???t th??m v??? b???n..." }
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
                        binding.tvDesPurpose.text = "Cho ch??ng t??i bi???t th??m v??? b???n..."
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
                        text = it.ifEmpty { "Th??m c??ng vi???c v?? h???c v???n" }
                        setTextColor(ContextCompat.getColor(requireContext(), if(it.isNotEmpty()) R.color.background_dark_mode else R.color.color_blue_500)  )
                    }

                }
        }
    }
    private fun resetAdapter(pos:Int, data:String){
        listTemp = sharedViewModel.getListItemInformation().value!!.toMutableList()
        listTemp[pos].content = data
    }

    private fun resetAdapterViewProfile(pos:Int, data:String){
        listTemp = informationAdapter.differ.currentList
        listTemp[pos].content = data
        informationAdapter.submitList(listTemp)
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

                9-> {
                    sharedViewModel.setPositionInformation(9)
                    findNavController().navigate(FragmentEditProfileDirections.actionFragmentEditProfileToFragmentQuiz())
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