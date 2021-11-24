package com.devapp.fr.util

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.devapp.fr.util.Constants.DATA_STORE_NAME
import com.devapp.fr.util.Constants.KEY_SKIP_SLIDE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore:DataStore<Preferences> by preferencesDataStore(DATA_STORE_NAME)
class DataStoreHelper private constructor() {
    val TAG = "DataStoreHelper"
    companion object{
        @Volatile
        private var instance:DataStoreHelper?=null
        fun getInstance(context:Context):DataStoreHelper{
            if(instance==null){
                synchronized(this){
                    return if(instance==null){
                        instance = DataStoreHelper()
                        instance as DataStoreHelper
                    } else instance as DataStoreHelper
                }
            } else return instance as DataStoreHelper
        }
    }

    //Make key for save to data store
    private object PreferenceKeys{
        val skipSlide = booleanPreferencesKey(KEY_SKIP_SLIDE)
    }

    /*
    * Save and read value for skipSlide key
    * Start
    */
    suspend fun saveSkipSlide(dataStore: DataStore<Preferences>,value:Boolean){
        dataStore.edit { preference->
            preference[PreferenceKeys.skipSlide] = value
        }
    }
    fun getSkipSlide(dataStore: DataStore<Preferences>): Flow<Boolean> {
        return dataStore.data.catch {
            if(it is IOException){
                Log.d(TAG, "getSkipSlide: "+it.message.toString())
                emit(emptyPreferences())
            } else{
                throw it
            }
        }.map {
            it[PreferenceKeys.skipSlide]?:false
        }
    }
    /*
    * Save and read value for skipSlide key
    * End
    */


}