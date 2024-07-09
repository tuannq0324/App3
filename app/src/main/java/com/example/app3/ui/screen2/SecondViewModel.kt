package com.example.app3.ui.screen2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.app3.database.MainRepository
import com.example.app3.ui.viewmodel.BaseViewModel

class SecondViewModelFactory(
    private val repository: MainRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SecondViewModel::class.java)) {
            return SecondViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class SecondViewModel(
    private val repository: MainRepository
) : BaseViewModel(repository)