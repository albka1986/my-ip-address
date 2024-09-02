package com.ponomarenko.myipadrress.ui.activity.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.ponomarenko.myipadrress.R
import com.ponomarenko.myipadrress.ui.activity.ui.components.Item
import com.ponomarenko.myipadrress.ui.activity.ui.components.NativeAdViewComposable
import com.ponomarenko.myipadrress.ui.activity.ui.components.PrimaryButton
import com.ponomarenko.myipadrress.ui.activity.utils.DevicePreviews
import com.ponomarenko.myipadrress.ui.activity.utils.ThemePreviews
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen() {
    val mediumPadding: Dp = dimensionResource(R.dimen.padding_medium)

    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    if (locationPermissionsState.allPermissionsGranted) {
        val viewmodel: MainAndroidViewModel = koinViewModel()
        val uiState: State<IPAddressState> = viewmodel.uiState.collectAsState()
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .safeDrawingPadding()
                .padding(mediumPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //            BannerAdView()
            NativeAdViewComposable()

            Item(
                title = stringResource(R.string.ip_address),
                value = uiState.value.internalIpAddress
            )

            Item(
                title = stringResource(R.string.network_type),
                value = uiState.value.networkType
            )

            Item(
                title = stringResource(R.string.network_name),
                value = uiState.value.networkName
            )

            Item(
                title = stringResource(R.string.external_ip_address),
                value = uiState.value.externalIpAddress
            )

            Spacer(modifier = Modifier.weight(1f))

            PrimaryButton(
                onClick = { viewmodel.updateData() },
                text = stringResource(R.string.refresh_data),
                isLoading = uiState.value.isLoading
            )
        }
    } else {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val allPermissionsRevoked =
                locationPermissionsState.permissions.size ==
                        locationPermissionsState.revokedPermissions.size

            val textToShow = if (!allPermissionsRevoked) {
                // If not all the permissions are revoked, it's because the user accepted the COARSE
                // location permission, but not the FINE one.
                stringResource(R.string.permissions_provided_partially)
            } else if (locationPermissionsState.shouldShowRationale) {
                // Both location permissions have been denied
                stringResource(R.string.force_to_provide_permissions)
            } else {
                // First time the user sees this feature or the user doesn't want to be asked again
                stringResource(R.string.request_permissions)
            }

            val buttonText = if (!allPermissionsRevoked) {
                stringResource(R.string.request_permissions_details_data)
            } else {
                stringResource(R.string.request_permissions_button)
            }

            Text(text = textToShow, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { locationPermissionsState.launchMultiplePermissionRequest() },
                Modifier.height(64.dp)
            ) {
                Text(text = buttonText, fontSize = 22.sp)
            }
        }
    }
}

@ThemePreviews
@DevicePreviews
@Composable
fun MainScreenPreview() {
    MainScreen()
}