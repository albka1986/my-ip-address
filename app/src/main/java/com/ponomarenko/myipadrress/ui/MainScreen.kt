package com.ponomarenko.myipadrress.ui

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.ponomarenko.myipadrress.R
import com.ponomarenko.myipadrress.ui.components.BannerAdView
import com.ponomarenko.myipadrress.ui.components.Dialog
import com.ponomarenko.myipadrress.ui.components.Item
import com.ponomarenko.myipadrress.ui.components.PrimaryButton
import com.ponomarenko.myipadrress.utils.DevicePreviews
import com.ponomarenko.myipadrress.utils.ThemePreviews
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen() {
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    if (locationPermissionsState.allPermissionsGranted) {
        val viewmodel: MainAndroidViewModel = koinViewModel()
        val uiState: State<IPAddressState> = viewmodel.uiState.collectAsState()
        var showDialog by remember { mutableStateOf(false) }
        val mediumPadding: Dp = dimensionResource(R.dimen.padding_medium)

        LifecycleEventEffect(event = Lifecycle.Event.ON_RESUME) {
            viewmodel.updateData()
        }

        Column(
            modifier = Modifier
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .safeDrawingPadding()
                .padding(mediumPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Item(
                title = stringResource(R.string.ip_address),
                value = uiState.value.internalIpAddress
            )

            Item(
                title = stringResource(R.string.network_type),
                value = uiState.value.networkType
            )

            Box {
                Item(
                    title = stringResource(R.string.network_name),
                    value = uiState.value.networkName
                )

                if (!uiState.value.isLocationEnabled) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .padding(vertical = 16.dp)
                            .align(Alignment.Center)
                            .alpha(0.6f)
                            .clip(shape = RoundedCornerShape(8.dp))
                            .clickable { showDialog = true }
                            .background(Color.Black)
                    )
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .matchParentSize()
                            .padding(16.dp),
                        text = stringResource(R.string.gps_is_disabled),
                        style = typography.displaySmall,
                        color = colorScheme.onSecondary
                    )
                }
            }

            Item(
                title = stringResource(R.string.external_ip_address),
                value = uiState.value.externalIpAddress
            )

            BannerAdView()

            Spacer(modifier = Modifier.weight(1f))

            PrimaryButton(
                onClick = { viewmodel.updateData() },
                text = stringResource(R.string.refresh_data),
                isLoading = uiState.value.isLoading
            )

            if (showDialog) {
                val context = LocalContext.current

                Dialog(
                    onDismissRequest = {
                        showDialog = false
                    },
                    onConfirmation = {
                        context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                        showDialog = false
                    },
                    dialogTitle = stringResource(R.string.location_is_disabled),
                    dialogText = stringResource(R.string.location_is_disabled_description),
                )
            }
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

            Text(text = textToShow, style = typography.titleLarge)
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { locationPermissionsState.launchMultiplePermissionRequest() },
                Modifier
                    .heightIn(min = 64.dp)
                    .padding(16.dp)
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