package com.example.neostoremvvm.factoryclass

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.neostoremvvm.model.ForgotPasswordViewModel
import com.example.neostoremvvm.model.RegisterViewModel
import com.example.neostoremvvm.repository.MainRepository

class ForgotPasswordFactory constructor(private val repository: MainRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ForgotPasswordViewModel::class.java)) {
            ForgotPasswordViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}