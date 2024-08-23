package com.ponomarenko.myipadrress.ui.activity.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(IPAddressState())
    val uiState: StateFlow<IPAddressState> = _uiState.asStateFlow()

    init {
        updateUIState()
    }

    private fun updateUIState() {
        _uiState.value = IPAddressState(
            //            ipAddress = utils.getIPAddress(true),
            //            networkType = utils.getActiveNetworkType(),
            //            networkName = utils.getActiveNetworkName()
            ipAddress = "192.168.0.1",
            networkType = "192.168.0.1",
            networkName = "192.168.0.1"
        )
    }

    fun onRefreshClicked() {
        updateUIState()
    }
}