package com.example.finalassignment.response

import com.example.eknowledgelibrary.entities.SavedCourses

data class GetSavedCourseResponse (
    val success : Boolean? = null,
    val count : Int? = null,
    val data : MutableList<SavedCourses>? = null
        )