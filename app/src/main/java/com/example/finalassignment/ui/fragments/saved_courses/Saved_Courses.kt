package com.example.finalassignment.ui.fragments.saved_courses

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalassignment.R
import com.example.finalassignment.adapter.SavedCourseAdapter
import com.example.finalassignment.repository.SavedCoursesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Saved_Courses : Fragment() {
private lateinit var savedcourserecyclerview : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         val view = inflater.inflate(R.layout.fragment_saved__courses, container, false)

        savedcourserecyclerview = view.findViewById(R.id.savedcourserecyclerview)
        savedCourses()
        return view
    }
    private fun savedCourses(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val savedCoursesRepository = SavedCoursesRepository()
                val response = savedCoursesRepository.getSavedCourses()
                if (response.success == true){
                    withContext(Dispatchers.Main){
                        val listSavedCourse = response.data!!
                        savedcourserecyclerview.adapter = SavedCourseAdapter( requireContext(), listSavedCourse)
                        savedcourserecyclerview.layoutManager = LinearLayoutManager(context)
                    }
                }
            }catch(ex : Exception){
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "khai k error aayo aayo ${ex}", Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}