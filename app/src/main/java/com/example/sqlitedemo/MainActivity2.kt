package com.example.sqlitedemo

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity2 : AppCompatActivity() {
    private lateinit var dbHandler: DbHandler
    private lateinit var adapter: DataAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //  enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHandler = DbHandler(this)
        val recyclerView: RecyclerView = findViewById(R.id.rvData)
        val fabAdd: FloatingActionButton = findViewById(R.id.fabAdd)

        adapter = DataAdapter(mutableListOf(), this::deleteData, this::editData)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        fabAdd.setOnClickListener { showAddCourseDialog() }

        // Load courses into RecyclerView
        refreshCourses()
    }

    private fun refreshCourses() {
        val courses = dbHandler.getAllData()
        adapter.updateData(courses)
    }

    private fun showAddCourseDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.data_add_update_dialog, null)
        val etName = dialogView.findViewById<EditText>(R.id.etName)
        val etAge = dialogView.findViewById<EditText>(R.id.etAge)

        AlertDialog.Builder(this)
            .setTitle("add data")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name = etName.text.toString().trim()
                val age = etAge.text.toString().trim()

                if (name.isNotEmpty() && age.isNotEmpty()) {
                    val id = dbHandler.insertData(name, age)
                    if (id != -1L) {
                        Toast.makeText(this, "Course added", Toast.LENGTH_SHORT).show()
                        refreshCourses()
                    } else {
                        Toast.makeText(this, "Failed to add course", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Name and Duration are required", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()

    }

    private fun deleteData(id: Int) {
        val rowsDeleted = dbHandler.deleteCourse(id)
        if (rowsDeleted > 0) {
            Toast.makeText(this, "Course deleted", Toast.LENGTH_SHORT).show()
            refreshCourses()
        }
    }

    private fun editData(data: ModelData) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.data_add_update_dialog, null)
        val etName = dialogView.findViewById<EditText>(R.id.etName)
        val etAge = dialogView.findViewById<EditText>(R.id.etAge)

        etName.setText(data.name)
        etAge.setText(data.age)

        AlertDialog.Builder(this)
            .setTitle("update data")
            .setView(dialogView)
            .setPositiveButton("update") { _, _ ->
                val name = etName.text.toString().trim()
                val age = etAge.text.toString().trim()
                if (name.isNotEmpty() && age.isNotEmpty()) {
                    val rowsUpdated = dbHandler.updateData(
                        id = data.id,
                        name = name,
                        age = age
                    )
                    if (rowsUpdated > 0) {
                        Toast.makeText(this, "Course updated", Toast.LENGTH_SHORT).show()
                        refreshCourses()
                    } else {
                        Toast.makeText(this, "Failed to update course", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Name and Age are required", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }
}


