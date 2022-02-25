package com.example.neostoremvvm.factoryclass

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.neostoremvvm.model.MyAccountViewModel
import com.example.neostoremvvm.repository.MainRepository

class MyAccountFactory constructor (private val repository: MainRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MyAccountViewModel::class.java)) {
            MyAccountViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}