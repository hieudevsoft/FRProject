package com.devapp.fr.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.fragment.NavHostFragment
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
    }

    private fun setTheme(isDarkMode:Boolean){
        if(isDarkMode){
            val subColor = ContextCompat.getColor(this, R.color.sub_text_dark_mode)
            val color = ContextCompat.getDrawable(this, R.color.background_dark_mode)
            bottomBar.apply {
                background = color
                tabColor = subColor
                tabColorSelected = Color.WHITE
                rippleColor = subColor
                indicatorColor = Color.WHITE
            }
        }
    }
}