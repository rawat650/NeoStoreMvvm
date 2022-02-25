package com.example.neostoremvvm.factoryclass

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.neostoremvvm.model.ProductDetailViewModel
import com.example.neostoremvvm.repository.MainRepository

class ProductDetailViewmodelFactory constructor(private val repository: MainRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ProductDetailViewModel::class.java)) {
            ProductDetailViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}
