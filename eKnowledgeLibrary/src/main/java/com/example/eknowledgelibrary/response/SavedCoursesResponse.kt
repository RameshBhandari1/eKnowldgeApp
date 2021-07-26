package com.example.finalassignment.response

import com.example.eknowledgelibrary.entities.SavedCourses

data class SavedCoursesResponse (
    val success : Boolean? = null,
    val coursePostMessage : String? = null,
    val data : SavedCourses? = null
        )