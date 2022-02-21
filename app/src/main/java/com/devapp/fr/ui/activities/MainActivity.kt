package com.devapp.fr.ui.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.devapp.fr.R
import com.devapp.fr.app.DarkTheme
import com.devapp.fr.app.LightTheme
import com.devapp.fr.data.entities.UserProfile
import com.devapp.fr.data.models.items.AccountOnline
import com.devapp.fr.databinding.ActivityMainBinding
import com.devapp.fr.network.NotificationService
import com.devapp.fr.network.ResourceRemote
import com.devapp.fr.ui.viewmodels.AuthAndProfileViewModel
import com.devapp.fr.ui.viewmodels.RealTimeViewModel
import com.devapp.fr.ui.viewmodels.SharedViewModel
import com.devapp.fr.util.Constants
import com.devapp.fr.util.DataHelper
import com.devapp.fr.util.UiHelper
import com.devapp.fr.util.UiHelper.toGone
import com.devapp.fr.util.UiHelper.toVisible
import com.devapp.fr.util.extensions.showToast
import com.devapp.fr.util.storages.DataStoreHelper
import com.devapp.fr.util.storages.SharedPreferencesHelper
import com.devapp.fr.util.storages.dataStore
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.ThemeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
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
    private val authViewModel: AuthAndProfileViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val realTimeViewModel: RealTimeViewModel by viewModels()
    private var user: UserProfile? = null
    var listName: MutableList<String> = mutableListOf()
    var listUri: MutableList<String> = mutableListOf()
    fun getUser() = user

    @Inject
    lateinit var prefs: SharedPreferencesHelper
    override fun getStartTheme(): AppTheme {
        //Init SharedPreferencesHelper
        sharedPreferencesHelper = SharedPreferencesHelper(applicationContext)
        return if (sharedPreferencesHelper.readDarkMode()) DarkTheme()
        else LightTheme()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //set insets
        //setInsetsWindow()

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

        //get userprofile
        prefs.readIdUserLogin()?.let {
            if (intent.getBooleanExtra("navigate_chat", false))
                sharedViewModel.setPositionMainViewPager(1)
            startService(
                Intent(this, NotificationService::class.java).also {
                    it.putExtra("navigate_chat", intent.getBooleanExtra("navigate_chat", false))
                }
            )
            getUserProfile(it)
            realTimeViewModel.readNotificationCallVideo(it) {
                it?.let { string ->
                    if (it.contains("null") || it.contains("##") || it.split("#")[0].isEmpty()) return@readNotificationCallVideo
                    UiHelper.triggerBottomAlertDialog(
                        this,
                        "Cuộc gọi",
                        "${string.split("#")[1]} muốn gọi cho bạn ~",
                        "Có",
                        "Không",
                        false,
                        { dialogInterface, _ ->
                            subscriberNotificationCallVideo(string)
                            dialogInterface.dismiss()
                        }
                    ) {
                        realTimeViewModel.resetStateFlowNotificationCallVideo()
                        realTimeViewModel.sendNotificationCallVideo(
                            prefs.readIdUserLogin()!!,
                            "",
                            "",
                            ""
                        )
                    }
                }
            }
        }

        //subscriber observer
        subscribeObserver()

    }


    private fun subscriberNotificationCallVideo(string: String) {
        realTimeViewModel.sendNotificationCallVideo(
            prefs.readIdUserLogin()!!,
            "",
            "",
            string.split("#")[2]
        )
        lifecycle.coroutineScope.launchWhenResumed {
            realTimeViewModel.stateFlowNotificationCallVideo.collect {
                it?.let {
                    startActivity(Intent(this@MainActivity, VideoCallActivity::class.java).also {
                        it.putExtra("roomId", string.split("#")[0])
                        it.putExtra("partnerId", prefs.readIdUserLogin()!!)
                    })
                }
            }
        }
    }


    private fun getUserProfile(id: String) {
        authViewModel.getUserProfile(id)
    }

    private fun subscribeObserver() {
        lifecycle.coroutineScope.launchWhenCreated {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                authViewModel.stateGetUserProfile.collect {
                    when (it) {
                        is ResourceRemote.Loading -> {
                        }

                        is ResourceRemote.Success -> {
                            user = it.data
                            updateSharedViewModel(it.data)
                            authViewModel.getAllProfileWaitingAccept(it.data!!.id) { listWaitingAccept ->
                                sharedViewModel.setSharedFlowListUserWaitingAccept(listWaitingAccept)
                                authViewModel.getAllProfileMatch(it.data!!.id) { listWaitingMatch ->
                                    sharedViewModel.setSharedFlowListUserMatch(listWaitingMatch)
                                    authViewModel.getAllUserMatch(it.data!!.id) {
                                        sharedViewModel.setSharedFlowListUserMatchByMe(it)
                                        val listIdsWaitingAccept =
                                            listWaitingAccept.map { it.id }.toMutableList()
                                                .also { it.add(prefs.readIdUserLogin().toString()) }
                                        val listIdsUserMatch =
                                            listWaitingMatch.map { it.id }.toMutableList()
                                        val listIdsAlreadyMatch = it.map { it.id }.toMutableList()
                                        listIdsUserMatch.addAll(listIdsAlreadyMatch)
                                        listIdsWaitingAccept.addAll(listIdsUserMatch)
                                        authViewModel.getAllProfileSwipe(
                                            listIdsWaitingAccept,
                                            user!!.gender,
                                            Constants.LIMIT_REQUEST_SWIPE
                                        )
                                    }

                                }
                            }
                            Log.d(TAG, "observer user: ${it.data?.name}")
                        }

                        is ResourceRemote.Error -> {
                            showToast("Kiểm tra kết nối mạng!!!")
                        }
                        else -> {

                        }
                    }
                }
            }
        }

    }

    private fun updateSharedViewModel(data: UserProfile?) {
        Log.d("FragmentProfile", "updateSharedViewModel")
        data?.let {
            sharedViewModel.setSharedFlowBasicInformation(
                hashMapOf(
                    0 to data.name,
                    1 to data.dob,
                    2 to data.address,
                    3 to data.gender
                )
            )
            sharedViewModel.setSharedFlowJob(data.job)
            sharedViewModel.setSharedFlowSexuality(data.purpose)
            sharedViewModel.setSharedFlowInterest(data.interests ?: mutableListOf())
            data.interests?.let { it -> prefs.saveInterest(it.joinToString("&")) }
            sharedViewModel.setSharedFlowIntroduce(data.bio)
            sharedViewModel.setSharedFlowImage(data.images ?: emptyList())
            sharedViewModel.setListItemInformation(DataHelper.getListItemInformation())

            data.additionInformation?.let {
                sharedViewModel.setPositionInformation(-1)
                sharedViewModel.setSharedFlowTall(it.tall + 1001)
                sharedViewModel.setSharedFlowChild(it.child + 1001)
                sharedViewModel.setSharedFlowDrink(it.drink + 1001)
                sharedViewModel.setSharedFlowMaritalStatus(it.maritalStatus + 1001)
                sharedViewModel.setSharedFlowChooseGender(it.trueGender + 1001)
                sharedViewModel.setSharedFlowSmoke(it.smoking + 1001)
                sharedViewModel.setSharedFlowPet(it.pet + 1001)
                sharedViewModel.setSharedFlowReligion(it.religion + 1001)
                sharedViewModel.setSharedFlowCertificate(it.certificate + 1001)
            }
        }
    }


    override fun onDestroy() {
        authViewModel.resetStateGetUserProfile()
        authViewModel.resetSateGetAllProfileSwipe()
        super.onDestroy()
    }

    override fun onResume() {
        if (prefs.readIdUserLogin() != null) {
            realTimeViewModel.sendStatusOnOff(AccountOnline(prefs.readIdUserLogin()!!, true))
        }
        super.onResume()
    }

    override fun onStop() {
        try {
            if (prefs.readIdUserLogin() != null) {
                realTimeViewModel.sendStatusOnOff(AccountOnline(prefs.readIdUserLogin()!!, false))
            }
        } catch (e: Exception) {
            realTimeViewModel.sendStatusOnOff(AccountOnline("fake", false))
        }
        super.onStop()
    }

    override fun onPause() {
        try {
            if (prefs.readIdUserLogin() != null) {
                realTimeViewModel.sendStatusOnOff(AccountOnline(prefs.readIdUserLogin()!!, false))
            }
        } catch (e: Exception) {
            realTimeViewModel.sendStatusOnOff(AccountOnline("fake", false))
        }
        super.onPause()
    }

    private fun setInsetsWindow() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
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
        if (isLogin) navHostFragment.findNavController().navigate(R.id.fragmentMainViewPager)
    }


    override fun syncTheme(appTheme: AppTheme) {}
    private fun handleNavHostFragment() {
        navHostFragment.navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == R.id.fragmentSettings || destination.id == R.id.fragmentChats || destination.id == R.id.fragmentLoves
            ) {
                bottomBar.toVisible()
            } else {
                binding.root.fitsSystemWindows = false
                bottomBar.toGone()
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