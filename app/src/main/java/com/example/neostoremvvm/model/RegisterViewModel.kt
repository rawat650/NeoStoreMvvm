package com.example.neostoremvvm.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.neostoremvvm.dataclass.DefaultResponse
import com.example.neostoremvvm.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel constructor(private val repository: MainRepository)  : ViewModel() {

    val registerLiveData = MutableLiveData<DefaultResponse?>()
    val errorMessage = MutableLiveData<String>()

    fun getRegister(first_name: String,last_name: String, email: String,password: String,confirm_password: String,gender:String,
    phone_no: Long) {
        val response = repository.register(first_name,last_name,email,password,confirm_password,gender,phone_no)
        response?.enqueue(object : Callback<DefaultResponse?> {
            override fun onResponse(
                call: Call<DefaultResponse?>,
                response: Response<DefaultResponse?>
            ) {
                if(response.body() != null && response.body()?.status == 200) {
                    registerLiveData.postValue(response.body())
                }else{
                    errorMessage.postValue("Unscuccessful")
                }
            }

            override fun onFailure(call: Call<DefaultResponse?>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}
