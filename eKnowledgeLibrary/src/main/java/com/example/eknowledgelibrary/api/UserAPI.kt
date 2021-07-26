package com.example.finalassignment.api

import com.example.eknowledgelibrary.entities.Users
import com.example.finalassignment.response.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserAPI {
    //for Register User
    @POST("user/insert")
    suspend fun userRegister(
        @Body users : Users
    ):Response<RegisterResponse>

    //for user login
    @FormUrlEncoded
    @POST("user/login")
    suspend fun userVerification(
        @Field("username") username : String,
        @Field("password") password : String
    ):Response<LoginResponse>

    //update user detail
    @PUT("user/update/{id}")
    suspend fun updateUser(
        @Header("Authorization") token : String,
        @Path("id") id: String,
        @Body user : Users
    ):Response<UpdateUserResponse>

    //profile upload
    @Multipart
    @PUT("user/photo/{id}")
    suspend fun uploadProfile(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Part file: MultipartBody.Part
    ): Response<ProfileResponse>

    //get user data
    @GET("user/getme")
    suspend fun getUser(
        @Header("Authorization") token : String
    ):Response<GetUserResponse>

}