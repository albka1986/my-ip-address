package com.ponomarenko.myipadrress.ui.activity.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ponomarenko.myipadrress.R
import com.ponomarenko.myipadrress.ui.activity.ui.components.Item
import com.ponomarenko.myipadrress.ui.activity.ui.components.PrimaryButton
import com.ponomarenko.myipadrress.ui.activity.utils.DevicePreviews
import com.ponomarenko.myipadrress.ui.activity.utils.ThemePreviews

@Composable
fun MainScreen(
    modifier: Modifier,
    viewModel: MainViewModel = viewModel()
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .padding(horizontal = 42.dp),
    ) {
        Item(title = stringResource(R.string.ip_address), value = "192.168.0.1")

        Item(title = stringResource(R.string.network_type), value = "192.168.0.1")

        Item(title = stringResource(R.string.network_name), value = "192.168.0.1")

        Spacer(modifier = Modifier.weight(1f))

        PrimaryButton(
            onClick = { viewModel.onRefreshClicked() },
            text = stringResource(R.string.refresh_data)
        )
    }
}

@ThemePreviews
@DevicePreviews
@Composable
fun MainScreenPreview() {
    MainScreen(Modifier)
}