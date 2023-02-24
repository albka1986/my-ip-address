package com.ponomarenko.myipadrress.ui.activity.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager
import android.util.Log
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.Collections
import java.util.Locale

class NetworkManager(context: Context) {

    private val connectivityManager: ConnectivityManager? = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

    fun getIPAddress(useIPv4: Boolean = true): String {
        try {
            val interfaces: List<NetworkInterface> = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (inf in interfaces) {
                val addrs: List<InetAddress> = Collections.list(inf.inetAddresses)
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress) {
                        val sAddr = addr.hostAddress
                        val isIPv4 = sAddr.indexOf(':') < 0
                        if (useIPv4) {
                            if (isIPv4) return sAddr
                        } else {
                            if (!isIPv4) {
                                val delim = sAddr.indexOf('%') // drop ip6 zone suffix
                                return if (delim < 0) sAddr.uppercase(Locale.getDefault()) else sAddr.substring(
                                    0,
                                    delim
                                )
                                    .uppercase(
                                        Locale.getDefault()
                                    )
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("NetworkManager", e.localizedMessage)
        }
        return ""
    }

    fun networkName(context: Context): String {
        var networkName = ""
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        var activeNetwork: NetworkInfo? = null
        if (cm != null) {
            activeNetwork = cm.activeNetworkInfo
        }
        val isConnected = activeNetwork != null &&
            activeNetwork.isConnectedOrConnecting
        if (isConnected) {
            networkName = activeNetwork!!.extraInfo
            if (networkName == null) {
                networkName = getMobileNetworkMName(context)
            }
        }
        Log.d("debug", "getExtraInfo: $networkName")
        return networkName.trim { it <= ' ' }
    }

    fun networkType(context: Context): String {
        var networkType = ""
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        var activeNetwork: NetworkInfo? = null
        if (cm != null) {
            activeNetwork = cm.activeNetworkInfo
        }
        val isConnected = activeNetwork != null &&
            activeNetwork.isConnectedOrConnecting
        if (isConnected) {
            networkType = activeNetwork!!.typeName
        }
        return networkType.trim { it <= ' ' }
    }

    private fun getMobileNetworkMName(context: Context): String {
        val manager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return manager.networkOperatorName
    }
}