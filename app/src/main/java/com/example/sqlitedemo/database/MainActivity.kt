package com.example.sqlitedemo.database

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sqlitedemo.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    lateinit var dbHandler: DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  enableEdgeToEdge()
        setContentView(R.layout.activity_main3)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        dbHandler = DatabaseHelper.getInstance(this)!!
        getItemList()
    }
    private fun getItemList() {
        CoroutineScope(Dispatchers.IO).launch {

            try {
                val data = RetrofitInstance.getService().getItem()
                val itemList: List<Product> = data.products
                withContext(Dispatchers.Main){
                    val  response = data
                    dbHandler.insertItem(itemList)
                    Log.e("success", "response : ${response.products}")
                }
            }
            catch (e:Exception){
                Log.e("error","message : ${e.message}")
            }
        }

    }
}