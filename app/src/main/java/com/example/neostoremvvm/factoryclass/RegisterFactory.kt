package com.example.neostoremvvm.factoryclass

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.neostoremvvm.model.RegisterViewModel
import com.example.neostoremvvm.repository.MainRepository

class RegisterFactory constructor(private val repository: MainRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            RegisterViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}