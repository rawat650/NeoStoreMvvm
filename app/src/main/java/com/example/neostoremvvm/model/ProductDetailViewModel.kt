package com.example.neostoremvvm.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.neostoremvvm.dataclass.ProductDataX
import com.example.neostoremvvm.dataclass.ProductDetail
import com.example.neostoremvvm.dataclass.ProductImage
import com.example.neostoremvvm.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailViewModel  constructor(private val repository: MainRepository)  : ViewModel() {

    val productDetailList = MutableLiveData<ProductDataX>()
    val productImageList = MutableLiveData<List<ProductImage>>()
    val errorMessage = MutableLiveData<String>()

    fun getProductDetailList(product_id:String ) {

        val response = repository.productDetail(product_id)
        response?.enqueue(object : Callback<ProductDetail> {
            override fun onResponse(
                call: Call<ProductDetail>,
                response: Response<ProductDetail>
            ) {
                productDetailList.postValue(response.body()?.data)
            }

            override fun onFailure(call: Call<ProductDetail>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}

