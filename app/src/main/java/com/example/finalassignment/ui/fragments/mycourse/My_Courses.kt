package com.example.finalassignment.ui.fragments.mycourse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eknowledgelibrary.entities.CourseDetails
import com.example.finalassignment.R
import com.example.finalassignment.adapter.CourseDetailsAdapter
import com.example.finalassignment.database.eKnowledgeDB
import com.example.finalassignment.repository.CourseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

class My_Courses : Fragment() {
    private lateinit var recyclerview : RecyclerView
    private var courses = mutableListOf<CourseDetails>().asReversed()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my__courses, container, false)
        recyclerview = view.findViewById(R.id.recyclerview)

        runBlocking {
            deleteCourses().collect()
            loadCourses().collect()
            getCourses().collect{value -> courses = value.toMutableList().asReversed()}
        }
        val adapter = CourseDetailsAdapter(courses, requireContext())
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        return view
    }
    private fun deleteCourses(): Flow<String> = flow{
        eKnowledgeDB.getInstance(requireContext()).addCourseDAO().deleteAll()
        emit("Done")
    }
    private fun loadCourses(): Flow<String> = flow{
        val courseRepository = CourseRepository()
        val response = courseRepository.getMyCourses()
        response.data!!.forEach {
            eKnowledgeDB.getInstance(requireContext()).addCourseDAO().insertCourses(it)
            emit("Done")
        }
    }
    private fun getCourses() : Flow<List<CourseDetails>> = flow {
        val listCourses = eKnowledgeDB.getInstance(requireContext()).addCourseDAO().getAllCourses()
        emit(listCourses)
    }
}