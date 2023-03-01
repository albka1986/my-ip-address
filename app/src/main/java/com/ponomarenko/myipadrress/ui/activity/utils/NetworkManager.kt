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

    private val connectivityManager: ConnectivityManager? = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    private val telephonyManager: TelephonyManager? = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager

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

    fun networkName(): String {
        var networkName = ""
        var activeNetwork: NetworkInfo? = null
        if (connectivityManager != null) {
            activeNetwork = connectivityManager.activeNetworkInfo
        }
        val isConnected = activeNetwork != null &&
            activeNetwork.isConnectedOrConnecting
        if (isConnected) {
            networkName = activeNetwork!!.extraInfo
            if (networkName.isEmpty()) {
                networkName = getMobileNetworkMName()
            }
        }
        Log.d("debug", "getExtraInfo: $networkName")
        return networkName.trim { it <= ' ' }
    }

    fun networkType(): String {
        var networkType = ""
        var activeNetwork: NetworkInfo? = null
        if (connectivityManager != null) {
            activeNetwork = connectivityManager.activeNetworkInfo
        }
        val isConnected = activeNetwork != null &&
            activeNetwork.isConnectedOrConnecting
        if (isConnected) {
            networkType = activeNetwork!!.typeName
        }
        return networkType.trim { it <= ' ' }
    }

    private fun getMobileNetworkMName(): String =
        telephonyManager?.networkOperatorName.orEmpty()
}