package com.example.neostoremvvm.repository

import com.example.neostoremvvm.api.RetrofitService.Companion.retrofitService
import com.example.neostoremvvm.dataclass.AddressInfo
import com.example.neostoremvvm.storage.AddressDao
import com.example.neostoremvvm.storage.Addressentity

class AdressRepository(private val addressDao: AddressDao) {

    suspend fun getData():List<AddressInfo>{
        var list = mutableListOf<AddressInfo>()
        for (item in addressDao.getData()) {
            var addressInfo = AddressInfo(item.id,item.address,item.city,item.state,item.zipcode,item.country)
            list.add(addressInfo)
        }
       return list
}
    suspend fun insertAddress(data: AddressInfo) {
         val addressentity = Addressentity(data.id,data.address,data.city,data.state,data.zipcode,data.country)
             addressDao.addAdress(addressentity)
     }
    suspend fun deleteAdress(data: AddressInfo) {
       var addressentity = Addressentity(data.id,data.address,data.city,data.state,data.zipcode,data.country,)
       addressDao.deleteHistory(addressentity)
   }
    fun placeOrders(access_token:String,address: String) = retrofitService?.placeOrder(access_token,address)
}