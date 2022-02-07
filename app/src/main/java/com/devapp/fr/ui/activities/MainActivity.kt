package com.devapp.fr.ui.activities

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.devapp.fr.R
import com.devapp.fr.app.DarkTheme
import com.devapp.fr.app.LightTheme
import com.devapp.fr.databinding.ActivityMainBinding
import com.devapp.fr.util.UiHelper.setColorStatusBar
import com.devapp.fr.util.storages.DataStoreHelper
import com.devapp.fr.util.storages.SharedPreferencesHelper
import com.devapp.fr.util.storages.dataStore
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.ThemeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import nl.joery.animatedbottombar.AnimatedBottomBar
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ThemeActivity() {
    val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    lateinit var bottomBar: AnimatedBottomBar
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun getStartTheme(): AppTheme {
        //Init SharedPreferencesHelper
        sharedPreferencesHelper= SharedPreferencesHelper(applicationContext)
        return if(sharedPreferencesHelper.readDarkMode()) DarkTheme()
        else LightTheme()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set insets
        setInsetsWindow()

        //Mapping bottomAppbar
        bottomBar = binding.bottomBar

        //Get NavHostFragment
        navHostFragment =
            supportFragmentManager.findFragmentById(binding.fragmentContainerView.id) as NavHostFragment

        //Set up theme
        lifecycleScope.launchWhenStarted {
            DataStoreHelper.getInstance().getDarkMode(this@MainActivity.dataStore).collectLatest {
                setTheme(it)
            }
        }

        //Handle NavHost
        handleNavHostFragment()

        //save state login
        saveStateLogin()
    }

    private fun setInsetsWindow() {
        WindowCompat.setDecorFitsSystemWindows(window,false)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root){
                view,windowInsets->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = insets.left
                bottomMargin = insets.bottom
                rightMargin = insets.right
            }

            WindowInsetsCompat.CONSUMED
        }
    }

    private fun saveStateLogin() {
        val isLogin = sharedPreferencesHelper.readIsLogin()
        if(isLogin) navHostFragment.findNavController().navigate(R.id.fragmentMainViewPager)
    }


    override fun syncTheme(appTheme: AppTheme) {}
    private fun handleNavHostFragment() {
        navHostFragment.navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id==R.id.fragmentSettings||destination.id==R.id.fragmentChats||destination.id==R.id.fragmentLoves 
            ) {
                bottomBar.visibility = View.VISIBLE
            } else {
                binding.root.fitsSystemWindows = false
                bottomBar.visibility = View.GONE
            }
        }
    }

    private fun setTheme(isDarkMode: Boolean) {
        var subColor: Int? = null
        var color: Drawable? = null
        var tabSelectedColor: Int? = null
        var indColor: Int? = null
        if (isDarkMode) {
            tabSelectedColor = Color.WHITE
            indColor = Color.WHITE
            subColor = ContextCompat.getColor(this, R.color.sub_text_dark_mode)
            color = ContextCompat.getDrawable(this, R.color.background_dark_mode)
        } else {
            tabSelectedColor = Color.BLACK
            indColor = Color.BLACK
            subColor = ContextCompat.getColor(this, R.color.sub_text_light_mode)
            color = ContextCompat.getDrawable(this, R.color.background_light_mode)
        }
        bottomBar.apply {
            background = color
            tabColor = subColor
            tabColorSelected = tabSelectedColor
            rippleColor = subColor
            indicatorColor = indColor
        }
        binding.root.background = color
    }

}