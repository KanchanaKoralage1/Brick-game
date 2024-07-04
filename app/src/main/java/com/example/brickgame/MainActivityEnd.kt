package com.example.brickgame

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivityEnd :AppCompatActivity(){

    private lateinit var scoreTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_end)


        val score = intent.getIntExtra("Score", 0)

        scoreTextView = findViewById(R.id.textView2)
        scoreTextView.text = "Your Score:  $score"

        val Restart=findViewById<Button>(R.id.restart)

        Restart.setOnClickListener {

            val intent = Intent(this, GameView::class.java)
            startActivity(intent)

        }

        val Exit=findViewById<Button>(R.id.exit)

        Exit.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }

    }
}