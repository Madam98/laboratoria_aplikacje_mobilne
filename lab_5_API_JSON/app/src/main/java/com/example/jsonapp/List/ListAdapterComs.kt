package com.example.jsonapp.List

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.jsonapp.R

class ListAdapterComs(private val context: Activity, private val name: Array<String>, private val email: Array<String>, private var body: Array<String>)
    : ArrayAdapter<String>(context, R.layout.custom_table_comments, name) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.custom_table_comments, null, true)

        val posterText = rowView.findViewById(R.id.textPoster) as TextView
        val bodyText = rowView.findViewById(R.id.textComment) as TextView

        posterText.text = name[position]+"\n<"+email[position]+">:"
        bodyText.text = "\""+body[position]+"\""
        return rowView
    }
}