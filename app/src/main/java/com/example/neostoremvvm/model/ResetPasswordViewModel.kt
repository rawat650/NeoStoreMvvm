package com.example.neostoremvvm.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.neostoremvvm.dataclass.UpdateProfileClass
import com.example.neostoremvvm.repository.MainRepository
import retrofit2.Call
import retrofit2.Response

class ResetPasswordViewModel constructor(private val repository: MainRepository)  : ViewModel()  {

    val resetPass = MutableLiveData<UpdateProfileClass>()
    val errorMessage = MutableLiveData<String>()

    fun getPassword(access_token : String,old_password:String,password:String,confirm_password: String) {
        val response = repository.resetPassword(access_token,old_password,password,confirm_password)
        response?.enqueue(object : retrofit2.Callback<UpdateProfileClass> {
            override fun onResponse(
                call: Call<UpdateProfileClass>,
                response: Response<UpdateProfileClass>
            ) {
                resetPass.postValue(response.body())
            }
            override fun onFailure(call: Call<UpdateProfileClass>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}