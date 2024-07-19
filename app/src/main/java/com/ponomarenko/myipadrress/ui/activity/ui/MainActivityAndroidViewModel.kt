package com.ponomarenko.myipadrress.ui.activity.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ponomarenko.myipadrress.ui.activity.utils.NetworkManager
import timber.log.Timber

class MainActivityAndroidViewModel(
    private val utils: NetworkManager
) : MainActivityViewModel, ViewModel() {

    override val ipAddress = MutableLiveData("")

    override val networkName = MutableLiveData("")

    override val networkType = MutableLiveData("")

    override fun onViewCreated() {
        loadData()
    }

    override fun loadData() {
        try {
            ipAddress.postValue(utils.getIPAddress(true))
            networkType.postValue(utils.getActiveNetworkType())
            networkName.postValue(utils.getActiveNetworkName())
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

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
}