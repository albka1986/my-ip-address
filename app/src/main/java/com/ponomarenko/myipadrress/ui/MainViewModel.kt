package com.ponomarenko.myipadrress.ui

import androidx.lifecycle.ViewModel
import com.ponomarenko.myipadrress.data.IPAddressRepository

class MainViewModel(private val repository: IPAddressRepository) : ViewModel() {
    fun getIPAddress() = repository.getIPAddress()
}