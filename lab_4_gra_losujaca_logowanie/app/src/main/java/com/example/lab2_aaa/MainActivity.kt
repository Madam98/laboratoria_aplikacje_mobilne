package com.example.lab2_aaa

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.lab2_aaa.DB.Model.EmpModelClass

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val builder = AlertDialog.Builder(this)
        val buttonGuess = findViewById<Button>(R.id.guessButton)
        val buttonNew = findViewById<Button>(R.id.newGameButton)
        val buttonLogout = findViewById<Button>(R.id.logoutButton)
        val buttonScore = findViewById<Button>(R.id.scoreButton)
        val textCount = findViewById<TextView>(R.id.countText)
        val textPoints = findViewById<TextView>(R.id.countText2)
        val textEdit = findViewById<EditText>(R.id.editText)
        val textHint = findViewById<TextView>(R.id.helpText)
        var answer = (0..20).random()
        var shots = 0
        var currpoints: Int
        var points: Int
        var login: String
        var password: String
        val databaseHandler = DatabaseHandler(this)
        val sharedScore = this.getSharedPreferences("com.example.myapplication.shared",0)
        val edit = sharedScore.edit()
        textCount.text=shots.toString()

        currpoints = getRecord()
        textPoints.text=(currpoints).toString()

        buttonLogout.setOnClickListener {
            edit.clear()
            edit.apply()
            finish()
        }

        buttonScore.setOnClickListener {
            val intent = Intent(this, ScoreboardActivity::class.java)
            startActivity(intent)
        }

        buttonNew.setOnClickListener {
            textEdit.text.clear()
            answer= (0..20).random()
            shots=0
            textCount.text = shots.toString()
            if (textHint.visibility == TextView.GONE)
                textHint.visibility = TextView.VISIBLE
            textHint.text = "NEW GAME"
        }

        buttonGuess.setOnClickListener {
            if (textEdit.text.isEmpty() || Integer.parseInt(textEdit.text.toString()) < 0 || Integer.parseInt(
                    textEdit.text.toString()
                ) > 20){
                builder.setTitle("Error")
                builder.setMessage("Provide number from range <0;20>")
                builder.setPositiveButton("OK", null)
                builder.show()
                textEdit.text.clear()
            }
            else{
                if(textEdit.text.toString().toInt()==answer){
                    points = when(shots){
                        0 -> 5
                        in 1..3 -> 3
                        4,5 -> 2
                        else -> 1
                    }
                    currpoints+=points
                    textPoints.text=(currpoints).toString()
                    setRecord(currpoints)
                    builder.setTitle("Congratulations")
                    builder.setMessage("You guessed mystery number correctly!\n Number of tries: "+(shots+1)+"\nNumber of points scored: "+points)
                    builder.setPositiveButton("Yay", null)
                    builder.show()
                    login = sharedScore.getString("login", "Default") ?: "Not Set"
                    password = sharedScore.getString("password", "Default") ?: "Not Set"
                    currpoints = sharedScore.getInt("score",0)
                    databaseHandler.updatePlayer(EmpModelClass(login,password,currpoints))
                    textEdit.text.clear()
                    answer= (0..20).random()
                    shots=0
                    textCount.text = shots.toString()
                    if (textHint.visibility == TextView.GONE)
                        textHint.visibility = TextView.VISIBLE
                    textHint.text = "NEW GAME"
                }
                else{
                    if (textHint.visibility == TextView.GONE)
                        textHint.visibility = TextView.VISIBLE
                    if(Integer.parseInt(textEdit.text.toString()) >answer){
                        textHint.text = "Mystery number is smaller than provided.."
                    }
                    else{
                        textHint.text = "Mystery number is bigger than provided.."
                    }
                    textEdit.text.clear()
                    shots++
                    textCount.text = shots.toString()
                    if (shots==10){
                        builder.setTitle("You loose")
                        builder.setMessage("You did not guessed mystery number in 10 tries!\n" +
                                " Starting new game.")
                        builder.setPositiveButton("OK", null)
                        builder.show()
                        textEdit.text.clear()
                        textEdit.text.clear()
                        answer= (0..20).random()
                        shots=0
                        textCount.text = shots.toString()
                        if (textHint.visibility == TextView.GONE)
                            textHint.visibility = TextView.VISIBLE
                        textHint.text = "NEW GAME"
                    }
                }
            }
        }
    }
    fun setRecord(currpoints: Int){
        val sharedScore = this.getSharedPreferences("com.example.myapplication.shared",0)
        val edit = sharedScore.edit()
        edit.putInt("score", currpoints)
        edit.apply()
    }

    fun getRecord():Int{
        val sharedScore = this.getSharedPreferences("com.example.myapplication.shared",0)
        return sharedScore.getInt("score", 0)
    }
}