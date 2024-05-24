package ru.mirea.laptevavs.lesson6

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.mirea.laptevavs.lesson6.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var button: Button
    lateinit var group: EditText
    lateinit var number: EditText
    lateinit var favFilm: EditText
    lateinit var sharedPref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        // Обработчик для области вывода контента
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Инициализация SharedPreferences для сохранения данных
        sharedPref = getSharedPreferences("mirea_settings", MODE_PRIVATE)
        editor = sharedPref.edit()

        // Получение
        button = binding.buttonSave
        group = binding.editTextGroup
        number = binding.editTextNumber
        favFilm = binding.editTextFavoriteFilm

        // Обработчик нажатия кнопки
        button.setOnClickListener {
            editor.putString("GROUP", group.text.toString())
            editor.putString("NUMBER", number.text.toString())
            editor.putString("FAV_FILM", favFilm.text.toString())
            editor.apply()
        }

        // Восстановление сохраненных данных
        group.setText(sharedPref.getString("GROUP", "unknown"))
        number.setText(sharedPref.getString("NUMBER", "unknown"))
        favFilm.setText(sharedPref.getString("FAV_FILM", "unknown"))
    }
}