package com.devapp.fr.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devapp.fr.data.entities.googles.PostBodyFinger
import com.devapp.fr.data.repositories.FingerWriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HandWriterViewModel @Inject constructor(
    private val app: Application, private val fingerWriteRepository: FingerWriteRepository
) : AndroidViewModel(app) {
    val TAG = "HandWriterViewModel"
    var resultResponse = MutableLiveData<List<Any?>?>()
    fun postBody(postBodyFinger: PostBodyFinger) {
        viewModelScope.launch(Dispatchers.IO) {
            fingerWriteRepository.postBody(postBodyFinger)
                .enqueue(object : Callback<List<Any?>> {
                    override fun onResponse(
                        call: Call<List<Any?>>,
                        response: Response<List<Any?>>
                    ) {
                        if (response.code() in 200..209) {
                            if (response.isSuccessful)
                                if (response.body() != null)
                                    resultResponse.postValue(response.body())
                                else Log.e(TAG, "onResponse: ${response.body()}")
                            else Log.e(TAG, "onResponse: failure response")
                        } else {
                            resultResponse.postValue(null)
                        }
                    }

                    override fun onFailure(call: Call<List<Any?>>, t: Throwable) {
                        Log.e(TAG, "onFailure: ${call.timeout()} ${t.message}")
                        resultResponse.postValue(null)
                    }

                })
        }
    }
}