package com.example.eknowledgelibrary.entities

import android.os.Parcel
import android.os.Parcelable

//This is for API
data class Users(
    val _id : String? = null,
    val fullname : String? = null,
    val email : String? = null,
    val username : String? = null,
    val password : String? = null,
    val bio : String? = null,
    val phone : String? = null,
    val address : String? = null,
    val profile : String? = null,
    val accounttype : String? = null
)
