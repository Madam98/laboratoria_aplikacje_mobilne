package com.example.jsonapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jsonapp.DB.DatabaseHandler
import com.example.jsonapp.Model.ComModel
import com.example.jsonapp.Model.PostModel
import com.example.jsonapp.Model.TodosModel
import com.example.jsonapp.Model.UserModel
import org.json.JSONArray
import org.json.JSONTokener
import java.lang.Integer.parseInt
import java.net.URL

class TitleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title)

        val thread = Thread {
            run {
                val databaseHandler = DatabaseHandler(this)
                var url = "https://jsonplaceholder.typicode.com/users"
                var body = URL(url).readText()
                if (body != "") {
                    val jsonArray = JSONTokener(body).nextValue() as JSONArray
                    for (i in 0 until jsonArray.length()) {
                        val id = jsonArray.getJSONObject(i).getString("id")
                        val name = jsonArray.getJSONObject(i).getString("name")
                        val email = jsonArray.getJSONObject(i).getString("email")
                        databaseHandler.addUser(UserModel(parseInt(id),name,email))
                    }
                }
                url = "https://jsonplaceholder.typicode.com/todos"
                body = URL(url).readText()
                if (body != "") {
                    val jsonArray = JSONTokener(body).nextValue() as JSONArray
                    for (i in 0 until jsonArray.length()) {
                        val userId = jsonArray.getJSONObject(i).getString("userId")
                        val id = jsonArray.getJSONObject(i).getString("id")
                        val title = jsonArray.getJSONObject(i).getString("title")
                        val completed = jsonArray.getJSONObject(i).getString("completed")
                        databaseHandler.addTodos(TodosModel(parseInt(id),parseInt(userId),title,completed))
                    }
                }
                url = "https://jsonplaceholder.typicode.com/posts"
                body = URL(url).readText()
                if (body != "") {
                    val jsonArray = JSONTokener(body).nextValue() as JSONArray
                    for (i in 0 until jsonArray.length()) {
                        val userId = jsonArray.getJSONObject(i).getString("userId")
                        val id = jsonArray.getJSONObject(i).getString("id")
                        val title = jsonArray.getJSONObject(i).getString("title")
                        val body2 = jsonArray.getJSONObject(i).getString("body")
                        databaseHandler.addPosts(PostModel(parseInt(id),parseInt(userId),title,body2))
                    }
                }
                url = "https://jsonplaceholder.typicode.com/comments"
                body = URL(url).readText()
                if (body != "") {
                    val jsonArray = JSONTokener(body).nextValue() as JSONArray
                    for (i in 0 until jsonArray.length()) {
                        val postId = jsonArray.getJSONObject(i).getString("postId")
                        val id = jsonArray.getJSONObject(i).getString("id")
                        val name = jsonArray.getJSONObject(i).getString("name")
                        val email = jsonArray.getJSONObject(i).getString("email")
                        val body2 = jsonArray.getJSONObject(i).getString("body")
                        databaseHandler.addComs(ComModel(parseInt(id),parseInt(postId),name,email,body2))
                    }
                }
            }
            runOnUiThread {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        thread.start()
    }
}