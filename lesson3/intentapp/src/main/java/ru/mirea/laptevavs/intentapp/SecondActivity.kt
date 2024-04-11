package ru.mirea.laptevavs.intentapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import java.lang.Math.pow

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        lateinit var textView: TextView

        textView = findViewById(R.id.textView)

        val intent = intent
        val string = intent.getStringExtra("message")

        textView.text = "Квадрат значения моего номера по списку в группе стоставляет число ${pow(14.0,2.0).toInt()}," +
                " а текущее время $string"
    }
}