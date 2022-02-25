package com.example.neostoremvvm.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.neostoremvvm.dataclass.OrderListData
import com.example.neostoremvvm.dataclass.OrderListResponse
import com.example.neostoremvvm.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyOrderViewModel constructor(private val repository: MainRepository)  : ViewModel() {

    val orderList = MutableLiveData<List<OrderListData>>()
    val errorMessage = MutableLiveData<String>()

    fun getOrderlist( access_token:String ) {

        val response = repository.order( access_token)
        response?.enqueue(object : Callback<OrderListResponse> {
            override fun onResponse(
                call: Call<OrderListResponse>,
                response: Response<OrderListResponse>
            ) {
                orderList.postValue(response.body()?.data)
            }

            override fun onFailure(call: Call<OrderListResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}
