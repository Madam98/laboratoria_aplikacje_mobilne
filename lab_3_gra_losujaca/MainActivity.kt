package com.example.myapplication

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.R

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val builder = AlertDialog.Builder(this)
        val buttonGuess = findViewById<Button>(R.id.guessButton)
        val buttonNew = findViewById<Button>(R.id.newGameButton)
        val buttonReset = findViewById<Button>(R.id.resetButton)
        val textCount = findViewById<TextView>(R.id.countText)
        val textPoints = findViewById<TextView>(R.id.countText2)
        val textEdit = findViewById<EditText>(R.id.editText)
        val textHint = findViewById<TextView>(R.id.hintText)
        var answer = (0..20).random()
        var shots = 0
        var currpoints = 0
        var points: Int
        textCount.text=shots.toString()

        fun setRecord(){
            val sharedScore = this.getSharedPreferences("com.example.myapplication.shared",0)
            val edit = sharedScore.edit()
            edit.putInt("score", currpoints)
            edit.apply()
        }

        fun getRecord(){
            val sharedScore = this.getSharedPreferences("com.example.myapplication.shared",0)
            currpoints = sharedScore.getInt("score", 0)
        }

        getRecord()
        textPoints.text=(currpoints).toString()

        buttonReset.setOnClickListener(){
            currpoints=0
            textPoints.text=(currpoints).toString()
            setRecord()
        }

        buttonNew.setOnClickListener(){
            textEdit.getText().clear()
            answer= (0..20).random()
            shots=0
            textCount.setText(Integer.toString(shots))
            if (textHint.visibility == TextView.GONE)
                textHint.setVisibility(TextView.VISIBLE)
            textHint.setText("NEW GAME")
        }

        buttonGuess.setOnClickListener(){
            if (textEdit.text.isEmpty() || Integer.parseInt(textEdit.text.toString()) < 0 || Integer.parseInt(
                    textEdit.text.toString()
                ) > 20){
                Toast.makeText(applicationContext, "Provide number in range <0;20>", Toast.LENGTH_LONG).show()
            }
            else{
                if(textEdit.text.toString().toInt()==answer){
                    when(shots){
                        0 -> points=5
                        in 1..3 -> points=3
                        4,5 -> points=2
                        else -> points=1
                    }
                    currpoints+=points
                    textPoints.text=(currpoints).toString()
                    setRecord()
                    builder.setTitle("Congratulations")
                    builder.setMessage("You guessed mystery number correctly!\n Number of tries: "+(shots+1)+"\nNumber of points scored: "+points)
                    builder.setPositiveButton("Yay", null)
                    builder.show()
                    textEdit.getText().clear()
                    answer= (0..20).random()
                    shots=0
                    textCount.setText(Integer.toString(shots))
                    if (textHint.visibility == TextView.GONE)
                        textHint.setVisibility(TextView.VISIBLE)
                    textHint.setText("NEW GAME")
                }
                else{
                    if (textHint.visibility == TextView.GONE)
                        textHint.setVisibility(TextView.VISIBLE)
                    if(Integer.parseInt(textEdit.text.toString()) >answer){
                        textHint.setText("Mystery number is smaller than provided.")
                    }
                    else{
                        textHint.setText("Mystery number is bigger than provided.")
                    }
                    textEdit.getText().clear()
                    shots++
                    textCount.setText(Integer.toString(shots))
                    if (shots==10){
                        builder.setTitle("You loose")
                        builder.setMessage("You did not guessed mystery number in 10 tries!\n Starting new game.")
                        builder.setPositiveButton("OK", null)
                        builder.show()
                        textEdit.getText().clear()
                        textEdit.getText().clear()
                        answer= (0..20).random()
                        shots=0
                        textCount.setText(Integer.toString(shots))
                        if (textHint.visibility == TextView.GONE)
                            textHint.setVisibility(TextView.VISIBLE)
                        textHint.setText("NEW GAME")
                    }
                }
            }
        }
    }
}