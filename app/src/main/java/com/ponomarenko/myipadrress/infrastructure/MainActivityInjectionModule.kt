package com.ponomarenko.myipadrress.infrastructure

import com.ponomarenko.myipadrress.ui.activity.ui.MainAndroidViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object MainActivityInjectionModule {

    val module = module {

        viewModel {
            MainAndroidViewModel(application = get())
        }

    }
}
