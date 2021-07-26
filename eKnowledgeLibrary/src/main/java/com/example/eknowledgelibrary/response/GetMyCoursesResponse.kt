package com.example.finalassignment.response

import com.example.eknowledgelibrary.entities.CourseDetails


data class GetMyCoursesResponse (
    val success : Boolean? = null,
    val count : Int? = null,
    val data : MutableList<CourseDetails>? = null
)