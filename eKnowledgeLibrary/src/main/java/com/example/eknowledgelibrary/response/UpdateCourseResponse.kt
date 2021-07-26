package com.example.finalassignment.response

import com.example.eknowledgelibrary.entities.CourseDetails

data class UpdateCourseResponse(
    val success : Boolean? = null,
    val message : String? = null,
    val data : CourseDetails? = null
)