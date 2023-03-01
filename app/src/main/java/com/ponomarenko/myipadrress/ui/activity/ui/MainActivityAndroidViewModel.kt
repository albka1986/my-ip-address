package com.ponomarenko.myipadrress.ui.activity.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ponomarenko.myipadrress.ui.activity.utils.NetworkManager

class MainActivityAndroidViewModel(
    private val utils: NetworkManager
) : MainActivityViewModel, ViewModel() {

    override val ipAddress = MutableLiveData("")

    override val networkName = MutableLiveData("")

    override val networkType = MutableLiveData("")

    override fun onIPAddressClicked() {
        //todo
    }

    override fun onTypeClicked() {
        //todo
    }

    override fun onNetworkNameClicked() {
        //todo
    }

    override fun onRefreshClicked() {
        loadData()
    }

    override fun loadData() {
        ipAddress.postValue(utils.getIPAddress(true))
        networkType.postValue(utils.networkType())
        networkName.postValue(utils.networkName())
    }

    override fun onViewCreated() {
        loadData()
    }
}