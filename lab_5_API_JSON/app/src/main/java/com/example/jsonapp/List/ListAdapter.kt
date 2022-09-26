package com.example.jsonapp.List

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jsonapp.Model.DataModel
import com.example.jsonapp.R
import java.time.DayOfWeek

class ListAdapter(private val context: Activity, private val name: Array<String>, private val email: Array<String>, private val todos: Array<String>, private val posts: Array<String>)
    : ArrayAdapter<String>(context, R.layout.custom_table, name) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.custom_table, null, true)

        val nameText = rowView.findViewById(R.id.textName) as TextView
        val emailText = rowView.findViewById(R.id.textEmail) as TextView
        val todosText = rowView.findViewById(R.id.textTodos) as TextView
        val postText = rowView.findViewById(R.id.textPost) as TextView

        nameText.text = name[position]
        emailText.text = email[position]
        todosText.text = todos[position]
        postText.text = posts[position]
        return rowView
    }
}