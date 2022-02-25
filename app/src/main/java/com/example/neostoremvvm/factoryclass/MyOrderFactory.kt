package com.example.neostoremvvm.factoryclass

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.neostoremvvm.model.AddToCartViewModel
import com.example.neostoremvvm.model.MyOrderViewModel
import com.example.neostoremvvm.repository.MainRepository

class MyOrderFactory constructor(private val repository: MainRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MyOrderViewModel::class.java)) {
            MyOrderViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}