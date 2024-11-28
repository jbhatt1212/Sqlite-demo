package com.example.sqlitedemo.db

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sqlitedemo.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeActivity : AppCompatActivity() {
    private lateinit var dbHandler: ItemDbHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        dbHandler = ItemDbHandler(this)
        getCountryList()
    }
        private fun getCountryList() {
            CoroutineScope(Dispatchers.IO).launch {

                try {
                    val data = ApiInstance.getService().getData()
                    val countriesList: List<Post> = data.posts
             withContext(Dispatchers.Main){
                 val  response = data
                 dbHandler.insertCountryData(countriesList)
                 dbHandler.updateBodyColumnFromApi(countriesList)


                 Log.e("success", "response : ${response.posts}")
             }
                }
                catch (e:Exception){
                    Log.e("error","message : ${e.message}")
                }
                }

        }

}