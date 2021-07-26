package com.example.finalassignment.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalassignment.R
import com.example.finalassignment.adapter.CourseResoursesAdapter
import com.example.finalassignment.repository.CourseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class CoursesActivity : AppCompatActivity() {
    private lateinit var txtcetagory : TextView
    private lateinit var backbuttoncourse : ImageView
    private lateinit var courseResourseRecycler : RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_courses)

        txtcetagory = findViewById(R.id.txtcetagory)
        backbuttoncourse = findViewById(R.id.backbuttoncourse)
        courseResourseRecycler = findViewById(R.id.courseResourseRecycler)

        getCoursesResourses()
        backbuttoncourse.setOnClickListener {
            startActivity(Intent(this, Home_Menu_Activity::class.java))
        }


        val txt = intent.getStringExtra("cetagory")
        if (txt == "IOT") {
            txtcetagory.setText("Internet Pervasive Computing")
        }
        else if (txt == "Android"){
            txtcetagory.setText("Android Development")
        }
        else{
            txtcetagory.setText("Web Api Development")
        }
    }
    private fun getCoursesResourses(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val txt = intent.getStringExtra("cetagory")
                val courseRepository = CourseRepository()
                val response = courseRepository.getCourseByCategory(txt.toString())
                if(response.success == true){
                    val listCourses = response.data
                    withContext(Dispatchers.Main){
                        courseResourseRecycler.adapter = CourseResoursesAdapter( this@CoursesActivity, listCourses!!)
                        courseResourseRecycler.layoutManager = LinearLayoutManager(this@CoursesActivity)
                    }
                }

            }catch (ex: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@CoursesActivity,
                        "Error : ${ex}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}