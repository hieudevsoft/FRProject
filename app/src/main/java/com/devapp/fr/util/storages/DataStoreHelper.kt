package com.devapp.fr.util.storages

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.devapp.fr.util.Constants.DATA_STORE_NAME
import com.devapp.fr.util.Constants.KEY_DARK_MODE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore:DataStore<Preferences> by preferencesDataStore(DATA_STORE_NAME)
class DataStoreHelper private constructor() {
    val TAG = "DataStoreHelper"
    companion object{
        @Volatile
        private var instance: DataStoreHelper?=null
        fun getInstance(): DataStoreHelper {
            if(instance ==null){
                synchronized(this){
                    return if(instance ==null){
                        instance = DataStoreHelper()
                        instance as DataStoreHelper
                    } else instance as DataStoreHelper
                }
            } else return instance as DataStoreHelper
        }
    }

    //Make key for save to data store
    private object PreferenceKeys{
        val darkMode = booleanPreferencesKey(KEY_DARK_MODE)
    }


    /*
    * Save and read value for darkMode key
    * Start
    */
    suspend fun saveDarkMode(dataStore: DataStore<Preferences>,value:Boolean){
        dataStore.edit { preference->
            preference[PreferenceKeys.darkMode] = value
        }
    }
    fun getDarkMode(dataStore: DataStore<Preferences>): Flow<Boolean> {
        return dataStore.data.catch {
            if(it is IOException){
                Log.d(TAG, "getSkipSlide: "+it.message.toString())
                emit(emptyPreferences())
            } else{
                throw it
            }
        }.map {
            it[PreferenceKeys.darkMode]?:false
        }
    }
    /*
    * Save and read value for darkMode key
    * End
    */


}