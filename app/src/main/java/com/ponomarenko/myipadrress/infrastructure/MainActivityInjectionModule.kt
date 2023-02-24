package com.ponomarenko.myipadrress.infrastructure

import com.ponomarenko.myipadrress.ui.activity.ui.MainActivityAndroidViewModel
import com.ponomarenko.myipadrress.ui.activity.utils.NetworkManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object MainActivityInjectionModule {

    val module = module {
        viewModel {
            MainActivityAndroidViewModel(
                utils = NetworkManager
            )
        }
    }
}