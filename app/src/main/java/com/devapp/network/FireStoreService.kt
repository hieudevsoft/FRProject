package com.devapp.network

import android.content.Context
import android.widget.Toast
import com.devapp.fr.data.entities.UserProfile
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class FireStoreService private constructor(){

    object Holder{
        val instance = FireStoreService()
    }

    companion object{
        fun getInstance() = Holder.instance
    }

    private suspend fun addUserProfile(context:Context,userProfile: UserProfile):ResourceRemote<String>{
        val collection = Firebase.firestore.collection("profiles")
        val res = withContext(Dispatchers.IO){
            try {
                collection.add(userProfile).await()
                ResourceRemote.Success("success")
            }catch (e:Exception){
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                ResourceRemote.Error(e.message)
            }
        }
        return res
    }

}