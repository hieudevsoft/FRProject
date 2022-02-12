package com.devapp.fr.ui.fragments.configProfile

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.devapp.fr.R
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.data.entities.UserProfile
import com.devapp.fr.databinding.FragmentTermsBinding
import com.devapp.fr.ui.activities.InformationUserActivity
import com.devapp.fr.util.UiHelper.enableOrNot
import com.devapp.fr.util.animations.AnimationHelper.startAnimClick
import com.devapp.fr.util.storages.SharedPreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Clock
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class FragmentTerms : BaseFragment<FragmentTermsBinding>() {
    val TAG = "FragmentTerms"
    val TIME_READ = 10000L
    @Inject
    lateinit var prefs:SharedPreferencesHelper

    override fun onSetupView() {
        var startTime = System.currentTimeMillis()
        binding.apply {
            btnJoin.setOnClickListener {
                it.startAnimClick()
                Log.d(TAG, "onSetupView: ${System.currentTimeMillis()-startTime}")
                if(System.currentTimeMillis()-startTime<=TIME_READ){
                    Toast.makeText(requireContext(), "Lừa à đọc đi đã ~", Toast.LENGTH_SHORT).show()
                    it.enableOrNot(false)
                    startTime = System.currentTimeMillis()
                    Handler(Looper.getMainLooper()).postDelayed({
                        it.enableOrNot(true)
                    },TIME_READ)
                }else{
                    prefs.saveProcessRegister(8)
                    requireActivity().finish()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }



}