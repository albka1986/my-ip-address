package com.ponomarenko.myipadrress.ui.activity.ui

import androidx.lifecycle.LiveData

interface MainActivityViewModel {

    val networkType: LiveData<String>

    val networkName: LiveData<String>

    val ipAddress: LiveData<String>

    fun loadData()

    fun onIPAddressClicked()

    fun onTypeClicked()

    fun onNetworkNameClicked()

    fun onRefreshClicked()

    fun onViewCreated()
}