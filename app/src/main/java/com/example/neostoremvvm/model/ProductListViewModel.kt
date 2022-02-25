package com.example.neostoremvvm.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.neostoremvvm.dataclass.ProductData
import com.example.neostoremvvm.dataclass.ProductList
import com.example.neostoremvvm.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductListViewModel constructor(private val repository: MainRepository)  : ViewModel() {

    val productList = MutableLiveData<List<ProductData>>()
    val errorMessage = MutableLiveData<String>()

    fun getProductList(product_category_id:String ) {

        val response = repository.productList(product_category_id)
        response?.enqueue(object : Callback<ProductList>{
            override fun onResponse(
                call: Call<ProductList>,
                response: Response<ProductList>
            ) {
                productList.postValue(response.body()!!.data)
            }

            override fun onFailure(call: Call<ProductList>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}