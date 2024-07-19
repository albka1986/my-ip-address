package com.ponomarenko.myipadrress.ui.activity.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.telephony.TelephonyManager
import com.ponomarenko.myipadrress.ui.activity.model.NetworkType
import timber.log.Timber
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.Collections
import java.util.Locale

class NetworkManager(context: Context) {

    private val connectivityManager: ConnectivityManager? =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    private val telephonyManager: TelephonyManager? =
        context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager

    @Deprecated("use in case new method will bring any issue")
    fun iPAddress(useIPv4: Boolean = true): String {
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
        } catch (ex: Exception) {
            Timber.e(ex.localizedMessage)
        }
        return ""
    }

    fun getIPAddress(useIPv4: Boolean): String {
        try {
            NetworkInterface.getNetworkInterfaces()
                ?.iterator()
                ?.forEach { networkInterface ->
                    Collections.list(networkInterface.inetAddresses)
                        .forEach { inetAddress ->
                            if (!inetAddress.isLoopbackAddress) {
                                val ipAddress = inetAddress.hostAddress
                                val isIPv4 = inetAddress is Inet4Address
                                if (useIPv4) {
                                    if (isIPv4) return ipAddress
                                } else {
                                    if (!isIPv4) {
                                        val zoneIndex = ipAddress.indexOf('%')
                                        return if (zoneIndex < 0) {
                                            ipAddress.uppercase(Locale.getDefault())
                                        } else {
                                            ipAddress.substring(0, zoneIndex)
                                                .uppercase(Locale.getDefault())
                                        }
                                    }
                                }
                            }
                        }
                }
        } catch (ex: Exception) {
            Timber.e(ex.localizedMessage)
        }
        return ""
    }


    @Deprecated("backup")
    fun networkType(): String {
        var networkType = "- - - "
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

    fun getActiveNetworkType(): String {
        if (connectivityManager == null) return NetworkType.UNKNOWN.value

        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return if (networkCapabilities != null) {
            when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkType.WIFI.value
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    val mobileNetworkType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        networkCapabilities.networkSpecifier?.toString()
                    } else {
                        networkType()
                    }
                    mobileNetworkType ?: NetworkType.MOBILE.value
                }

                else -> NetworkType.UNKNOWN.value
            }
        } else {
            NetworkType.DISCONNECTED.value
        }
    }

    @Deprecated("use in case new method will bring any issue")
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

        Timber.d("getExtraInfo: $networkName")
        return networkName.trim { it <= ' ' }
    }

    @Deprecated("backup")
    fun getActiveNetworkName(): String {
        val activeNetwork = connectivityManager?.activeNetwork
        val networkCapabilities = connectivityManager?.getNetworkCapabilities(activeNetwork)
        return if (networkCapabilities != null) {
            when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    val wifiInfo = connectivityManager?.getNetworkInfo(NetworkCapabilities.TRANSPORT_WIFI)
                    wifiInfo?.extraInfo ?: ""
                }

                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    val mobileInfo = connectivityManager?.getNetworkInfo(NetworkCapabilities.TRANSPORT_CELLULAR)
                    mobileInfo?.extraInfo ?: ""
                }

                else -> ""
            }
        } else {
            ""
        }.trim()
    }

    private fun getMobileNetworkMName(): String =
        telephonyManager?.networkOperatorName.orEmpty()
}