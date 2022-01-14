package com.devapp.fr.ui.fragments.information

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.devapp.fr.R
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.databinding.FragmentUserJobBinding

class FragmentUserJob : BaseFragment<FragmentUserJobBinding>() {
    override fun onSetupView() {
        binding.edtCompany.addTextChangedListener {
            //TO DO TEXT CHANGED
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}