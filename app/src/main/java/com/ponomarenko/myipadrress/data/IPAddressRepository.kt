package com.ponomarenko.myipadrress.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object IPAddressRepository {

    private val ipAddress = MutableLiveData<IPAddress>()

    fun setIpAddress(ipAddress: IPAddress) {
        this.ipAddress.value = ipAddress
    }

    fun getIPAddress(): LiveData<IPAddress> {
        return ipAddress
    }


}
