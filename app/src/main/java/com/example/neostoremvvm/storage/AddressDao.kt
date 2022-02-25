package com.example.neostoremvvm.storage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface AddressDao {

    @Insert
     suspend fun addAdress(data: Addressentity)

    @Query("select * From  address")
     suspend fun getData(): List<Addressentity>

    @Delete
    suspend fun deleteHistory(data:Addressentity)

}