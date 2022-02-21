package com.devapp.fr.network

import com.devapp.fr.data.entities.googles.PostBodyFinger
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface GoogleService{
    @POST("request?itc=vi-t-i0-handwrit&app=chext")
    fun postHandWriting(@Body postBodyFinger: PostBodyFinger): Call<List<Any?>>
}