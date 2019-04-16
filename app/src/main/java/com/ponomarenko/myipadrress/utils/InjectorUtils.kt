package com.ponomarenko.myipadrress.utils

import com.ponomarenko.myipadrress.data.IPAddressRepository
import com.ponomarenko.myipadrress.data.MainViewModelfFactory

object InjectorUtils {
    fun provideMainViewModelFactory(): MainViewModelfFactory {
        return MainViewModelfFactory(IPAddressRepository)
    }
}