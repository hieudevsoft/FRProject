package com.devapp.fr.ui.fragments.quiz

import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.devapp.fr.R
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.databinding.FragmentQuizResultBinding
import com.devapp.fr.network.ResourceRemote
import com.devapp.fr.ui.fragments.information.FragmentChooseGenderArgs
import com.devapp.fr.ui.viewmodels.AuthAndProfileViewModel
import com.devapp.fr.ui.viewmodels.SharedViewModel
import com.devapp.fr.util.DataHelper.getListPersonality
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenStarted
import com.devapp.fr.util.extensions.showToast
import com.devapp.fr.util.storages.SharedPreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class FragmentQuizResult : BaseFragment<FragmentQuizResultBinding> () {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    @Inject
    lateinit var prefs: SharedPreferencesHelper
    private val authAndProfileViewModel: AuthAndProfileViewModel by activityViewModels()
    private var persona : String = ""
    private var currentPositionChoose = -1
    override fun onSetupView() {
        findNavController()
        val args  : FragmentQuizResultArgs by navArgs()
        persona = args.personality
        setPersona()
        binding.apply {
            btnDone.setOnClickWithAnimationListener {
                currentPositionChoose = getListPersonality().indexOf(txPersonalityType.text.toString().trim())
                authAndProfileViewModel.updateAdditionalInformation(prefs.readIdUserLogin()!!,"personality",currentPositionChoose)
            }

            btnTryAgain.setOnClickWithAnimationListener {
                findNavController().navigate(FragmentQuizResultDirections.actionFragmentQuizResultToFragmentQuiz())
            }
        }
        subscribeObserver()
    }

    private fun subscribeObserver() {
        launchRepeatOnLifeCycleWhenStarted {
            authAndProfileViewModel.stateAdditionalInformation.collect {
                when (it) {
                    is ResourceRemote.Loading -> {
                        loadingDialog.show()
                    }

                    is ResourceRemote.Success -> {
                        loadingDialog.dismiss()
                        sharedViewModel.setSharedPersonality(currentPositionChoose)
                        showToast("Cập nhật thành công ")
                        findNavController().navigate(FragmentQuizResultDirections.actionFragmentQuizResultToFragmentEditProfile())
                    }

                    is ResourceRemote.Error -> {
                        showToast("Có lỗi xảy ra ")
                        findNavController().popBackStack()
                    }
                    else -> {

                    }
                }
            }
        }
    }

    private fun setPersona() {
        if (persona == "INTJ-A" || persona =="INTJ-J") {
            binding.txPersonalityType.text = "Architect"
            binding.txPersona.text = persona
            binding.imgPersonality.setAnimation(R.raw.lottie_engineer)
            binding.txDescription.text = getString(R.string.architect)
        }
        else if (persona == "INTP-A" || persona =="INTP-T") {
            binding.txPersonalityType.text = "Logician"
            binding.txPersona.text = persona
            binding.imgPersonality.setAnimation(R.raw.logician)
            binding.txDescription.text = getString(R.string.logician)
        }
        else if (persona == "ENTJ-A" || persona == "ENTJ-T") {
            binding.txPersonalityType.text = "Commander"
            binding.txPersona.text = persona
            binding.imgPersonality.setAnimation(R.raw.commander)
            binding.txDescription.text = getString(R.string.commander)
        }
        else if (persona == "ENTP-A" || persona == "ENTP-T") {
            binding.txPersonalityType.text = "Debater"
            binding.txPersona.text = persona
            binding.imgPersonality.setAnimation(R.raw.debater)
            binding.txDescription.text = getString(R.string.debater)
        }
        else if (persona == "INFJ-A"|| persona == "INFJ-T") {
            binding.txPersonalityType.text = "Advocate"
            binding.txPersona.text = persona
            binding.imgPersonality.setAnimation(R.raw.advocate)
            binding.txDescription.text = getString(R.string.advocate)
        }
        else if (persona == "INFP-A"|| persona == "INFP-T") {
            binding.txPersonalityType.text = "Mediator"
            binding.txPersona.text = persona
            binding.imgPersonality.setAnimation(R.raw.mediator)
            binding.txDescription.text = getString(R.string.advocate)
        }
        else if (persona == "ENFJ-A"|| persona == "ENFJ-T") {
            binding.txPersonalityType.text = "Protagonist"
            binding.txPersona.text = persona
            binding.imgPersonality.setAnimation(R.raw.protagonist)
            binding.txDescription.text = getString(R.string.protagonist)
        }
        else if (persona == "ENFP-A"|| persona == "ENFP-T") {
            binding.txPersonalityType.text = "Campaigner"
            binding.txPersona.text = persona
            binding.imgPersonality.setAnimation(R.raw.campaigner)
            binding.txDescription.text = getString(R.string.campaigner)
        }
        else if (persona == "IOTJ-A"|| persona == "ISTJ-T") {
            binding.txPersonalityType.text = "Logistician"
            binding.txPersona.text = persona
            binding.imgPersonality.setAnimation(R.raw.logistician)
            binding.txDescription.text = getString(R.string.logistician)
        }
        else if (persona == "IOFJ-A"|| persona == "IOFJ-T") {
            binding.txPersonalityType.text = "Defender"
            binding.txPersona.text = persona
            binding.imgPersonality.setAnimation(R.raw.defender)
            binding.txDescription.text = getString(R.string.logistician)
        }
        else if (persona == "EOTJ-A"|| persona == "EOTJ-T") {
            binding.txPersonalityType.text = "Executive"
            binding.txPersona.text = persona
            binding.imgPersonality.setAnimation(R.raw.executive)
            binding.txDescription.text = getString(R.string.executive)
        }
        else if (persona == "EOFJ-A"|| persona == "EOFJ-T") {
            binding.txPersonalityType.text = "Consul"
            binding.txPersona.text = persona
            binding.imgPersonality.setAnimation(R.raw.consul)
            binding.txDescription.text = getString(R.string.consul)
        }
        else if (persona == "IOTP-A"|| persona == "IOTP-T") {
            binding.txPersonalityType.text = "Virtuoso"
            binding.txPersona.text = persona
            binding.imgPersonality.setAnimation(R.raw.virtuoso)
            binding.txDescription.text = getString(R.string.virtuoso)
        }
        else if (persona == "IOFP-A"|| persona == "IOFP-T") {
            binding.txPersonalityType.text = "Adventurer"
            binding.txPersona.text = persona
            binding.imgPersonality.setAnimation(R.raw.adventurer)
            binding.txDescription.text = getString(R.string.adventurer)
        }
        else if (persona == "EOTP-A"|| persona == "EOTP-T") {
            binding.txPersonalityType.text = "Entrepreneur  "
            binding.txPersona.text = persona
            binding.imgPersonality.setAnimation(R.raw.entrepreneur)
            binding.txDescription.text = getString(R.string.entrepreneur)
        }
        else if (persona == "EOFP-A"|| persona == "EOFP-T") {
            binding.txPersonalityType.text = "Entertainer"
            binding.txPersona.text = persona
            binding.imgPersonality.setAnimation(R.raw.entertainer)
            binding.txDescription.text = getString(R.string.entertainer)
        }
    }
}