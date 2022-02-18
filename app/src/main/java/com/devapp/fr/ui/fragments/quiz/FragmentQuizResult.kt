package com.devapp.fr.ui.fragments.quiz

import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.devapp.fr.R
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.databinding.FragmentQuizResultBinding


class FragmentQuizResult : BaseFragment<FragmentQuizResultBinding> () {

    private var persona : String = ""
    override fun onSetupView() {
        val controller = findNavController()
        val args  : FragmentQuizResultArgs by navArgs()
        persona = args.personality
        setPersona()
        Toast.makeText(requireActivity(), "$persona", Toast.LENGTH_SHORT).show()
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
        else if (persona == "ISTJ-A"|| persona == "ISTJ-T") {
            binding.txPersonalityType.text = "Logistician"
            binding.txPersona.text = persona
            binding.imgPersonality.setAnimation(R.raw.logistician)
            binding.txDescription.text = getString(R.string.logistician)
        }
        else if (persona == "ISFJ-A"|| persona == "ISFJ-T") {
            binding.txPersonalityType.text = "Defender"
            binding.txPersona.text = persona
            binding.imgPersonality.setAnimation(R.raw.defender)
            binding.txDescription.text = getString(R.string.logistician)
        }
        else if (persona == "ESTJ-A"|| persona == "ESTJ-T") {
            binding.txPersonalityType.text = "Executive"
            binding.txPersona.text = persona
            binding.imgPersonality.setAnimation(R.raw.executive)
            binding.txDescription.text = getString(R.string.executive)
        }
        else if (persona == "ESFJ-A"|| persona == "ESFJ-T") {
            binding.txPersonalityType.text = "Consul"
            binding.txPersona.text = persona
            binding.imgPersonality.setAnimation(R.raw.consul)
            binding.txDescription.text = getString(R.string.consul)
        }
        else if (persona == "ISTP-A"|| persona == "ISTP-T") {
            binding.txPersonalityType.text = "Virtuoso"
            binding.txPersona.text = persona
            binding.imgPersonality.setAnimation(R.raw.virtuoso)
            binding.txDescription.text = getString(R.string.virtuoso)
        }
        else if (persona == "ISFP-A"|| persona == "ISFP-T") {
            binding.txPersonalityType.text = "Adventurer"
            binding.txPersona.text = persona
            binding.imgPersonality.setAnimation(R.raw.adventurer)
            binding.txDescription.text = getString(R.string.adventurer)
        }
        else if (persona == "ESTP-A"|| persona == "ESTP-T") {
            binding.txPersonalityType.text = "Entrepreneur  "
            binding.txPersona.text = persona
            binding.imgPersonality.setAnimation(R.raw.entrepreneur)
            binding.txDescription.text = getString(R.string.entrepreneur)
        }
        else if (persona == "ESFP-A"|| persona == "ESFP-T") {
            binding.txPersonalityType.text = "Entertainer"
            binding.txPersona.text = persona
            binding.imgPersonality.setAnimation(R.raw.entertainer)
            binding.txDescription.text = getString(R.string.entertainer)
        }




    }
}