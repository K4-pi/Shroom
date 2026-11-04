package com.example.shroom.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

/**
 * @brief Reports whether the device currently has an Internet-capable network.
 * @receiver Application or activity context used to look up ConnectivityManager.
 * @return True when the active network advertises NET_CAPABILITY_INTERNET.
 */
fun Context.isNetworkOnline(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = cm.activeNetwork ?: return false
    val caps = cm.getNetworkCapabilities(network) ?: return false
    return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}
