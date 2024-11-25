package com.example.sqlitedemo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CourseAdapter(
    private val courses: MutableList<Course>,
    private val onDelete: (Int) -> Unit,
    private val onEdit: (Course) -> Unit
) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    class CourseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val courseName: TextView = view.findViewById(R.id.tvCourseName)
        val courseDuration: TextView = view.findViewById(R.id.tvCourseDuration)
        val courseTracks: TextView = view.findViewById(R.id.tvCourseTracks)
        val courseDescription: TextView = view.findViewById(R.id.tvCourseDescription)
        val btnDelete: TextView = view.findViewById(R.id.btnDelete)
        val btnEdit: TextView = view.findViewById(R.id.btnEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_course, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courses[position]
        holder.courseName.text = course.name
        holder.courseDuration.text = course.duration
        holder.courseTracks.text = course.tracks
        holder.courseDescription.text = course.description
        holder.btnDelete.setOnClickListener { onDelete(course.id) }
        holder.btnEdit.setOnClickListener { onEdit(course) }
    }

    override fun getItemCount(): Int = courses.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateCourses(newCourses: List<Course>) {
        courses.clear()
        courses.addAll(newCourses)
        notifyDataSetChanged()
    }
}