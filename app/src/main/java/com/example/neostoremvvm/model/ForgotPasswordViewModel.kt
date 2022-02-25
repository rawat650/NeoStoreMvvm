package com.example.neostoremvvm.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.neostoremvvm.dataclass.DefaultResponse
import com.example.neostoremvvm.dataclass.ForgotPasswordResponse
import com.example.neostoremvvm.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordViewModel constructor(private val repository: MainRepository)  : ViewModel() {

    val forgotPassordLiveData = MutableLiveData<ForgotPasswordResponse>()
    val errorMessage = MutableLiveData<String>()

    fun getForgotPassword(email: String) {
        val response = repository.forgotPassword(email)
        response?.enqueue(object : Callback<ForgotPasswordResponse> {
            override fun onResponse(
                call: Call<ForgotPasswordResponse>,
                response: Response<ForgotPasswordResponse>
            ) {
                if(response.body() != null && response.body()?.status == 200) {
                    forgotPassordLiveData.postValue(response.body())
                }else{
                    errorMessage.postValue("Wrong EmailId")
                }
            }

            override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}