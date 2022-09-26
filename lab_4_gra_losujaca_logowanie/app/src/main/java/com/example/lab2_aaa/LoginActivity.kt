package com.example.lab2_aaa

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.lab2_aaa.DB.Model.EmpModelClass
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val editLogin = findViewById<EditText>(R.id.editTextLogin)
        val editPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val buttonScore = findViewById<Button>(R.id.buttonScore)
        val builder = AlertDialog.Builder(this)

        buttonLogin.setOnClickListener {
            if(editLogin.text.isEmpty() || editPassword.text.isEmpty()){
                builder.setTitle("Error")
                builder.setMessage("Provide login and password")
                builder.setPositiveButton("OK", null)
                builder.show()
                editPassword.text.clear()
                editLogin.text.clear()
            }
            else{
                val login = editLogin.text.toString()
                val password = editPassword.text.toString()
                val score = 0
                val databaseHandler = DatabaseHandler(this)
                try {
                    val status = databaseHandler.findPlayer(login)
                    if(status[0].password != password) {
                        Toast.makeText(
                            applicationContext,
                            "Incorrect password",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else{
                        val intent = Intent(this, MainActivity::class.java)
                        setRecord(status[0].login,status[0].password,status[0].score)
                        startActivity(intent)
                        editLogin.text.clear()
                        editPassword.text.clear()
                    }
                }catch (e: Exception){
                    databaseHandler.addPlayer(EmpModelClass(login,password,score))
                    val intent = Intent(this, MainActivity::class.java)
                    setRecord(login,password,score)
                    startActivity(intent)
                    editLogin.text.clear()
                    editPassword.text.clear()
                }
            }

        }
        buttonScore.setOnClickListener {
            val intent = Intent(this, ScoreboardActivity::class.java)
            startActivity(intent)
            editLogin.text.clear()
            editPassword.text.clear()
        }
    }

    fun setRecord(a: String,b: String,c: Int){
        val sharedScore = this.getSharedPreferences("com.example.myapplication.shared",0)
        val edit = sharedScore.edit()
        edit.putString("login", a)
        edit.putString("password", b)
        edit.putInt("score", c)
        edit.apply()
    }
}