package com.devapp.fr.ui.activities

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
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
    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper

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
        setColorStatusBar(getColor(R.color.white))
        //Mapping bottomAppbar
        bottomBar = binding.bottomBar
        bottomBar.onTabSelected={
            when {
                it.title.contains("setting",true) -> navHostFragment.findNavController().navigate(R.id.fragmentSettings)
                it.title.contains("chats",true) -> navHostFragment.findNavController().navigate(R.id.fragmentChats)
                else -> navHostFragment.findNavController().navigate(R.id.fragmentLoves)
            }
        }
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

    }

    override fun onResume() {
        val isLogin = sharedPreferencesHelper.readIsLogin()
        if(isLogin) navHostFragment.findNavController().navigate(R.id.fragmentProfile)
        super.onResume()
    }

    override fun syncTheme(appTheme: AppTheme) {}
    private fun handleNavHostFragment() {
        navHostFragment.navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == R.id.fragmentSplash||destination.id==R.id.fragmentInbox
                ||destination.id == R.id.fragmentLogin||destination.id==R.id.fragmentProfile
            ) {
                bottomBar.visibility = View.GONE
            } else {
                bottomBar.visibility = View.VISIBLE
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

    override fun onBackPressed() {

    }
}