package com.example.neostoremvvm.model

import android.app.Application
import androidx.lifecycle.*
import com.example.neostoremvvm.dataclass.AddressInfo
import com.example.neostoremvvm.dataclass.PlaceOrderResponse
import com.example.neostoremvvm.repository.AdressRepository
import com.example.neostoremvvm.storage.AddressDao
import com.example.neostoremvvm.storage.AddressDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread


class AddressViewModel (application: Application) :AndroidViewModel(application) {
    val delete = MutableLiveData<Boolean>()
    val repository: AdressRepository
    val dao: AddressDao
    val allAddress = MutableLiveData<List<AddressInfo>>()
    val placeOrderLiveData = MutableLiveData<PlaceOrderResponse>()
    val errorMessage = MutableLiveData<String>()

    init {
        dao = AddressDb.getDatabase(application).addressDao()
        repository = AdressRepository(dao)
//        allAddress = repository.allAddress
    }

    fun getData() {

        viewModelScope.launch(Dispatchers.IO) {
            val get = repository.getData()
            allAddress.postValue(get)
        }
    }

    // on below line we are creating a new method for deleting a data. In this we are
    // calling a delete method from our repository to delete our note.
    fun deleteAdress(data: AddressInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAdress(data)
            delete.postValue(true)
        }
    }

    // on below line we are creating a new method for inserting  a note. In this we are
    // calling a insert method from our repository to update our note.
    fun insertAddress(data: AddressInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertAddress(data)
        }
    }
    fun getOrderPlace(access_token:String,address: String){
        val response = repository.placeOrders(access_token,address)
        response?.enqueue(object : Callback<PlaceOrderResponse> {
            override fun onResponse(
                call: Call<PlaceOrderResponse>,
                response: Response<PlaceOrderResponse>
            ) {
                placeOrderLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<PlaceOrderResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}

