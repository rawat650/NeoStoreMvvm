package com.example.neostoremvvm.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.neostoremvvm.dataclass.FetchAccountDetail
import com.example.neostoremvvm.dataclass.UpdateProfileClass
import com.example.neostoremvvm.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileViewModel constructor(private val repository: MainRepository)  : ViewModel()  {

    val profile = MutableLiveData<UpdateProfileClass>()
    val fetchDetailData = MutableLiveData<FetchAccountDetail>()
    val errorMessage = MutableLiveData<String>()

    fun getProfile(accessToken: String,first_name :String,last_name: String,email:String,dob: String,
                   phone_no : String,profile_pic:String) {
        val response = repository.editProfile(accessToken,first_name,last_name,email,dob,phone_no,profile_pic)
        response?.enqueue(object : retrofit2.Callback<UpdateProfileClass> {
            override fun onResponse(
                call: Call<UpdateProfileClass>,
                response: Response<UpdateProfileClass>
            ) {
                profile.postValue(response.body())
            }
            override fun onFailure(call: Call<UpdateProfileClass>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun getData(access_token : String){
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