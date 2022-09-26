package com.example.jsonapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.jsonapp.DB.DatabaseHandler
import com.example.jsonapp.List.ListAdapter
import com.example.jsonapp.Model.UserModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView = findViewById<ListView>(R.id.lv)

        viewRecord(listView)

        listView.onItemClickListener = AdapterView.OnItemClickListener {
                parent, _, position, _ ->
            val selectedItemText = parent.getItemAtPosition(position)
            val sharedScore = this.getSharedPreferences("com.example.myapplication.shared",Context.MODE_PRIVATE)
            val edit = sharedScore.edit()
            edit.putString("name", selectedItemText.toString())
            edit.apply()
            val intent = Intent(this, UserInfoActivity::class.java)
            startActivity(intent)
        }
    }

    fun viewRecord(lv: ListView){
        val databaseHandler = DatabaseHandler(this)
        val emp: List<UserModel> = databaseHandler.viewUser()
        val empArrayName = Array(emp.size){"null"}
        val empArrayEmail = Array(emp.size){"null"}
        val empArrayNumber = Array(emp.size){"null"}
        val empArrayPost = Array(emp.size){"null"}
        for((index, e) in emp.withIndex()){
            empArrayName[index] = e.name
            empArrayEmail[index] = e.email
            empArrayNumber[index] = databaseHandler.countCompletedTodos(e.id).toString()+"/"+databaseHandler.countTodos(e.id).toString()
            empArrayPost[index] = databaseHandler.countPosts(e.id).toString()
        }
        val myListAdapter = ListAdapter(this,empArrayName,empArrayEmail,empArrayNumber,empArrayPost)
        lv.adapter = myListAdapter
    }
}