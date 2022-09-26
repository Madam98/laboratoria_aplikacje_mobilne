package com.example.jsonapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import com.example.jsonapp.DB.DatabaseHandler
import com.example.jsonapp.List.ListAdapterPostTitles
import com.example.jsonapp.List.ListAdapterTodos
import com.example.jsonapp.Model.PostModel
import com.example.jsonapp.Model.TodosModel

class UserInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        val databaseHandler = DatabaseHandler(this)
        val textUser = findViewById<TextView>(R.id.user)
        val lvTodos = findViewById<ListView>(R.id.lv1)
        val lvPosts = findViewById<ListView>(R.id.lv2)
        val buttonBack = findViewById<Button>(R.id.buttonBack)

        val name: String = getRecord()

        textUser.text = name

        val id: Int = databaseHandler.getIdFromName(name)

        viewRecordTodos(lvTodos, id)
        viewRecordPosts(lvPosts, id)

        buttonBack.setOnClickListener {
            finish()
        }

        lvPosts.onItemClickListener = AdapterView.OnItemClickListener {
                parent, _, position, _ ->
            val selectedItemText = parent.getItemAtPosition(position)
            val sharedScore = this.getSharedPreferences("com.example.myapplication.shared", Context.MODE_PRIVATE)
            val edit = sharedScore.edit()
            edit.putString("title", selectedItemText.toString())
            edit.apply()
            val intent = Intent(this, PostActivity::class.java)
            startActivity(intent)
        }
    }

    fun viewRecordTodos(lv: ListView, id: Int){
        val databaseHandler = DatabaseHandler(this)
        val emp: List<TodosModel> = databaseHandler.viewTodos(id)
        val empArrayTitle = Array(emp.size){"null"}
        val empArrayCompleted = Array(emp.size){"null"}
        for((index, e) in emp.withIndex()){
            empArrayTitle[index] = e.title
            empArrayCompleted[index] = e.completed
        }
        val myListAdapter = ListAdapterTodos(this,empArrayTitle,empArrayCompleted)
        lv.adapter = myListAdapter
    }

    fun viewRecordPosts(lv: ListView, id: Int){
        val databaseHandler = DatabaseHandler(this)
        val emp: List<PostModel> = databaseHandler.viewPosts(id)
        val empArrayTitle = Array(emp.size){"null"}
        for((index, e) in emp.withIndex()){
            empArrayTitle[index] = e.title
        }
        val myListAdapter = ListAdapterPostTitles(this,empArrayTitle)
        lv.adapter = myListAdapter
    }

    fun getRecord():String{
        val sharedScore = this.getSharedPreferences("com.example.myapplication.shared",0)
        return sharedScore.getString("name", "Error").toString()
    }
}