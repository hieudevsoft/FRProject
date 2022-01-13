package com.devapp.fr.ui.fragments.information

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.devapp.fr.R
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.databinding.FragmentIntroduceBinding

class FragmentIntroduce : BaseFragment<FragmentIntroduceBinding>() {
    override fun onSetupView() {
        binding.edtIntroduce.addTextChangedListener {
            val numberOfCharacter = it.toString().length
            binding.tvNumberCharacter.text = "${500-numberOfCharacter} ký tự"
            if(numberOfCharacter>500){
                binding.edtIntroduce.setText(it.toString())
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}