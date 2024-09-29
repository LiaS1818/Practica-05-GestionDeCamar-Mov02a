package com.example.ejemplo01segundoparcial250920224

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lector = findViewById<Button>(R.id.btnLector)
        val camara = findViewById<Button>(R.id.btnFoto  )
        lector.setOnClickListener { lector() }
        camara.setOnClickListener { camara() }

    }

    fun lector() {
        val intent = Intent(applicationContext, LectorActivity::class.java)
        startActivity(intent)
    }

    fun camara() {
        val intent = Intent(applicationContext, CamaraActivity::class.java)
        startActivity(intent)
    }
}