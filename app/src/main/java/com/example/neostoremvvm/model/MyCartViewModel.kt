package com.example.neostoremvvm.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.neostoremvvm.dataclass.CartData
import com.example.neostoremvvm.dataclass.CartDeleteResponse
import com.example.neostoremvvm.dataclass.CartEditResponse
import com.example.neostoremvvm.dataclass.CartListResponse
import com.example.neostoremvvm.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyCartViewModel constructor(private val repository: MainRepository)  : ViewModel() {

    val myCartLiveData = MutableLiveData<CartListResponse>()
    val cartEditLiveData = MutableLiveData<CartEditResponse>()
    val cartDeleteLiveData = MutableLiveData<CartDeleteResponse>()
    val cartLiveData = MutableLiveData<List<CartData>>()

    val errorMessage = MutableLiveData<String>()

    fun getCartList(accessToken:String ) {

        val response = repository.myCart(accessToken)
        response?.enqueue(object : Callback<CartListResponse> {
            override fun onResponse(
                call: Call<CartListResponse>,
                response: Response<CartListResponse>
            ) {
                myCartLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<CartListResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
    fun getCartEdit(access_token:String,product_id:Int?, quantity:Int?) {
        val response = repository.cartEdit(access_token,product_id,quantity)
        response?.enqueue(object : Callback<CartEditResponse> {
            override fun onResponse(
                call: Call<CartEditResponse>,
                response: Response<CartEditResponse>
            ) {
                cartEditLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<CartEditResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
    fun getCartDelete(access_token:String,product_id:Int?) {
        val response = repository.cartDelete(access_token,product_id)
        response?.enqueue(object : Callback<CartDeleteResponse> {
            override fun onResponse(
                call: Call<CartDeleteResponse>,
                response: Response<CartDeleteResponse>
            ) {
                cartDeleteLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<CartDeleteResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}