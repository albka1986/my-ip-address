package com.ponomarenko.myipadrress.ui.activity.ui

import androidx.lifecycle.LiveData

interface MainActivityViewModel {

    val ipAddress: LiveData<String>

    val networkType: LiveData<String>

    val networkName: LiveData<String>

    fun loadData()

    fun onIPAddressClicked()

    fun onTypeClicked()

    fun onNetworkNameClicked()

    fun onRefreshClicked()

    fun onViewCreated()
}