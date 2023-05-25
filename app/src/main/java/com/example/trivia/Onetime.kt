package com.example.trivia

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Onetime: AppCompatActivity() {
    var nxt=0
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.onetimedesign)
        val any: Button=findViewById(R.id.cat1)
        val science: Button=findViewById(R.id.cat2)
        val computers: Button=findViewById(R.id.cat3)
        val general: Button=findViewById(R.id.cat4)
        val sports: Button=findViewById(R.id.cat5)
        any.setOnClickListener { call(1) }
        science.setOnClickListener { call(2) }
        computers.setOnClickListener { call(3) }
        general.setOnClickListener { call(4) }
        sports.setOnClickListener { call(5) }
    }
    private fun call(i: Int){
        val intent = Intent(this, MainScreen::class.java)
        nxt=i
        intent.putExtra("check",nxt)
        startActivity(intent)
    }
}