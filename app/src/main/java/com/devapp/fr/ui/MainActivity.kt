package com.devapp.fr.ui

import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.devapp.fr.R
import com.devapp.fr.databinding.ActivityMainBinding
import nl.joery.animatedbottombar.AnimatedBottomBar

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    lateinit var bottomBar: AnimatedBottomBar
    private lateinit var navHostFragment: NavHostFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Mapping bottomAppbar
        bottomBar = binding.bottomBar

        //Get NavHostFragment
        navHostFragment = supportFragmentManager.findFragmentById(binding.fragmentContainerView.id) as NavHostFragment

        //Set up theme
        setTheme(true)

        //Handle NavHost
        handleNavHostFragment()

    }


    private fun handleNavHostFragment() {
        navHostFragment.navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if(destination.id==R.id.fragmentSplash){
                bottomBar.visibility = View.GONE
            }else{
                bottomBar.visibility = View.VISIBLE
            }
        }
    }

    fun navigateToDestination(destination:Int){
        navHostFragment.navController.navigate(destination)
    }

    private fun setTheme(isDarkMode:Boolean){
        var subColor:Int?=null
        var color:Drawable?=null
        if(isDarkMode){
            subColor = ContextCompat.getColor(this, R.color.sub_text_dark_mode)
            color = ContextCompat.getDrawable(this, R.color.background_dark_mode)
        }
        bottomBar.apply {
            background = color
            if (subColor != null) {
                tabColor = subColor
            }
            tabColorSelected = Color.WHITE
            if (subColor != null) {
                rippleColor = subColor
            }
            indicatorColor = Color.WHITE
        }
        binding.root.background = color
    }
}