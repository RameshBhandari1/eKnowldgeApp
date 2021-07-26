package com.example.finalassignment.response

import com.example.eknowledgelibrary.entities.Users

data class GetUserResponse (
    val success : Boolean? = null,
    val data : Users? = null
)