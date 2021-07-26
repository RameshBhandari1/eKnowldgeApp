package com.example.finalassignment.api

import com.example.eknowledgelibrary.entities.CourseDetails
import com.example.finalassignment.response.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface CourseAPI {
    //Add Course
    @POST ("course/insert")
    suspend fun addCourse(
        @Header("Authorization") token : String,
        @Body courseDetails: CourseDetails
    ):Response<AddCourseResponse>

    //view courses
    @GET("course/data")
    suspend fun getMyCourses(
        @Header("Authorization") token : String
    ):Response<GetMyCoursesResponse>

    //view courses by category
    @GET("course/{courseCategory}")
    suspend fun getCourseByCategory(
        @Header("Authorization") token : String,
        @Path("courseCategory") courseCategory: String
    ):Response<GetMyCoursesResponse>

    //view courses by id
    @GET("course/single/{id}")
    suspend fun getCourseById(
        @Header("Authorization") token : String,
        @Path("id") id: String
    ):Response<GetSingleCourseResponse>

    //update course detail
    @PUT("course/update/{id}")
    suspend fun updateCourse(
        @Header("Authorization") token : String,
        @Path("id") id: String,
        @Body courseDetails: CourseDetails
    ):Response<UpdateCourseResponse>

    //delete course
    @DELETE("course/delete/{id}")
    suspend fun deleteCourse(
        @Header("Authorization") token : String,
        @Path("id") id : String
    ):Response<DeleteResponse>

    //upload file
    @Multipart
    @PUT("course/file/{id}")
    suspend fun uploadFile(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Part file: MultipartBody.Part
    ): Response<FileResponse>
}