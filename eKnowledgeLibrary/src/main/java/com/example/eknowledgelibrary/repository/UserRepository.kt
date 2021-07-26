package com.example.finalassignment.repository

import com.example.eknowledgelibrary.entities.Users
import com.example.finalassignment.api.ServiceBuilder
import com.example.finalassignment.api.UserAPI
import com.example.finalassignment.api.eKnowledgeAPIRequest
import com.example.finalassignment.response.*
import okhttp3.MultipartBody

class UserRepository
    :eKnowledgeAPIRequest(){
        private val userAPI = ServiceBuilder.buildService(UserAPI::class.java)

    //For Register user
    suspend fun userRegister(users : Users) : RegisterResponse{
        return apiRequest {
            userAPI.userRegister(users)
        }
    }

    //for user login
    suspend fun userLogin(username : String, password : String): LoginResponse{
        return apiRequest {
            userAPI.userVerification(username, password)
        }
    }

    //update user
    suspend fun updateUser(id : String, user : Users): UpdateUserResponse {
        return apiRequest {
            userAPI.updateUser(
                ServiceBuilder.token!!, id, user
            )
        }
    }

    //upload file
    suspend fun uploadProfile(id: String, body : MultipartBody.Part): ProfileResponse {
        return apiRequest {
            userAPI.uploadProfile(
                ServiceBuilder.token!!, id, body
            )
        }
    }

    //Get User data
    suspend fun getUser(): GetUserResponse{
        return apiRequest {
            userAPI.getUser(
                ServiceBuilder.token!!
            )
        }
    }
}