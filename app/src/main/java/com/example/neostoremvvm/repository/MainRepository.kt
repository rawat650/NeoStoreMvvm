package com.example.neostoremvvm.repository

import com.example.neostoremvvm.api.RetrofitService
import retrofit2.http.Field


class MainRepository(private val retrofitService: RetrofitService?) {

    fun productList(productCategoryId: String) = retrofitService?.productList(productCategoryId)
    fun productDetail(productId: String) = retrofitService?.productDetail(productId)
    fun register(first_name: String,last_name: String, email: String,password: String,
                 confirm_password: String,gender:String, phone_no: Long) = retrofitService?.createUser(first_name,last_name,email,password,confirm_password,gender,phone_no)
    fun login(email: String,password:String) = retrofitService?.loginUser(email,password)
    fun forgotPassword(email: String) = retrofitService?.forgotPassword(email)
    fun editProfile(accessToken: String,first_name :String,last_name: String,email:String,dob: String,phone_no : String,profile_pic:String) =
        retrofitService?.updateProfile(accessToken,first_name,last_name,email,dob,phone_no,profile_pic)
    fun fetchData(access_token : String) = retrofitService?.fetchAccountDetail(access_token)
    fun resetPassword(access_token : String,old_password:String,password:String,confirm_password: String) =
        retrofitService?.changePassword(access_token,old_password,password,confirm_password)
    fun rating(product_id:String,rating:Int?) = retrofitService?.ratingProduct(product_id,rating)
    fun addToCart(accessToken: String, productAddId:Int, quantity: Int) =
        retrofitService?.addToCart(accessToken,productAddId,quantity)
    fun myCart(access_token: String) = retrofitService?.cartList(access_token)
    fun cartEdit(access_token:String,product_id:Int?, quantity:Int?) = retrofitService?.editCartItem(access_token,product_id,quantity)
    fun cartDelete(access_token:String,product_id:Int?) = retrofitService?.deleteCartItem(access_token,product_id)
    fun order( access_token:String)=retrofitService?.orderList(access_token)
    fun detailOrder(access_token: String,order_id:Int?) = retrofitService?.orderDeatilList(access_token,order_id)

}