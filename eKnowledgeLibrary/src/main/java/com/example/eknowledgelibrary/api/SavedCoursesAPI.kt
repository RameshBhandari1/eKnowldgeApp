package com.example.finalassignment.api

import com.example.eknowledgelibrary.entities.SavedCourses
import com.example.finalassignment.response.DeleteResponse
import com.example.finalassignment.response.GetSavedCourseResponse
import com.example.finalassignment.response.SavedCoursesResponse
import retrofit2.Response
import retrofit2.http.*

interface SavedCoursesAPI {
    //Save Course
    @POST("course/save")
    suspend fun saveCourses(
        @Header("Authorization") token : String,
        @Body savedCourses: SavedCourses
    ): Response<SavedCoursesResponse>

    //view courses
    @GET("course/saved")
    suspend fun getSavedCourses(
        @Header("Authorization") token : String
    ):Response<GetSavedCourseResponse>

    //delete course
    @DELETE("course/save/delete/{id}")
    suspend fun deleteSavedCourse(
        @Header("Authorization") token : String,
        @Path("id") id : String
    ):Response<DeleteResponse>
}