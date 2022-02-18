package com.devapp.fr

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.content.ContextCompat
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MyApplication : Application() {
    companion object{
        val CHANNEL_ID = "app_fr"
    }
    override fun onCreate() {
        super.onCreate()
        createChannelNotification()
    }

    private fun createChannelNotification() {
        val defaultSoundUri: Uri =
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val att = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()
            val channel = NotificationChannel(CHANNEL_ID,"Fr",NotificationManager.IMPORTANCE_HIGH)
            channel.setSound(defaultSoundUri,att)
            channel.description = "Thông báo kết đôi"
            channel.enableLights(true)
            channel.lightColor = ContextCompat.getColor(applicationContext,R.color.background_dark_mode)
            channel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            channel.enableVibration(true)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.let {
                notificationManager.createNotificationChannel(channel)
            }
        }


    }
}