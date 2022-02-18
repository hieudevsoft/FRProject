package com.devapp.fr.network

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.devapp.fr.data.entities.UserProfile
import com.devapp.fr.di.IoDispatcher
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StorageService @Inject constructor(private val context:Context) {

    suspend fun addImagesById(
        id: String,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO,
        listName:List<String>,
        vararg uriImage:Uri,
    ): ResourceRemote<String> {
        val ref = Firebase.storage.reference
        val res = withContext(dispatcher) {
            try {
                uriImage.forEachIndexed { index, uri ->
                    ref.child("images/$id/${listName[index]}").putFile(uri).await()
                }
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

    suspend fun downloadImage(
        id: String,
        nameFile:String,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): ResourceRemote<Bitmap> {
        val ref = Firebase.storage.reference
        val res = withContext(dispatcher) {
            try {
                val maxDownloadSize = 10L * 1024 *1024
                val bytes = ref.child("images/$id/$nameFile").getBytes(maxDownloadSize).await()
                val bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.size)
                ResourceRemote.Success(bitmap)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
                ResourceRemote.Error(null,e.message)
            }
        }
        return res
    }

    suspend fun deleteImageByNameOrUrl(
        id: String,
        isByName:Boolean,
        nameFile:String="",
        url:String="",
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): ResourceRemote<Boolean> {
        Log.d("FragmentProfile", "deleteImageByNameOrUrl: $id")
        val ref = Firebase.storage.reference
        val res = withContext(dispatcher) {
            try {
                if(isByName){
                    ref.child("images/$id/$nameFile").delete().await()
                    ResourceRemote.Success(true)
                }else{
                    Log.d("FragmentProfile", "deleteImageByNameOrUrl: $id")
                    val images = ref.child("images/$id").listAll().await()
                    images.items.forEach {
                        if(it.downloadUrl.await().toString().contains(url)){
                            it.delete().await()
                            return@forEach
                        }
                    }
                    ResourceRemote.Success(true)
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
                ResourceRemote.Error(false,e.message)
            }
        }
        return res
    }

    suspend fun downloadAllImagesById(
        id: String,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): ResourceRemote<List<String>> {
        val ref = Firebase.storage.reference
        val res = withContext(dispatcher) {
            try {
                val images = ref.child("images/$id").listAll().await()
                val imageUrls = mutableListOf<String>()
                images.items.forEach {
                    imageUrls.add(it.downloadUrl.await().toString())
                }
                ResourceRemote.Success(imageUrls)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
                ResourceRemote.Error(null,e.message)
            }
        }
        return res
    }

    suspend fun addImageIntoStorageChasts(
        name: String,
        senderRoom:String,
        recieverRoom:String,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO,
    ): Boolean {
        val ref = Firebase.storage.reference
        val res = withContext(dispatcher) {
            try {

                true
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
                false
            }
        }
        return res
    }
}