package com.ponomarenko.myipadrress.model

enum class NetworkType(val value: String) {
    WIFI("Wi-Fi"),
    CELLULAR("Cellular"),
    DISCONNECTED("Disconnected"),
    UNKNOWN("Unknown")
}