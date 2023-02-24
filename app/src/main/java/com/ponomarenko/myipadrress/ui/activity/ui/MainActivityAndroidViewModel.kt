package com.ponomarenko.myipadrress.ui.activity.ui

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ponomarenko.myipadrress.ui.activity.utils.NetworkManager

class MainActivityAndroidViewModel(
    private val utils: NetworkManager,
    private val context: Context
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
        networkType.postValue(utils.networkType(context))
        networkName.postValue(utils.networkName(context))
    }

    override fun onViewCreated() {
        loadData()
    }
}