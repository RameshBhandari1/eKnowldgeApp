package com.example.finalassignment.api

import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException
import java.lang.StringBuilder

abstract class  eKnowledgeAPIRequest {
    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T{
        val response = call.invoke()
        //if api request was success
        if(response.isSuccessful){
            return response.body()!!
        }else{
            //if api request was not sucecss
            val error =response.errorBody()?.string()
            val message = StringBuilder()
            error?.let {
                try{
                    message.append(JSONObject(it).getString("success"))
                }catch (ex: JSONException){

                }
                message.append("\n")
            }
                message.append("Error code : ${response.code()}")
                throw  IOException(message.toString())
        }
    }
}