package com.example.cjspp.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cjspp.Login
import kotlinx.coroutines.launch

class FirebaseViewModel(private val repository:FirebaseViewModelRepository):ViewModel() {

    // Authentication | Login
    fun userLogin(activity:Login, userEmail:String, userPassword:String){
        viewModelScope.launch {
            repository.userLogin(activity, userEmail, userPassword)
        }
    }
}