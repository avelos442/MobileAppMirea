package ru.mirea.laptevavs.favoritebook

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var textViewUserBook: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewUserBook = findViewById(R.id.textViewBook)
        //обработчик результата
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val userBook = data?.getStringExtra(USER_MESSAGE)
                    textViewUserBook.text = userBook
                }
            }
    }
    //Отправка введенных пользователем данных по нажатию на кнопку
    fun getInfoAboutBook(view: View) {
        val intent = Intent(this, ShareActivity::class.java)
        intent.putExtra(KEY, "Java для чайников")
        activityResultLauncher.launch(intent)
    }

    companion object {
        const val KEY = "book-name"
        const val USER_MESSAGE = "MESSAGE"
    }
}