package com.example.finalassignment

import com.example.eknowledgelibrary.entities.CourseDetails
import com.example.finalassignment.api.ServiceBuilder
import com.example.finalassignment.repository.CourseRepository
import com.example.finalassignment.repository.SavedCoursesRepository
import com.example.finalassignment.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class eKnowledgeUnitTesting{

    private lateinit var userRepository: UserRepository
    private lateinit var savedCoursesRepository: SavedCoursesRepository
    private lateinit var courseRepository: CourseRepository

    // ===================================User Testing==============================================
    // Login Test
    @Test
    fun checkLogin() = runBlocking{
        userRepository = UserRepository()
        val response = userRepository.userLogin("kushal","kushal123")
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult,actualResult)
    }

    // get logedin User Profile data
    @Test
    fun ViewUser() = runBlocking {
        userRepository = UserRepository()
        ServiceBuilder.token = "Bearer " + userRepository.userLogin("unish","unish123").token
        val response = userRepository.getUser()
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult,actualResult)
    }

    // ================================Courses Testing==============================================
    // Get Courses only added by Logedin user
    @Test
    fun GetMyCourse() = runBlocking {
        userRepository = UserRepository()
        ServiceBuilder.token = "Bearer " + userRepository.userLogin("kushal","kushal123").token
        courseRepository = CourseRepository()
        val response = courseRepository.getMyCourses()
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult,actualResult)
    }

    // get saved courses
    @Test
    fun GetSavedCourse() = runBlocking {
        userRepository = UserRepository()
        ServiceBuilder.token = "Bearer " + userRepository.userLogin("unish","unish123").token
        savedCoursesRepository = SavedCoursesRepository()
        val response = savedCoursesRepository.getSavedCourses()
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult,actualResult)
    }

    //add course
    @Test
    fun AddCourse() = runBlocking {
        userRepository = UserRepository()
        ServiceBuilder.token = "Bearer " + userRepository.userLogin("kushal","kushal123").token
        courseRepository = CourseRepository()
        val course = CourseDetails(courseTitle = "Kotlin", courseCategory = "Android", courseDesc = "Develop Your own Application.", fileTitle = "Kotlin", courseFile = "kotlin.pdf")
        val response = courseRepository.addCourse(course)
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult,actualResult)
    }

    // Get Orders
    @Test
    fun UpdateCourse() = runBlocking {
        userRepository = UserRepository()
        ServiceBuilder.token = "Bearer " + userRepository.userLogin("kushal","kushal123").token
        courseRepository = CourseRepository()
        val updatedCourse = CourseDetails(courseTitle = "Assignment Brief", courseCategory = "WebDevelopment", courseDesc = "Detail about Assignment", fileTitle = "Assignment Guideline", courseFile = "1617639863484Assignment-Presenation-Guideline.pdf")
        val response = courseRepository.updateCourse("607c2247c2eea0187c04d3e9", updatedCourse)
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult,actualResult)
    }
}