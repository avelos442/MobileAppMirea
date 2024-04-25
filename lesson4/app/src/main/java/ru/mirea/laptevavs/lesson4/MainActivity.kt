package ru.mirea.laptevavs.lesson4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.mirea.laptevavs.lesson4.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textViewSongName.text = "Attention"
    }
}