package com.example.neostoremvvm.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.neostoremvvm.dataclass.AddToCartResponse
import com.example.neostoremvvm.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddToCartViewModel constructor(private val repository: MainRepository)  : ViewModel()  {

    val addToCartResponse = MutableLiveData<AddToCartResponse>()
    val errorMessage = MutableLiveData<String>()

    fun getAddToCart(acessToken: String,productAddId: Int,quantity: Int) {

        val response = repository.addToCart(acessToken,productAddId,quantity)
        response?.enqueue(object : Callback<AddToCartResponse> {
            override fun onResponse(
                call: Call<AddToCartResponse>,
                response: Response<AddToCartResponse>
            ) {
                addToCartResponse.postValue(response.body())
            }

            override fun onFailure(call: Call<AddToCartResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })

    }
}