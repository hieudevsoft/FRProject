package com.devapp.fr.network

sealed class ResourceRemote<out T> {
    object Idle:ResourceRemote<Nothing>()
    object Loading:ResourceRemote<Nothing>()
    object Empty:ResourceRemote<Nothing>()
    class Success<T>(var data: T) : ResourceRemote<T>()
    class Error<T>(var message:String?=null) : ResourceRemote<T>()
}