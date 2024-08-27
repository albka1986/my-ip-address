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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.ponomarenko.myipadrress.R
import com.ponomarenko.myipadrress.ui.activity.ui.components.Item
import com.ponomarenko.myipadrress.ui.activity.ui.components.PrimaryButton
import com.ponomarenko.myipadrress.ui.activity.utils.DevicePreviews
import com.ponomarenko.myipadrress.ui.activity.utils.ThemePreviews
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen() {
    val viewmodel: MainAndroidViewModel = koinViewModel()
    val uiState: State<IPAddressState> = viewmodel.uiState.collectAsState()
    val mediumPadding: Dp = dimensionResource(R.dimen.padding_medium)

    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    if (locationPermissionsState.allPermissionsGranted) {
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
                value = uiState.value.ipAddress
            )

            Item(
                title = stringResource(R.string.network_type),
                value = uiState.value.networkType
            )

            Item(
                title = stringResource(R.string.network_name),
                value = uiState.value.networkName
            )

            Spacer(modifier = Modifier.weight(1f))

            PrimaryButton(
                onClick = { viewmodel.updateData() },
                text = stringResource(R.string.refresh_data)
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
                "Yay! Thanks for letting me access your approximate location. " +
                        "But you know what would be great? If you allow me to know where you " +
                        "exactly are. Thank you!"
            } else if (locationPermissionsState.shouldShowRationale) {
                // Both location permissions have been denied
                "Getting your exact location is important for this app. " +
                        "Please grant us fine location. Thank you :D"
            } else {
                // First time the user sees this feature or the user doesn't want to be asked again
                "This feature requires location permission"
            }

            val buttonText = if (!allPermissionsRevoked) {
                "Allow precise location"
            } else {
                "Request permissions"
            }

            Text(text = textToShow)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { locationPermissionsState.launchMultiplePermissionRequest() }) {
                Text(buttonText)
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