package com.example.finalassignment.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.eknowledgelibrary.entities.CourseDetails
import com.example.finalassignment.R
import com.example.finalassignment.ui.CourseDetailsActivity
import com.example.finalassignment.ui.fragments.mycourse.CourseUpdate

class CourseResoursesAdapter(
    val context: Context,
    val listCourses : MutableList<CourseDetails>
):RecyclerView.Adapter<CourseResoursesAdapter.CourseResoursesViewHolder>() {
    class CourseResoursesViewHolder(view: View):RecyclerView.ViewHolder(view){
        val cardviewCourse: CardView
        val tvcourseheading: TextView

        init {
            cardviewCourse = view.findViewById(R.id.cradviewCourse)
            tvcourseheading = view.findViewById(R.id.tvcourseheading)
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CourseResoursesAdapter.CourseResoursesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.course_resourses, parent, false)
        return CourseResoursesViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: CourseResoursesAdapter.CourseResoursesViewHolder,
        position: Int
    ) {
        val listcourse = listCourses[position]
        holder.tvcourseheading.text = listcourse.courseTitle
        holder.cardviewCourse.setOnClickListener {
            val intent = Intent(context, CourseDetailsActivity::class.java)
            intent.putExtra("courseDetail", listcourse)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
       return listCourses.size
    }
}