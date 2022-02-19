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
import androidx.activity.viewModels
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.MultiInstanceInvalidationService
import com.devapp.fr.MyApplication.Companion.CHANNEL_ID
import com.devapp.fr.data.repositories.ServiceRepository
import com.devapp.fr.ui.activities.MainActivity
import com.devapp.fr.ui.viewmodels.RealTimeViewModel
import com.devapp.fr.ui.viewmodels.RealTimeViewModelFactory
import com.devapp.fr.util.storages.SharedPreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class NotificationService : LifecycleService() {
    @Inject
    lateinit var serviceRepository: ServiceRepository
    @Inject
    lateinit var prefs:SharedPreferencesHelper
    private var isStartTheFirstApp:Boolean = true
    companion object{
        val RC_CODE_NOTIFICATION = 0
    }
    private val TAG = "NotificationService"
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: onCreate")
    }

    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)
        if(isStartTheFirstApp) {
            sendNotification(startId,"Xin chào bạn tới ứng dụng ~",true)
            isStartTheFirstApp = false
        }
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        intent?.let {
            if(!it.getBooleanExtra("navigate_chat",false))
            lifecycleScope.launchWhenCreated {
                serviceRepository.readNotification(prefs.readIdUserLogin()!!,{
                    it?.let {
                        if(prefs.readIdUserLogin()!=it.idOwnerSend){
                            sendNotification(startId,it.message+", "+it.time,false)
                        }
                    }
                })
            }
        }
        return START_NOT_STICKY
    }

    private fun sendNotification(id:Int, data: String,isStartFirstApp:Boolean) {
        isStartTheFirstApp = false
        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val intent = Intent(this,MainActivity::class.java).also {
            it.putExtra("navigate_chat",true)
        }
        val pendingIntent = PendingIntent.getActivity(applicationContext, RC_CODE_NOTIFICATION,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notification = Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("Fr thông báo")
                .setContentText("$data ${if(!isStartFirstApp) "đã phản hồi bạn" else ""} ")
                .setSmallIcon(com.devapp.fr.R.drawable.ic_heart_redcolor)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()
            startForeground(id,notification)
        } else {
            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Fr thông báo")
                .setContentText("$data ${if(!isStartFirstApp) "đã phản hồi bạn" else ""} ")
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