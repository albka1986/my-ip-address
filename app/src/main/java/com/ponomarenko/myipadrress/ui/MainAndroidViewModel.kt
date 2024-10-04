package com.ponomarenko.myipadrress.ui

import android.app.Application
import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.telephony.TelephonyManager
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ponomarenko.myipadrress.R
import com.ponomarenko.myipadrress.model.NetworkType
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.Collections

class MainAndroidViewModel(application: Application) : ViewModel() {

    private val connectivityManager: ConnectivityManager by lazy {
        application.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private val wifiManager: WifiManager by lazy {
        application.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    private val telephonyManager: TelephonyManager by lazy {
        application.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    }

    private val locationManager: LocationManager by lazy {
        application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private val httpClient: HttpClient by lazy { HttpClient() }

    private val networkRequest: NetworkRequest by lazy {
        NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
    }

    private val networkCallback by lazy {
        @RequiresApi(Build.VERSION_CODES.S)
        object : ConnectivityManager.NetworkCallback(FLAG_INCLUDE_LOCATION_INFO) {
            override fun onCapabilitiesChanged(
                network: Network,
                capabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, capabilities)
                (capabilities.transportInfo as? WifiInfo)?.apply {
                    if (ssid != WifiManager.UNKNOWN_SSID) {
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLocationEnabled = true,
                                networkName = ssid
                                    .removePrefix("\"")
                                    .removeSuffix("\"")
                            )
                        }
                    } else {
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLocationEnabled = locationManager.isProviderEnabled(
                                    LocationManager.GPS_PROVIDER
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private val noNetworkText = application.getString(R.string.no_network)
    private val wifiText = application.getString(R.string.wifi)
    private val cellularText = application.getString(R.string.cellular)
    private val unknownText = application.getString(R.string.unknown)
    private val defaultIpAddress = application.getString(R.string.default_ip_address)

    private val _uiState = MutableStateFlow(IPAddressState())
    val uiState: StateFlow<IPAddressState> = _uiState.asStateFlow()

    override fun onCleared() {
        super.onCleared()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }

    fun updateData() {
        _uiState.update { currentState ->
            currentState.copy(isLoading = true)
        }

        val networkType: NetworkType = getNetworkType()

        _uiState.update { currentState ->
            currentState.copy(
                internalIpAddress = getInternalIpAddress().takeIf { networkType != NetworkType.DISCONNECTED }
                    ?: defaultIpAddress,
                networkType = getNetworkTypeText(networkType),
                networkName = getNetworkName(networkType),
            )
        }
            .also {
                getExternalIpAddress()
            }
    }

    private fun getNetworkTypeText(networkType: NetworkType) =
        when (networkType) {
            NetworkType.WIFI -> wifiText
            NetworkType.CELLULAR -> cellularText
            NetworkType.UNKNOWN -> unknownText
            else -> noNetworkText
        }

    private fun getNetworkName(networkType: NetworkType): String =
        when (networkType) {
            NetworkType.WIFI -> getWifiNetworkName()
            NetworkType.CELLULAR -> telephonyManager.simOperatorName
            else -> noNetworkText
        }

    private fun getWifiNetworkName(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestAndListenForNetwork()
            ""
        } else {
            // For older versions (below Android 12)
            @Suppress("DEPRECATION")
            wifiManager
                .connectionInfo
                .ssid
                .removePrefix("\"")
                .removeSuffix("\"")
        }
    }

    private fun requestAndListenForNetwork() {
        connectivityManager.requestNetwork(networkRequest, networkCallback)
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    private fun getNetworkType(): NetworkType {
        val activeNetwork = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return when {
            activeNetwork == null -> NetworkType.DISCONNECTED
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkType.WIFI
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkType.CELLULAR
            else -> NetworkType.UNKNOWN
        }
    }

    private fun getInternalIpAddress(): String {
        // For Android 12 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val network: Network = connectivityManager.activeNetwork ?: return defaultIpAddress
            val linkProperties: LinkProperties = connectivityManager.getLinkProperties(network)
                ?: return defaultIpAddress
            val addresses: List<InetAddress> = linkProperties.linkAddresses.map { it.address }

            for (address in addresses) {
                if (address is Inet4Address) {
                    return address.hostAddress ?: defaultIpAddress
                }
            }
        } else {
            // Fallback for older versions
            try {
                val interfaces: ArrayList<NetworkInterface> = Collections.list(NetworkInterface.getNetworkInterfaces())
                for (networkInterface: NetworkInterface in interfaces) {
                    val addresses: ArrayList<InetAddress> = Collections.list(networkInterface.inetAddresses)
                    for (address: InetAddress in addresses) {
                        if (!address.isLoopbackAddress && address is Inet4Address) {
                            return address.hostAddress ?: defaultIpAddress
                        }
                    }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        return defaultIpAddress
    }

    private fun getExternalIpAddress() {
        viewModelScope.launch(Dispatchers.IO) {
            val externalIpAddress: String = try {
                httpClient.get(urlString = EXTERNAL_IP_ADDRESS_URL)
                    .body<String>()
            } catch (e: Exception) {
                Timber.w(e)
                defaultIpAddress
            }

            _uiState.update { currentState ->
                currentState.copy(externalIpAddress = externalIpAddress, isLoading = false)
            }
        }
    }

    private companion object {

        const val EXTERNAL_IP_ADDRESS_URL = "https://api.ipify.org?format=text"
    }
}