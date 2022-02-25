package com.example.neostoremvvm.model

import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.neostoremvvm.activities.HomeActivity
import com.example.neostoremvvm.dataclass.DefaultResponse
import com.example.neostoremvvm.dataclass.FetchAccountDetail
import com.example.neostoremvvm.repository.MainRepository
import com.example.neostoremvvm.storage.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginViewModel  constructor(private val repository: MainRepository)  : ViewModel() {

    val loginLiveData = MutableLiveData<DefaultResponse>()
    val fetchDetailData = MutableLiveData<FetchAccountDetail>()
    val errorMessage = MutableLiveData<String>()

    fun getlogin(email: String,password:String){
        val response = repository.login(email,password)
        response?.enqueue(object : Callback<DefaultResponse>{
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                if(response.body() != null && response.body()?.status == 200) {
                    loginLiveData.postValue(response.body())
                }else{
                    errorMessage.postValue("Unscuccessful")
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
    fun getUpdateData(access_token : String){
        val response = repository.fetchData(access_token)
        response?.enqueue(object : Callback<FetchAccountDetail>{
            override fun onResponse(
                call: Call<FetchAccountDetail>,
                response: Response<FetchAccountDetail>
            ) {
                fetchDetailData.postValue(response.body())
            }

            override fun onFailure(call: Call<FetchAccountDetail>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}
