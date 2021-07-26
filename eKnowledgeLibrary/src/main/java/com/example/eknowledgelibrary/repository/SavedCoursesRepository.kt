package com.example.finalassignment.repository

import com.example.eknowledgelibrary.entities.SavedCourses
import com.example.finalassignment.api.SavedCoursesAPI
import com.example.finalassignment.api.ServiceBuilder
import com.example.finalassignment.api.eKnowledgeAPIRequest
import com.example.finalassignment.response.DeleteResponse
import com.example.finalassignment.response.GetSavedCourseResponse
import com.example.finalassignment.response.SavedCoursesResponse

class SavedCoursesRepository :
    eKnowledgeAPIRequest() {
    private val SavedCoursesAPI =
        ServiceBuilder.buildService(SavedCoursesAPI::class.java)

    //Save Course
    suspend fun saveCourses(savedCourses: SavedCourses): SavedCoursesResponse {
        return apiRequest {
            SavedCoursesAPI.saveCourses(
                ServiceBuilder.token!!,savedCourses
            )
        }
    }

    //Delete course
    suspend fun deleteSavedCourse(id :String): DeleteResponse {
        return apiRequest {
            SavedCoursesAPI.deleteSavedCourse(
                ServiceBuilder.token!!, id
            )
        }
    }

            //view single course
    suspend fun getSavedCourses(): GetSavedCourseResponse {
        return apiRequest {
            SavedCoursesAPI.getSavedCourses(
                ServiceBuilder.token!!
            )
        }
    }
}