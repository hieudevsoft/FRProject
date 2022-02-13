package com.devapp.fr.network

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.devapp.fr.data.entities.AdditionInformation
import com.devapp.fr.data.entities.UserProfile
import com.devapp.fr.di.IoDispatcher
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
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
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
                ResourceRemote.Error(e.message)
            }
        }
        return res
    }

    suspend fun getUserProfile(
        id: String,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): ResourceRemote<UserProfile?> {
        val collection = Firebase.firestore.collection("profiles")
        val res = withContext(dispatcher) {
            try {
                val queriesSnapshot = collection
                    .whereEqualTo("id", id)
                    .orderBy("email")
                    .get()
                    .await()
                ResourceRemote.Success(queriesSnapshot.documents[0].toObject<UserProfile>())

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
                ResourceRemote.Error(null, e.message)
            }
        }
        return res
    }

    suspend fun isEmailExist(
        email: String,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): ResourceRemote<String> {
        val collection = Firebase.firestore.collection("profiles")
        val res = withContext(dispatcher) {
            try {
                val snapShot = collection.whereEqualTo("email", email).get().await()
                if (snapShot.documents.isNotEmpty())
                    ResourceRemote.Success("success find")
                else ResourceRemote.Empty
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
                ResourceRemote.Error(e.message)
            }
        }
        return res
    }

    suspend fun loginWithEmailAndPassword(
        email: String,
        password:String,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): ResourceRemote<String> {
        val collection = Firebase.firestore.collection("profiles")
        val res = withContext(dispatcher) {
            try {
                val snapShot = collection
                    .whereEqualTo("email", email)
                    .whereEqualTo("password",password)
                    .get()
                    .await()
                if (snapShot.documents.isNotEmpty())
                    ResourceRemote.Success(snapShot.documents.first().toObject(UserProfile::class.java)!!.id)
                else ResourceRemote.Empty
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
                ResourceRemote.Error(e.message)
            }
        }
        return res
    }

    suspend fun updateImages(
        id: String,
        listImage:List<String>,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): ResourceRemote<Boolean> {
        val collection = Firebase.firestore.collection("profiles")
        val res = withContext(dispatcher) {
            try {
                val snapShot = collection.whereEqualTo("id", id).get().await()
                Firebase.firestore.runTransaction {
                    if (snapShot.documents.isNotEmpty()){
                        val profile = collection.document(snapShot.documents[0].id)
                        it.update(profile,"images",listImage)
                    }
                }
                ResourceRemote.Success(true)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
                ResourceRemote.Error(false,e.message)
            }
        }
        return res
    }

    suspend fun updateBasicInformation(
        id:String,
        mapBasicInformation:HashMap<Int,Any>,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): ResourceRemote<Boolean> {
        val collection = Firebase.firestore.collection("profiles")
        val res = withContext(dispatcher) {
            try {
                val snapShot = collection.whereEqualTo("id", id).get().await()
                Firebase.firestore.runTransaction {
                    if (snapShot.documents.isNotEmpty()){
                        val profile = collection.document(snapShot.documents[0].id)
                        it.update(profile,"name",mapBasicInformation[0] as String)
                        it.update(profile,"dob",mapBasicInformation[1] as String)
                        it.update(profile,"address",mapBasicInformation[2] as String)
                        it.update(profile,"gender",mapBasicInformation[3] as Int)
                    }
                }
                ResourceRemote.Success(true)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
                ResourceRemote.Error(false,e.message)
            }
        }
        return res
    }

    suspend fun <T> updateFieldByName(
        id:String,
        fileName:String,
        data:T,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): ResourceRemote<Boolean> {
        val collection = Firebase.firestore.collection("profiles")
        val res = withContext(dispatcher) {
            try {
                val snapShot = collection.whereEqualTo("id", id).get().await()
                Firebase.firestore.runTransaction {
                    if (snapShot.documents.isNotEmpty()){
                        val profile = collection.document(snapShot.documents[0].id)
                        it.update(profile,fileName,data)
                    }
                }
                ResourceRemote.Success(true)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
                ResourceRemote.Error(false,e.message)
            }
        }
        return res
    }

    suspend fun updateFieldAdditionalInformation(
        id:String,
        property:String,
        data:Int,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): ResourceRemote<Boolean> {
        val collection = Firebase.firestore.collection("profiles")
        val res = withContext(dispatcher) {
            try {
                val snapShot = collection.whereEqualTo("id", id).get().await()
                Firebase.firestore.runTransaction {
                    if (snapShot.documents.isNotEmpty()){
                        val profile = collection.document(snapShot.documents[0].id)
                        var additionalInformation = snapShot.documents[0].toObject(UserProfile::class.java)?.additionInformation
                        if(additionalInformation==null) additionalInformation = AdditionInformation()
                        additionalInformation.setNewDataByName(property,data)
                        it.update(profile,"additionInformation",additionalInformation)
                    }
                }
                ResourceRemote.Success(true)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
                ResourceRemote.Error(false,e.message)
            }
        }
        return res
    }

}