package com.example.brickgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val Start=findViewById<Button>(R.id.start)
        Start.setOnClickListener{

            val intent = Intent(this, GameView::class.java)
            startActivity(intent)

        }

    }
}