package com.devapp.fr.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.devapp.fr.R
import com.devapp.fr.databinding.ActivityConfigProfileBinding
import com.devapp.fr.util.UiHelper.setColorStatusBar
import com.devapp.fr.util.UiHelper.setProgressAnimate
import com.devapp.fr.util.UiHelper.toGone
import com.devapp.fr.util.storages.SharedPreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ConfigProfileActivity : AppCompatActivity() {
    val TAG = "ConfigProfileActivity"
    private lateinit var _binding:ActivityConfigProfileBinding
    private val binding get() =_binding!!
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var gender:String
    @Inject
    lateinit var sharedPrefs:SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setColorStatusBar(R.color.white)
        _binding = ActivityConfigProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navHostFragment = supportFragmentManager.findFragmentById(binding.fragmentContainerView.id) as NavHostFragment
        intent?.run {
            gender = this.getStringExtra("gender").toString()
            Log.d(TAG, "onCreate: $gender")
        }
        binding.btnBack.setOnClickListener {
            if(!navHostFragment.findNavController().popBackStack()) finish()
        }
        navigateToProcess()
    }

    private fun navigateToProcess() {
        val process = sharedPrefs.readProcessRegister()
        when(process){
            0-> {}
            1-> navHostFragment.findNavController().navigate(R.id.fragmentNickName)
            2-> navHostFragment.findNavController().navigate(R.id.fragmentDateOfBirth)
            3-> navHostFragment.findNavController().navigate(R.id.fragmentAddress)
            4-> navHostFragment.findNavController().navigate(R.id.fragmentEmail)
            5-> navHostFragment.findNavController().navigate(R.id.fragmentPassword)
            6-> navHostFragment.findNavController().navigate(R.id.fragmentImage)
            7-> navHostFragment.findNavController().navigate(R.id.fragmentTerms)
            else->{
                startActivity(Intent(this,InformationUserActivity::class.java))
            }
        }
    }

    private val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
        when(destination.id){

            R.id.fragmentProcessConfig->{
                binding.progressBarStep.setProgress(0,true)
            }

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
            else->{
                binding.progressBarStep.toGone()
                binding.btnBack.toGone()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        navHostFragment.findNavController().addOnDestinationChangedListener(listener)
    }

    override fun onPause() {
        super.onPause()
        navHostFragment.findNavController().removeOnDestinationChangedListener(listener)
    }

    override fun onBackPressed() {

    }
}