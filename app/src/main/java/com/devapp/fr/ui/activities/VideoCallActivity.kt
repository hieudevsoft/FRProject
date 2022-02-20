package com.devapp.fr.ui.activities

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.devapp.fr.R
import com.devapp.fr.databinding.ActivityVideoCallBinding
import org.jitsi.meet.sdk.JitsiMeet
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.net.URL

class VideoCallActivity : JitsiMeetActivity() {

    private lateinit var serverUrl:URL
    private lateinit var binding: ActivityVideoCallBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoCallBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            serverUrl  = URL("https://meet.jit.si")
            val defaultOptions = JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverUrl)
                .build()
            JitsiMeet.setDefaultConferenceOptions(defaultOptions)
            binding.root.setOnClickListener {
                val options = JitsiMeetConferenceOptions.Builder()
                    .setRoom("123")
                    .setAudioOnly(false)
                    .setAudioMuted(false)
                    .build()
                launch(this@VideoCallActivity,options)
            }
        }catch (e:Exception){

        }
    }

}