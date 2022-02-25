package com.example.neostoremvvm.dataclass

data class ForgotPasswordResponse(
    val message: String,
    val status: Int,
    val user_msg: String
)