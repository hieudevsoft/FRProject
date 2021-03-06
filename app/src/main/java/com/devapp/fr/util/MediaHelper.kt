package com.devapp.fr.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import java.io.IOException
import java.lang.reflect.Executable
import kotlin.math.log

object MediaHelper {
    val TAG = "MediaHelper"
    var mFileName = ""
    private var mRecorder:MediaRecorder?=null
    private var mediaPlayer:MediaPlayer?=null
    fun getMediaPlayer() = mediaPlayer

    fun startRecording(context:Context){
        mRecorder = MediaRecorder()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
            val cw = ContextWrapper(context)
            mFileName= cw.getExternalFilesDir(Environment.DIRECTORY_DCIM).toString() + "/myrec.3gp"
        }
        else
        {
            mFileName = Environment.getExternalStorageDirectory().toString()+"/myrec.3gp"
        }
        mRecorder?.let {
            mRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
            mRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mRecorder!!.setOutputFile(mFileName)
            try {
                mRecorder!!.prepare()
                mRecorder!!.start()
                Toast.makeText(context, "Bắt đầu ghi âm ...", Toast.LENGTH_SHORT).show()
            }catch (e:Exception){
                Toast.makeText(context, "Oops!! ~ ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }?:Toast.makeText(context, "Oops!!", Toast.LENGTH_SHORT).show()

    }

    fun stopRecording(context:Context){
        try {
            mRecorder?.stop()
            mRecorder?.release()
            mRecorder = null
            Log.d("FragmentInbox", "stopRecording: $mFileName")
        }catch (e:Exception){
            Toast.makeText(context, "Co loi xay ra ~ ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun initMediaPlayer(mPath:String){
        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setAudioAttributes(AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .build())
        mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer!!.setDataSource(mPath)
        mediaPlayer?.prepare()
    }

    fun playingAudio(){
        try {
            Log.d(TAG, "playingAudio: Playing...")
            mediaPlayer?.start()
        } catch (e: IOException) {
            Log.d(TAG, "playingAudio: ${e.message} $mFileName")
        }
    }

    fun stopPlayingAudio(){
        try {
            if(mediaPlayer!!.isPlaying){
                mediaPlayer!!.stop()
                mediaPlayer!!.reset()
                mediaPlayer!!.release()
                mediaPlayer = null
            }
        }catch (e:java.lang.Exception){

        }
    }

    fun pausePlayingAudio(){
        try {
            if(mediaPlayer!!.isPlaying)
            mediaPlayer?.pause()
        }catch (e:java.lang.Exception){

        }
    }
}