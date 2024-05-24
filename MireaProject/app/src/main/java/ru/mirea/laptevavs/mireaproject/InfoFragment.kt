package ru.mirea.laptevavs.mireaproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InfoFragment : Fragment() {
    private lateinit var textView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Создаем представление фрагмента, основанное на layout'е fragment_info.
        val view = inflater.inflate(R.layout.fragment_info, container, false)
        textView = view.findViewById(R.id.textView)

        // Создаем экземпляр Retrofit для выполнения сетевых запросов.
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/") // Базовый URL для запросов.
            .addConverterFactory(GsonConverterFactory.create()) // Конвертер для обработки ответов в формате JSON.
            .build()

        // Создаем экземпляр ApiService с использованием Retrofit.
        val service = retrofit.create(ApiService::class.java)

        // Создаем сетевой запрос для получения списка пользователей.
        val call = service.getUsers()

        // Выполняем запрос асинхронно.
        call.enqueue(object : Callback<List<InfoModel>> {
            override fun onResponse(call: Call<List<InfoModel>>, response: Response<List<InfoModel>>) {
                // Обработка успешного ответа сервера.
                if (response.isSuccessful) {
                    // Получаем список пользователей из ответа.
                    val users = response.body()
                    // Отображаем список пользователей в TextView.
                    textView.text = users?.joinToString(separator = "\n\n") { "${it.name} (${it.email})" } ?: "Нет доступных данных"
                } else {
                    // В случае ошибки, отображаем код ошибки.
                    textView.text = "Ошибка: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<List<InfoModel>>, t: Throwable) {
                // В случае неудачного запроса, отображаем сообщение об ошибке.
                textView.text = "Ошибка: ${t.message}"
            }
        })

        return view // Возвращаем представление фрагмента.
    }
}
