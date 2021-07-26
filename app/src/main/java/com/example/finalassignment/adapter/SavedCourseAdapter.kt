package com.example.finalassignment.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.eknowledgelibrary.entities.SavedCourses
import com.example.finalassignment.R
import com.example.finalassignment.notification.NotificationChannels
import com.example.finalassignment.repository.CourseRepository
import com.example.finalassignment.repository.SavedCoursesRepository
import com.example.finalassignment.ui.CourseDetailsActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class SavedCourseAdapter(
    val context: Context,
    val listSavedCourse : MutableList<SavedCourses>,

    ):RecyclerView.Adapter<SavedCourseAdapter.SavedCourseViewHolder>() {
    class SavedCourseViewHolder(view:View): RecyclerView.ViewHolder(view){
        val tvsavedcourseheading: TextView
        val btnSavedupdate: TextView
        val btnSaveddelete: TextView

        init {
            tvsavedcourseheading = view.findViewById(R.id.tvsavedcourseheading)
            btnSavedupdate = view.findViewById(R.id.btnSavedupdate)
            btnSaveddelete = view.findViewById(R.id.btnSaveddelete)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedCourseViewHolder {
       val view = LayoutInflater.from(parent.context)
           .inflate(R.layout.saved_courses_layout, parent, false)
        return SavedCourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: SavedCourseViewHolder, position: Int) {
        val listsave = listSavedCourse[position]
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val courseRepository = CourseRepository()
                val response = courseRepository.getCourseById(listsave.CourseID!!)
                if(response.success == true){
                    val course = response.data!!
                    withContext(Dispatchers.Main) {
                        holder.tvsavedcourseheading.text = course.courseTitle
                        holder.btnSavedupdate.setOnClickListener {
                            val intent = Intent(context, CourseDetailsActivity::class.java)
                               intent.putExtra("courseDetail", course)
                            context.startActivity(intent)
                        }
                    }
                }
            }catch (ex: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(context,
                        "Error : ${ex}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        holder.btnSaveddelete.setOnClickListener{
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete student")
            builder.setMessage("Are you sure you want to delete??")
            builder.setIcon(android.R.drawable.ic_delete)

            builder.setPositiveButton("Yes") { _, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val savedCoursesRepository = SavedCoursesRepository()
                        val response = savedCoursesRepository.deleteSavedCourse(listsave._id!!)
                        if (response.success == true) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    context,
                                    "Saved Course Deleted",
                                    Toast.LENGTH_SHORT
                                ).show()
                                showLowPriorityDeleteNotification()
                            }
                            withContext(Dispatchers.Main) {
                                listSavedCourse.remove(listsave)
                                notifyDataSetChanged()
                            }
                        }
                    } catch (ex: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                context,
                                ex.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            builder.setNegativeButton("No") { _, _ ->
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }
    override fun getItemCount(): Int {
        return listSavedCourse.size
    }
    private fun showLowPriorityDeleteNotification() {
        val notificationManager = NotificationManagerCompat.from(context)
        val notificationChannels = NotificationChannels(context)
        notificationChannels.createNotificationChannel()
        val notification = NotificationCompat.Builder(context,notificationChannels.CHANNEL_2)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle("Course Delete notification.")
            .setContentText("SaveCourse Deleted Successfully!!")
            .setColor(Color.BLUE)
            .build()
        notificationManager.notify(2, notification)
    }
}