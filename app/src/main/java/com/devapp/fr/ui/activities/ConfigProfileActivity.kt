package com.devapp.fr.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.devapp.fr.R
import com.devapp.fr.databinding.ActivityConfigProfileBinding
import com.devapp.fr.util.UiHelper.setProgressAnimate
import com.devapp.fr.util.storages.SharedPreferencesHelper

class ConfigProfileActivity : AppCompatActivity() {
    val TAG = "ConfigProfileActivity"
    private lateinit var _binding:ActivityConfigProfileBinding
    private val binding get() =_binding!!
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var gender:String
    lateinit var sharedPrefs:SharedPreferencesHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityConfigProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navHostFragment = supportFragmentManager.findFragmentById(binding.fragmentContainerView.id) as NavHostFragment
        intent?.run {
            gender = this.getStringExtra("gender").toString()
            Log.d(TAG, "onCreate: $gender")
        }
        sharedPrefs = SharedPreferencesHelper(this)
        binding.btnBack.setOnClickListener {
            if(!navHostFragment.findNavController().popBackStack()) finish()
        }
    }

    private val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
        when(destination.id){
            R.id.fragmentNickName->{
                binding.progressBarStep.setProgress(20,true)
            }
            R.id.fragmentDateOfBirth->{
                binding.progressBarStep.setProgress(40,true)
            }
            R.id.fragmentAddress->{
                binding.progressBarStep.setProgress(60,true)
            }
            R.id.fragmentEmail->{
                binding.progressBarStep.setProgress(80,true)
            }
            R.id.fragmentPassword->{
                binding.progressBarStep.setProgress(100,true)
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        navHostFragment.findNavController().addOnDestinationChangedListener(listener)
    }

    override fun onPause() {
        super.onPause()
        navHostFragment.findNavController().removeOnDestinationChangedListener(listener)
    }
}