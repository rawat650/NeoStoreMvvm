package com.example.neostoremvvm.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.neostoremvvm.dataclass.AddToCartResponse
import com.example.neostoremvvm.dataclass.RatingResponse
import com.example.neostoremvvm.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RatingViewModel constructor(private val repository: MainRepository)  : ViewModel()  {

    val ratingViewModel = MutableLiveData<RatingResponse>()
    val errorMessage = MutableLiveData<String>()

    fun getRate(product_id:String,rating:Int?) {

        val response = repository.rating(product_id, rating)
        response?.enqueue(object : Callback<RatingResponse> {
            override fun onResponse(
                call: Call<RatingResponse>,
                response: Response<RatingResponse>
            ) {
                ratingViewModel.postValue(response.body())
            }

            override fun onFailure(call: Call<RatingResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }

        })
    }
}