package com.ponomarenko.myipadrress.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ponomarenko.myipadrress.ui.MainViewModel

class MainViewModelfFactory(private val repository: IPAddressRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }
}