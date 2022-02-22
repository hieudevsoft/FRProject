package com.devapp.fr.ui.activities

import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.devapp.fr.R
import com.devapp.fr.databinding.ActivityVideoCallBinding
import com.devapp.fr.ui.viewmodels.RealTimeViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.jitsi.meet.sdk.JitsiMeet
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.net.URL

@AndroidEntryPoint
class VideoCallActivity : JitsiMeetActivity() {
    private lateinit var serverUrl: URL
    private lateinit var binding: ActivityVideoCallBinding
    private val realTimeViewModel:RealTimeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoCallBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            serverUrl = URL("https://meet.jit.si")
            val defaultOptions = JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverUrl)
                .build()
            JitsiMeet.setDefaultConferenceOptions(defaultOptions)
            val options = JitsiMeetConferenceOptions.Builder()
                .setRoom(intent.getStringExtra("roomId"))
                .setAudioOnly(false)
                .setAudioMuted(false)
                .build()
            launch(this@VideoCallActivity, options)
        } catch (e: Exception) {

        }
    }

    override fun onDestroy() {
        intent.getStringExtra("partnerId")
            ?.let { realTimeViewModel.sendNotificationCallVideo(it,null,"","") }
        super.onDestroy()
    }

}