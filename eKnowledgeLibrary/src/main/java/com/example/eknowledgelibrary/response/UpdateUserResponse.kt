package com.example.finalassignment.response

import com.example.eknowledgelibrary.entities.Users

data class UpdateUserResponse (
    val success : Boolean? = null,
    val message : String? = null,
    val data : Users? = null
)