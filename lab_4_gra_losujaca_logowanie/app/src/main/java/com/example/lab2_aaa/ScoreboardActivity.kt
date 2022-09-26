package com.example.lab2_aaa

import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.lab2_aaa.DB.List.ListAdapter
import com.example.lab2_aaa.DB.Model.EmpModelClass


class ScoreboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scoreboard)

        val table2 = findViewById<ListView>(R.id.listScore)
        val backButton = findViewById<Button>(R.id.buttonBack)
        viewRecord(table2)

        backButton.setOnClickListener {
            finish()
        }
    }

    fun viewRecord(lv: ListView){
        val databaseHandler = DatabaseHandler(this)
        val emp: List<EmpModelClass> = databaseHandler.viewPlayer()
        val empArrayLogin = Array(emp.size){"null"}
        val empArrayScore = Array(emp.size){"0"}
        for((index, e) in emp.withIndex()){
            empArrayLogin[index] = e.login
            empArrayScore[index] = e.score.toString()
        }
        val myListAdapter = ListAdapter(this,empArrayLogin,empArrayScore)
        lv.adapter = myListAdapter
    }
}