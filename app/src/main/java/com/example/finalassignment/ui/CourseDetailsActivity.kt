package com.example.finalassignment.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.eknowledgelibrary.entities.CourseDetails
import com.example.eknowledgelibrary.entities.SavedCourses
import com.example.finalassignment.R
import com.example.finalassignment.api.ServiceBuilder
import com.example.finalassignment.repository.SavedCoursesRepository
import com.example.finalassignment.ui.fragments.mycourse.CourseUpdate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CourseDetailsActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var backbuttonDetail : ImageView
    private lateinit var tvCourseTitle : TextView
    private lateinit var tvCourseDesc : TextView
    private lateinit var tvFileTitle : TextView
    private lateinit var tvPost : TextView
    private lateinit var tvSavebtn : TextView
    lateinit var text : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_details)

        backbuttonDetail = findViewById(R.id.backbuttonDetail)
        tvCourseTitle = findViewById(R.id.tvCourseTitle)
        tvCourseDesc = findViewById(R.id.tvCourseDesc)
        tvFileTitle = findViewById(R.id.tvFileTitle)
        tvPost = findViewById(R.id.tvPost)
        tvSavebtn = findViewById(R.id.tvSavebtn)
        CourseDetail()
        intentcourse()
        if (ServiceBuilder.usertype == "Professor"){
            tvSavebtn.visibility = View.GONE
        }

        backbuttonDetail.setOnClickListener(this)
        tvFileTitle.setOnClickListener(this)
        tvSavebtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.backbuttonDetail -> {
                val intent = Intent(this, CoursesActivity::class.java)
                intent.putExtra("cetagory", text)
                startActivity(intent)
            }
            R.id.tvSavebtn ->{
                savedCourses()
            }
            R.id.tvFileTitle ->{
                fileOpen()
            }
        }
    }
    private fun CourseDetail(){
        val course = intent.getParcelableExtra<CourseDetails>("courseDetail")

        tvCourseTitle.text = course?.courseTitle.toString()
        tvCourseDesc.text = course?.courseDesc.toString()
        tvFileTitle.text = course?.fileTitle.toString()
        tvPost.text = "Post: ${course?.postDate.toString()}"

    }
    private fun intentcourse(){
        val course = intent.getParcelableExtra<CourseDetails>("courseDetail")
        text = course?.courseCategory.toString()
    }
    private fun fileOpen(){
        val course = intent.getParcelableExtra<CourseDetails>("courseDetail")
        val file = course!!.courseFile.toString()
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(ServiceBuilder.loadImagePath()+file))
        startActivity(intent)

    }
    private fun savedCourses(){
        val course = intent.getParcelableExtra<CourseDetails>("courseDetail")
        val courseID = course!!._id
        val savecourses = SavedCourses(CourseID = courseID )
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val savedCoursesRepository = SavedCoursesRepository()
                val response = savedCoursesRepository.saveCourses(savecourses)
                if (response.success == true){
                    withContext(Dispatchers.Main){
                    Toast.makeText(
                        this@CourseDetailsActivity,
                        "Course saved!!", Toast.LENGTH_SHORT
                    ).show()
                }
                }
            }catch(ex : Exception){
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@CourseDetailsActivity,
                        ex.toString(), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}