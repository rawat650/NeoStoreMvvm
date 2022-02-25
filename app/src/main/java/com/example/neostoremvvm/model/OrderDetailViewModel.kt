package com.example.neostoremvvm.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.neostoremvvm.dataclass.OrderDetail
import com.example.neostoremvvm.dataclass.OrderDetailResponse
import com.example.neostoremvvm.repository.MainRepository
import retrofit2.Callback
import retrofit2.Response

class OrderDetailViewModel constructor(private val repository: MainRepository) : ViewModel() {

    val detailList = MutableLiveData<OrderDetailResponse>()
  //  val detailorder = MutableLiveData<List<OrderDetail>>()
    val errorMessage = MutableLiveData<String>()

    fun getDetailData( access_token:String,order_id:Int?) {

        val response = repository.detailOrder(access_token, order_id)
        response?.enqueue(object : Callback<OrderDetailResponse> {
            override fun onResponse(
                call: retrofit2.Call<OrderDetailResponse>,
                response: Response<OrderDetailResponse>
            ) {
                detailList.postValue(response.body())
            }


            override fun onFailure(call: retrofit2.Call<OrderDetailResponse>, t: Throwable) {
            }
        })
    }
}
