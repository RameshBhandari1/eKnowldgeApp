package com.example.finalassignment.response

import com.example.eknowledgelibrary.entities.CourseDetails

data class AddCourseResponse (
    val success : Boolean? = null,
    val data : CourseDetails? = null
        )