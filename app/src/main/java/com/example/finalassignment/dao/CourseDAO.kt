package com.example.finalassignment.dao

import androidx.room.*
import com.example.eknowledgelibrary.entities.CourseDetails

@Dao
interface CourseDAO {
    @Insert
    suspend fun insertCourses (courseDetails: CourseDetails)

    @Query("SELECT * FROM CourseDetails")
    suspend fun getAllCourses(): List<CourseDetails>

    @Query("DELETE FROM CourseDetails")
    suspend fun deleteAll()
}