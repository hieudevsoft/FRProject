package com.devapp.fr.data.models.messages

import android.media.MediaPlayer
import android.util.Log
import com.devapp.fr.data.models.MessageType
import com.devapp.fr.util.MediaHelper
import java.io.IOException

data class MessageAudio(
    override val id:String="",
    override val userId:String="",
    override var type: MessageType,
    override var isMe:Boolean=true,
    private var audio:String?=null,
    var isPlaying:Boolean = false,
    var duration:Int?=null
):MessageModel(id,userId,type) {
    val TAG = "MessageAudio"
    private var mediaPlayer:MediaPlayer?=null

    override fun <T> getContent(): T {
        return audio as T;
    }

    init {
        MediaHelper.initMediaPlayer(getContent<String>())
        MediaHelper.getMediaPlayer()?.let { mediaPlayer = it }
        duration = mediaPlayer?.duration
    }


    fun playingAudio(){
        try {
            Log.d(TAG, "playingAudio: Playing...")
            mediaPlayer?.start()
        } catch (e: IOException) {

        }
    }

    fun onCompleteAudio(callBack:(MediaPlayer)->Unit){
        mediaPlayer?.setOnCompletionListener {
            callBack(it)
        }
    }

    fun stopPlayingAudio(){
        try {
            if(mediaPlayer!=null){
                if(mediaPlayer!!.isPlaying){
                    mediaPlayer!!.stop()
                    mediaPlayer!!.reset()
                    mediaPlayer!!.release()
                    mediaPlayer = null
                }
            }
        }catch (e:java.lang.Exception){

        }
    }

    fun pausePlayingAudio(){
        try {
            if(mediaPlayer!=null){
                if(mediaPlayer!!.isPlaying)
                    mediaPlayer?.pause()
            }
        }catch (e:java.lang.Exception){

        }
    }
}