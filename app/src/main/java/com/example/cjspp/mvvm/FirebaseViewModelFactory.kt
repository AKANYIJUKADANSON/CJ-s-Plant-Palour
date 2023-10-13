package com.example.cjspp.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException
import java.util.IllegalFormatException

class FirebaseViewModelFactory(private val repository:FirebaseViewModelRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return super.create(modelClass)
        if (modelClass.isAssignableFrom(FirebaseViewModel::class.java)) {
            return FirebaseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown view model class")
    }
}