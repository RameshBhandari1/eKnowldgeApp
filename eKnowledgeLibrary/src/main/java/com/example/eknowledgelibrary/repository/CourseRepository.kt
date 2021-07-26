package com.example.finalassignment.repository

import com.example.eknowledgelibrary.entities.CourseDetails
import com.example.finalassignment.api.CourseAPI
import com.example.finalassignment.api.ServiceBuilder
import com.example.finalassignment.api.eKnowledgeAPIRequest
import com.example.finalassignment.response.*
import okhttp3.MultipartBody

class CourseRepository :
    eKnowledgeAPIRequest() {
    private val CourseAPI =
        ServiceBuilder.buildService(CourseAPI::class.java)
    //Add Course
    suspend fun addCourse(courseDetails: CourseDetails): AddCourseResponse{
        return apiRequest {
            CourseAPI.addCourse(
                ServiceBuilder.token!!,courseDetails
            )
        }
    }
    //view courses
    suspend fun getMyCourses():GetMyCoursesResponse{
        return apiRequest {
            CourseAPI.getMyCourses(
                ServiceBuilder.token!!
            )
        }
    }
    //get Course By Category
    //view courses
    suspend fun getCourseByCategory(courseCategory : String):GetMyCoursesResponse{
        return apiRequest {
            CourseAPI.getCourseByCategory(
                ServiceBuilder.token!!, courseCategory
            )
        }
    }

    //get Course By ID
    //view courses
    suspend fun getCourseById(id : String):GetSingleCourseResponse{
        return apiRequest {
            CourseAPI.getCourseById(
                ServiceBuilder.token!!, id
            )
        }
    }

    //update course
    suspend fun updateCourse(id : String, courseDetails: CourseDetails): UpdateCourseResponse {
        return apiRequest {
            CourseAPI.updateCourse(
                ServiceBuilder.token!!, id, courseDetails
            )
        }
    }

    //Delete course
    suspend fun deleteCourse(id :String): DeleteResponse{
        return apiRequest {
            CourseAPI.deleteCourse(
                ServiceBuilder.token!!,id
            )
        }
    }

    //upload file
    suspend fun uploadFile(id: String, body : MultipartBody.Part):FileResponse{
        return apiRequest {
            CourseAPI.uploadFile(
                ServiceBuilder.token!!, id, body
            )
        }
    }
}