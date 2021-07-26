package com.example.eknowledge_wear.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.eknowledge_wear.R
import com.example.finalassignment.api.ServiceBuilder
import com.example.finalassignment.repository.CourseRepository
import com.example.finalassignment.repository.SavedCoursesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WearDashboardActivity : AppCompatActivity() {

    private lateinit var tvwearfullname: TextView
    private lateinit var postcourses: TextView
    private lateinit var savedcourses: TextView
    private lateinit var tvwearcount: TextView
    private lateinit var tvwearcount2: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wear_dashboard)

        tvwearfullname = findViewById(R.id.tvwearfullname)
        postcourses = findViewById(R.id.postcourses)
        savedcourses = findViewById(R.id.savedcourses)
        tvwearcount = findViewById(R.id.tvwearcount)
        tvwearcount2 = findViewById(R.id.tvwearcount2)

        tvwearfullname.text = ServiceBuilder.username.toString()

        if(ServiceBuilder.usertype=="Learner"){
            postcourses.visibility = View.GONE
            tvwearcount.visibility = View.GONE
            CoroutineScope(Dispatchers.IO).launch {
                val savedCoursesRepository = SavedCoursesRepository()
                val courseResponse = savedCoursesRepository.getSavedCourses()
                withContext(Dispatchers.Main) {
                    val course = courseResponse.count!!
                    tvwearcount2.setText("Courses: ${course}")
                }
            }
        }
       else if(ServiceBuilder.usertype=="Professor"){
            tvwearcount2.visibility = View.GONE
            savedcourses.visibility = View.GONE
            CoroutineScope(Dispatchers.IO).launch {
                val wearCourseRepository = CourseRepository()
                val courseResponse = wearCourseRepository.getMyCourses()
                withContext(Dispatchers.Main) {
                    val course = courseResponse.count!!
                    tvwearcount.setText("Courses: ${course}")
                }
            }
       }
    }
}