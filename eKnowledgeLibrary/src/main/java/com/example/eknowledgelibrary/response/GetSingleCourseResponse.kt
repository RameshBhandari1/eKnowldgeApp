package com.example.finalassignment.response

import com.example.eknowledgelibrary.entities.CourseDetails

data class GetSingleCourseResponse (
    val success : Boolean? = null,
    val data : CourseDetails? = null
)