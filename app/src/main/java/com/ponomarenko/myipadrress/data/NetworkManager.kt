package com.ponomarenko.myipadrress.data

import android.content.Context
import android.net.ConnectivityManager

object NetworkManager {


    fun updateIPAddress(context: Context) {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork
        val networkCapabilities = cm.getNetworkCapabilities(network)

        val ipAddress = IPAddress("1", NetworkType.MOBILE, "TEST")
        IPAddressRepository.setIpAddress(ipAddress)
    }


}
