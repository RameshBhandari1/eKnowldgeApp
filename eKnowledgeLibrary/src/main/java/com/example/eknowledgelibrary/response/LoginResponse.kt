package com.example.finalassignment.response

import com.example.eknowledgelibrary.entities.Users
data class LoginResponse (
    val success : Boolean? = null,
    val token : String? = null,
    val data : Users? = null
)