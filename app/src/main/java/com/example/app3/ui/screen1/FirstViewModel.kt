package com.example.app3.ui.screen1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.app3.database.MainRepository
import com.example.app3.ui.viewmodel.BaseViewModel

class FirstViewModelFactory(
    private val repository: MainRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FirstViewModel::class.java)) {
            return FirstViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class FirstViewModel(
    private val repository: MainRepository
) : BaseViewModel(repository)