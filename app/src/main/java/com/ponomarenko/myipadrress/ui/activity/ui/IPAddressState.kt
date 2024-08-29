package com.ponomarenko.myipadrress.ui.activity.ui

data class IPAddressState(
    val internalIpAddress: String = "",
    val externalIpAddress: String = "",
    val networkType: String = "",
    val networkName: String = "",
    val isLoading: Boolean = false,
)