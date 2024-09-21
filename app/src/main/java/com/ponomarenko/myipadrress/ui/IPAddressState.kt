package com.ponomarenko.myipadrress.ui

data class IPAddressState(
    val internalIpAddress: String = "",
    val externalIpAddress: String = "",
    val networkType: String = "",
    val networkName: String = "",
    val isLoading: Boolean = false,
)