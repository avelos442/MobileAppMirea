package ru.mirea.laptevavs.timeservice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.mirea.laptevavs.timeservice.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.Socket

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // Объявление статических констант для хоста и порта сервера времени
    private companion object {
        val TAG: String = MainActivity::class.java.simpleName
        const val HOST = "time.nist.gov"
        const val PORT = 13
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Включение режима обработки отступов от краёв экрана
        enableEdgeToEdge()

        // Инициализация привязки макета активности
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Установка обработчика для обновления отступов от системных панелей
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Установка обработчика нажатия на кнопку
        binding.button.setOnClickListener {
            // Запуск корутины для выполнения операций ввода-вывода в фоновом потоке
            lifecycleScope.launch(Dispatchers.IO) {
                getTimeFromServer()
            }

        }
    }

    // Функция для получения времени с сервера
    private suspend fun getTimeFromServer() {
        try {
            // Установка соединения с сервером времени
            val socket = Socket(HOST, PORT)
            val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
            reader.readLine()
            val timeResult = reader.readLine()
            socket.close()

            // Вывод результата времени в логи
            Log.d(TAG, timeResult)
            // Обновление пользовательского интерфейса с полученным временем
            withContext(Dispatchers.Main) {
                binding.textView.text = parseTimeResult(timeResult)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // Функция для парсинга строки с временем
    private fun parseTimeResult(timeResult: String): String {
        val split = timeResult.split(" ")
        if (split.size >= 3) {
            val date = split[1]
            val time = split[2]
            return "Date: $date\nTime: $time"
        }
        return "Unable to parse time result"
    }
}
