package com.example.finalassignment.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eknowledgelibrary.entities.CourseDetails
import com.example.finalassignment.R
import com.example.finalassignment.api.ServiceBuilder
import com.example.finalassignment.notification.NotificationChannels
import com.example.finalassignment.repository.CourseRepository
import com.example.finalassignment.response.UpdateCourseResponse
import com.example.finalassignment.ui.fragments.mycourse.CourseUpdate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CourseDetailsAdapter(
    val listcourse: MutableList<CourseDetails>,
    val context: Context,
): RecyclerView.Adapter<CourseDetailsAdapter.JobViewHolder>(){
    class JobViewHolder (view : View):RecyclerView.ViewHolder(view){
        val tvtitle : TextView
        val tvcategories : TextView
        val tvdesc : TextView
        val txtpostdate : TextView
        val btnCdelete: TextView
        val btnCupdate: TextView
        val tvFilename: TextView

        init {
            tvtitle = view.findViewById(R.id.tvtitle)
            tvcategories = view.findViewById(R.id.tvcategories)
            tvdesc = view.findViewById(R.id.tvdesc)
            txtpostdate = view.findViewById(R.id.txtpostdate)
            btnCdelete = view.findViewById(R.id.btnCdelete)
            btnCupdate = view.findViewById(R.id.btnCupdate)
            tvFilename = view.findViewById(R.id.tvFilename)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.course_detail_layout, parent, false)
        return JobViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val lstcourse = listcourse[position]
        holder.tvtitle.text = lstcourse.courseTitle
        holder.tvcategories.text = lstcourse.courseCategory
        holder.tvdesc.text = lstcourse.courseDesc
        holder.tvFilename.text = lstcourse.fileTitle
        holder.txtpostdate.text = "Post Date:${lstcourse.postDate}"
        holder.tvFilename.setOnClickListener {
            val file = lstcourse.courseFile.toString()
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(ServiceBuilder.loadImagePath()+file))
            context.startActivity(intent)
        }
        holder.btnCupdate.setOnClickListener{
            val intent = Intent(context, CourseUpdate::class.java)
            intent.putExtra("course", lstcourse)
            context.startActivity(intent)
        }
        holder.btnCdelete.setOnClickListener{
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete student")
            builder.setMessage("Are you sure you want to delete??")
            builder.setIcon(android.R.drawable.ic_delete)

            builder.setPositiveButton("Yes") { _, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val courseRepository = CourseRepository()
                        val response = courseRepository.deleteCourse(lstcourse._id!!)
                        if (response.success == true) {
                            withContext(Dispatchers.Main) {
                                showLowPriorityDeleteNotification()
                                Toast.makeText(
                                    context,
                                    "Course Deleted",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            withContext(Dispatchers.Main) {
                                listcourse.remove(lstcourse)
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
        return listcourse.size
    }
    private fun showLowPriorityDeleteNotification() {
        val notificationManager = NotificationManagerCompat.from(context)
        val notificationChannels = NotificationChannels(context)
        notificationChannels.createNotificationChannel()
        val notification = NotificationCompat.Builder(context,notificationChannels.CHANNEL_2)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle("Course Delete notification.")
            .setContentText("Course Deleted Successfully!!")
            .setColor(Color.BLUE)
            .build()
        notificationManager.notify(2, notification)
    }

}