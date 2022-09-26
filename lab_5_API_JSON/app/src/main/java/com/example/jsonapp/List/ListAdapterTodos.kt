package com.example.jsonapp.List

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.jsonapp.R

class ListAdapterTodos(private val context: Activity, private val title: Array<String>, private val completed: Array<String>)
    : ArrayAdapter<String>(context, R.layout.custom_table_todos, title) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.custom_table_todos, null, true)

        val titleText = rowView.findViewById(R.id.textTitle) as TextView
        val completedText = rowView.findViewById(R.id.textCompleted) as TextView

        titleText.text = title[position]
        completedText.text = completed[position]
        return rowView
    }
}