package com.example.jsonapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import com.example.jsonapp.DB.DatabaseHandler
import com.example.jsonapp.List.ListAdapterComs
import com.example.jsonapp.Model.ComModel

class PostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        val databaseHandler = DatabaseHandler(this)
        val textTitle = findViewById<TextView>(R.id.postTitle)
        val textBody = findViewById<TextView>(R.id.postBody)
        val lvComs = findViewById<ListView>(R.id.lvComs)
        val buttonBack = findViewById<Button>(R.id.buttonBack)
        val buttonBackBack = findViewById<Button>(R.id.buttonBackBack)

        val title: String = getRecord()
        textTitle.text = title

        val id: Int = databaseHandler.getIdFromTitle(title)
        textBody.text = databaseHandler.getBodyFromId(id)

        viewRecordComs(lvComs, id)

        buttonBack.setOnClickListener {
            finish()
        }

        buttonBackBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    fun viewRecordComs(lv: ListView, id: Int){
        val databaseHandler = DatabaseHandler(this)
        val emp: List<ComModel> = databaseHandler.viewComs(id)
        val empArrayName = Array(emp.size){"null"}
        val empArrayEmail = Array(emp.size){"null"}
        val empArrayBody = Array(emp.size){"null"}
        for((index, e) in emp.withIndex()){
            empArrayName[index] = e.name
            empArrayEmail[index] = e.email
            empArrayBody[index] = e.body
        }
        val myListAdapter = ListAdapterComs(this,empArrayName,empArrayEmail,empArrayBody)
        lv.adapter = myListAdapter
    }

    fun getRecord():String{
        val sharedScore = this.getSharedPreferences("com.example.myapplication.shared",0)
        return sharedScore.getString("title", "Error").toString()
    }
}