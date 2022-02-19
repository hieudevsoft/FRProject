package com.devapp.fr.network

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.devapp.fr.MyApplication.Companion.CHANNEL_ID
import com.devapp.fr.ui.activities.MainActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NotificationService : Service() {
    companion object{
        val RC_CODE_NOTIFICATION = 0
    }
    private val TAG = "NotificationService"
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ")
        intent?.let {
            val data = it.getStringExtra("data")
            if (data != null) {
                val combineId = it.getStringExtra("id")
                if (combineId != null) {
                    val idOwner = combineId.split("#")[1]
                    val idNotification = combineId.split("#")[0]
                    if(idOwner != idOwner)
                        sendNotification(startId,data)
                }
            }
        }
        return START_NOT_STICKY
    }

    private fun sendNotification(id:Int,data: String) {
        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val intent = Intent(this,MainActivity::class.java).also {
            it.putExtra("navigate_chat",true)
        }
        val pendingIntent = PendingIntent.getActivity(applicationContext, RC_CODE_NOTIFICATION,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notification = Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("Fr Thông báo")
                .setContentText("$data đã phản hồi bạn")
                .setSmallIcon(com.devapp.fr.R.drawable.ic_heart_redcolor)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()
            startForeground(id,notification)
        } else {
            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Fr Thông báo")
                .setContentText("$data đã phản hồi bạn")
                .setSmallIcon(com.devapp.fr.R.drawable.ic_heart_redcolor)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX) // this is deprecated in API 26 but you can still use for below 26. check below update for 26 API
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .build()
            startForeground(id,notification)
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }
}