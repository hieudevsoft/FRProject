package com.devapp.fr.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object NetworkHelper {
    fun isInternetConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager?.activeNetwork
            val networkCap = connectivityManager?.getNetworkCapabilities(network)
            networkCap != null && networkCap.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) && networkCap.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_VALIDATED
            )
        } else {
            val networkInfo = connectivityManager?.activeNetworkInfo
            networkInfo != null && networkInfo.isConnectedOrConnecting
        }
    }

}