package com.devapp.fr.data.repositories

import com.devapp.fr.data.entities.googles.PostBodyFinger
import com.devapp.fr.network.GoogleService
import retrofit2.Call
import javax.inject.Inject

class FingerWriteRepository @Inject constructor(private val googleService: GoogleService){
    fun postBody(postBodyFinger: PostBodyFinger): Call<List<Any?>> {
        return googleService.postHandWriting(postBodyFinger)
    }
}