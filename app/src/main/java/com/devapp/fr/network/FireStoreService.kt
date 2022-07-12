package com.devapp.fr.network

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.devapp.fr.data.entities.AdditionInformation
import com.devapp.fr.data.entities.UserProfile
import com.devapp.fr.di.IoDispatcher
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.auth.User
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

    suspend fun getAllUserProfileByLimit(
        ids: List<String>,
        gender:Int,
        limit:Long?=null,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): ResourceRemote<List<UserProfile>> {
        val collection = Firebase.firestore.collection("profiles")
        val res = withContext(dispatcher) {
            try {
                var queriesSnapshot: QuerySnapshot?

                if(limit!=null)
                queriesSnapshot = collection
                    .whereNotEqualTo("gender",gender)
                    .limit(limit)
                    .get()
                    .await()
                else queriesSnapshot = collection
                    .whereNotEqualTo("gender",gender)
                    .get()
                    .await()

                var listResult = mutableListOf<UserProfile>()
                queriesSnapshot.documents.forEach {
                    val item = it.toObject<UserProfile>()
                    if (item != null) {
                        if(!ids.contains(item.id))
                            listResult.add(item)
                    }
                }
                listResult.let {
                    listResult.shuffle()
                    if(listResult.size>=10) listResult = listResult.subList(0,9)
                    ResourceRemote.Success(listResult)
                }

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

    suspend fun sendNotificationForPartner(
        partnerId:String,
        ownerId:String,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): ResourceRemote<String> {
        val collectionNotifications = Firebase.firestore.collection("notifications").document(partnerId)
        val collectionWaitingAccept = Firebase.firestore.collection("waiting_accept").document(ownerId)
        val res = withContext(dispatcher) {
            try {
                val listIdsNotifications = collectionNotifications.get().await()
                val listIdsWaitingAccept = collectionWaitingAccept.get().await()
                var listPushNotifications = listIdsNotifications.data
                var listPushWaitingAccept = listIdsWaitingAccept.data
                if (listPushNotifications == null) {
                    listPushNotifications = hashMapOf()
                }
                if (listPushWaitingAccept == null) {
                    listPushWaitingAccept = hashMapOf()
                }

                listPushNotifications[listPushNotifications.size.toString()] = ownerId
                listPushWaitingAccept[listPushWaitingAccept.size.toString()] = partnerId
                collectionNotifications.set(listPushNotifications, SetOptions.merge())
                collectionWaitingAccept.set(listPushWaitingAccept, SetOptions.merge())

                ResourceRemote.Success("success")
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
                Log.d("TAG", "sendNotificationForPartner: ${e.message}")
                ResourceRemote.Error(e.message)
            }
        }
        return res
    }

    suspend fun getAllUserWaitingAcceptById(
        ownerId:String,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO,
        snapshotCallBack:(List<UserProfile>)->Unit
    ) {
        val collectionWaitingAccept = Firebase.firestore.collection("waiting_accept").document(ownerId)
        val collectionUserProfile = Firebase.firestore.collection("profiles")
       withContext(dispatcher) {
            try {
                val listUserResult = mutableListOf<UserProfile>()
                collectionWaitingAccept.addSnapshotListener { value, error ->
                    error?.let {
                        return@addSnapshotListener
                    }
                    value?.let {
                        listUserResult.clear()
                        if(it.data!=null&&it.data!!.isNotEmpty())
                        it.data!!.forEach { (_, any) ->
                            val id = any.toString()
                            val queriesSnapshot =
                                collectionUserProfile
                                    .whereEqualTo("id", id)
                                    .get()
                            queriesSnapshot.addOnCompleteListener {task->
                                if(task.isSuccessful){
                                    task.result.documents[0].toObject(UserProfile::class.java)
                                        ?.let { listUserResult.add(it) }
                                    snapshotCallBack(listUserResult.toList())
                                }
                            }
                        } else snapshotCallBack(emptyList())
                    }

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show() }
                Log.d("TAG", "getAllUserWaitingAcceptById: ${e.message}")
            }
        }
    }

    suspend fun getAllUserSendNotificationToMe(
        ownerId:String,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO,
        snapshotCallBack:(List<UserProfile>)->Unit
    ) {
        val collectionSendNotificationToMe = Firebase.firestore.collection("notifications").document(ownerId)
        val collectionUserProfile = Firebase.firestore.collection("profiles")
        withContext(dispatcher) {
            try {
                val listUserResult = mutableListOf<UserProfile>()
                collectionSendNotificationToMe.addSnapshotListener { value, error ->
                    error?.let {
                        return@addSnapshotListener
                    }
                        value?.let {
                            listUserResult.clear()
                            if(it.data==null || it.data!!.isEmpty()) snapshotCallBack(emptyList())
                             else it.data?.forEach { (_, any) ->
                                val id = any.toString()
                                val queriesSnapshot =
                                    collectionUserProfile
                                        .whereEqualTo("id", id)
                                        .get()
                                queriesSnapshot.addOnCompleteListener {task->
                                    if(task.isSuccessful){
                                        task.result.documents[0].toObject(UserProfile::class.java)
                                            ?.let { listUserResult.add(it) }
                                        snapshotCallBack(listUserResult.toList())
                                    }
                                }

                            }
                        }

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show() }
                Log.d("TAG", "sendNotificationToMe: ${e.message}")
            }
        }
    }

    suspend fun acceptOrCancelInviteMatch(
        partnerId:String,
        ownerId:String,
        isAccept:Boolean,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): ResourceRemote<String> {
        val collectionNotifications = Firebase.firestore.collection("notifications").document(ownerId)
        val collectionWaitingAccept = Firebase.firestore.collection("waiting_accept").document(partnerId)
        val collectionMatchOwner= Firebase.firestore.collection("matches").document(ownerId)
        val collectionMatchPartner= Firebase.firestore.collection("matches").document(partnerId)
        val res = withContext(dispatcher) {
            try {
                val listIdsNotifications = collectionNotifications.get().await()
                val listIdsWaitingAccept = collectionWaitingAccept.get().await()
                val listIdsMatchOwner = collectionMatchOwner.get().await()
                val listIdsMatchPartner = collectionMatchPartner.get().await()


                var listPushNotifications = listIdsNotifications.data
                var listPushWaitingAccept = listIdsWaitingAccept.data
                var listPushMatchOwner = listIdsMatchOwner.data
                var listPushMatchPartner = listIdsMatchPartner.data

                if (listPushNotifications == null) {
                    listPushNotifications = hashMapOf()
                }
                if (listPushWaitingAccept == null) {
                    listPushWaitingAccept = hashMapOf()
                }
                if (listPushMatchOwner == null) {
                    listPushMatchOwner = hashMapOf()
                }
                if (listPushMatchPartner == null) {
                    listPushMatchPartner = hashMapOf()
                }

                val list1 = listPushNotifications.values.toMutableList()
                list1.remove(partnerId)
                listPushNotifications.apply {
                    clear()
                    list1.forEachIndexed { index, any -> this.put(index.toString(),any) }
                }

                val list2 = listPushWaitingAccept.values.toMutableList()
                list2.remove(ownerId)
                listPushWaitingAccept.apply {
                    clear()
                    list2.forEachIndexed { index, any -> this[index.toString()] = any }
                }


                Log.d("TAG", "acceptOrCancelInviteMatch: $listPushNotifications ${listPushNotifications.isEmpty()}")
                Log.d("TAG", "acceptOrCancelInviteMatch: $listPushWaitingAccept ${listPushWaitingAccept.isEmpty()}")
                collectionNotifications.set(listPushNotifications)
                collectionWaitingAccept.set(listPushWaitingAccept)

                if(isAccept) {
                    listPushMatchOwner[listPushMatchOwner.size.toString()] = partnerId
                    listPushMatchPartner[listPushMatchPartner.size.toString()] = ownerId
                    collectionMatchOwner.set(listPushMatchOwner)
                    collectionMatchPartner.set(listPushMatchPartner)
                }

                ResourceRemote.Success("success")
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
                Log.d("TAG", "sendNotificationForPartner: ${e.message}")
                ResourceRemote.Error(e.message)
            }
        }
        return res
    }

    suspend fun getAllUserMatchesMe(
        ownerId:String,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO,
        snapshotCallBack:(List<UserProfile>)->Unit
    ) {
        val collectionMatches = Firebase.firestore.collection("matches").document(ownerId)
        val collectionUserProfile = Firebase.firestore.collection("profiles")
        withContext(dispatcher) {
            try {
                val listUserResult = mutableListOf<UserProfile>()
                collectionMatches.addSnapshotListener { value, error ->
                    error?.let {
                        return@addSnapshotListener
                    }
                    value?.let {
                        listUserResult.clear()
                        if(it.data==null || it.data!!.isEmpty()) snapshotCallBack(emptyList())
                        else {
                            it.data?.forEach { (_, any) ->
                                val id = any.toString()
                                val queriesSnapshot =
                                    collectionUserProfile
                                        .whereEqualTo("id", id)
                                        .get()
                                queriesSnapshot.addOnCompleteListener {task->
                                    if(task.isSuccessful){
                                        task.result.documents[0].toObject(UserProfile::class.java)
                                            ?.let { listUserResult.add(it) }
                                        snapshotCallBack(listUserResult.toList())
                                    }
                                }
                            }
                        }
                    }

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show() }
            }
        }
    }

    suspend fun getAllUserProfileByPersonality(
        ids: List<String>,
        personality:Int,
        limit: Long,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): ResourceRemote<List<UserProfile>> {
        val collection = Firebase.firestore.collection("profiles")
        val res = withContext(dispatcher) {
            try {
                var queriesSnapshot: QuerySnapshot? = collection
                    .limit(limit)
                    .get()
                    .await()

                var listResult = mutableListOf<UserProfile>()
                queriesSnapshot?.documents?.forEach {
                    val item = it.toObject<UserProfile>()
                    if (item != null) {
                        if(!ids.contains(item.id) && item.additionInformation?.personality==personality)
                            listResult.add(item)
                    }
                }
                listResult.let {
                    listResult.shuffle()
                    if(listResult.size>=10) listResult = listResult.subList(0,9)
                    ResourceRemote.Success(listResult)
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
                ResourceRemote.Error(null, e.message)
            }
        }
        return res
    }

}