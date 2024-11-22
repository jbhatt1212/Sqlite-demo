package com.example.sqlitedemo

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.app.AlertDialog

import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DbHelper
    private lateinit var adapter: CourseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        dbHelper = DbHelper(this)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val fabAdd: FloatingActionButton = findViewById(R.id.fabAdd)

        // Setup RecyclerView and Adapter
        adapter = CourseAdapter(mutableListOf(), this::deleteCourse, this::editCourse)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Add course when FAB is clicked
        fabAdd.setOnClickListener { showAddCourseDialog() }

        // Load courses into RecyclerView
        refreshCourses()
    }
    private fun refreshCourses() {
        val courses = dbHelper.getAllCourses()
        adapter.updateCourses(courses)
    }

    private fun showAddCourseDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_course, null)
        val etName = dialogView.findViewById<EditText>(R.id.etCourseName)
        val etDuration = dialogView.findViewById<EditText>(R.id.etCourseDuration)
        val etTracks = dialogView.findViewById<EditText>(R.id.etCourseTracks)
        val etDescription = dialogView.findViewById<EditText>(R.id.etCourseDescription)

        AlertDialog.Builder(this)
            .setTitle("Add Course")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name = etName.text.toString().trim()
                val duration = etDuration.text.toString().trim()
                val tracks = etTracks.text.toString().trim()
                val description = etDescription.text.toString().trim()

                if (name.isNotEmpty() && duration.isNotEmpty()) {
                    val id = dbHelper.insertCourse(name, duration, tracks, description)
                    if (id != -1L) {
                        Toast.makeText(this, "Course added", Toast.LENGTH_SHORT).show()
                        refreshCourses()
                    } else {
                        Toast.makeText(this, "Failed to add course", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Name and Duration are required", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    private fun deleteCourse(courseId: Int) {
        val rowsDeleted = dbHelper.deleteCourse(courseId)
        if (rowsDeleted > 0) {
            Toast.makeText(this, "Course deleted", Toast.LENGTH_SHORT).show()
            refreshCourses()
        }
    }

    private fun editCourse(course: Course) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_course, null)
        val etName = dialogView.findViewById<EditText>(R.id.etCourseName)
        val etDuration = dialogView.findViewById<EditText>(R.id.etCourseDuration)
        val etTracks = dialogView.findViewById<EditText>(R.id.etCourseTracks)
        val etDescription = dialogView.findViewById<EditText>(R.id.etCourseDescription)

        // Populate fields with the current course details
        etName.setText(course.name)
        etDuration.setText(course.duration)
        etTracks.setText(course.tracks)
        etDescription.setText(course.description)

        AlertDialog.Builder(this)
            .setTitle("Edit Course")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val name = etName.text.toString().trim()
                val duration = etDuration.text.toString().trim()
                val tracks = etTracks.text.toString().trim()
                val description = etDescription.text.toString().trim()

                if (name.isNotEmpty() && duration.isNotEmpty()) {
                    val rowsUpdated = dbHelper.updateCourse(
                        id = course.id,
                        name = name,
                        duration = duration,
                        tracks = tracks,
                        description = description
                    )
                    if (rowsUpdated > 0) {
                        Toast.makeText(this, "Course updated", Toast.LENGTH_SHORT).show()
                        refreshCourses()
                    } else {
                        Toast.makeText(this, "Failed to update course", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Name and Duration are required", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }
}
