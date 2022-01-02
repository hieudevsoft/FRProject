package com.devapp.fr.network

import android.content.Context
import android.widget.Toast
import com.devapp.fr.data.entities.UserProfile
import com.devapp.fr.di.IoDispatcher
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FireStoreService @Inject constructor(private val context: Context) {

    suspend fun addUserProfile(
        userProfile: UserProfile,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): ResourceRemote<String> {
        val collection = Firebase.firestore.collection("profiles")
        val res = withContext(dispatcher) {
            try {
                collection.add(userProfile).await()
                ResourceRemote.Success("success")
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                ResourceRemote.Error(e.message)
            }
        }
        return res
    }

}