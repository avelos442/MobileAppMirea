package ru.mirea.laptevavs.thread3

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ru.mirea.laptevavs.thread3.databinding.ActivityMainBinding
import java.util.Arrays


class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private var counter = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        val infoTextView = binding!!.textView
        val mainThread = Thread.currentThread()
        infoTextView.text = "Имя текущего потока: " + mainThread.name
        // Меняем имя и выводим в текстовом поле
        mainThread.name = "МОЙ НОМЕР ГРУППЫ: БСБО-11-21, НОМЕР ПО СПИСКУ: 14, МОЙ ЛЮБИИМЫЙ ФИЛЬМ: Сумерки"
        infoTextView.append(
            """
 Новое имя потока: ${mainThread.name}"""
        )
        Log.d(
            MainActivity::class.java.simpleName,
            "Stack:	" + Arrays.toString(mainThread.stackTrace)
        )
        Log.d(MainActivity::class.java.simpleName, "Group:	" + mainThread.threadGroup)
        binding!!.buttonMirea.setOnClickListener {
            calculateAveragePairs()
            Thread(object : Runnable {
                override fun run() {
                    val numberThread = counter++
                    Log.d(
                        "ThreadProject",
                        String.format(
                            "Запущен	поток	№	%d	студентом	группы	№	%s	номер	по списку	№	%d	",
                            numberThread,
                            "БСБО-11-21",
                            14
                        )
                    )
                    val endTime = System.currentTimeMillis() + 20 * 1000
                    while (System.currentTimeMillis() < endTime) {
                        synchronized(this) {
                            try {
                                Thread.sleep(endTime - System.currentTimeMillis())
                                Log.d(
                                    MainActivity::class.java.simpleName,
                                    "Endtime:	$endTime"
                                )
                            } catch (e: Exception) {
                                throw RuntimeException(e)
                            }
                        }
                        Log.d("ThreadProject", "Выполнен поток №	$numberThread")
                    }
                }
            }).start()
        }
    }

    private fun calculateAveragePairs() {
        val totalPairs: Int = binding?.obPar?.text.toString().toInt()
        val totalDays: Int = binding?.obDay?.text.toString().toInt()

        // Запускаем вычисление в фоновом потоке
        Thread {
            val result = totalPairs.toDouble() / totalDays
            // Передаем результат на главный поток для обновления UI
            Handler(Looper.getMainLooper()).post(Runnable { // Добавляем новую строку к существующему тексту
                binding!!.textView.append("\n")
                binding!!.textView.append(
                    String.format(
                        "Среднее количество пар в день за месяц: %.2f",
                        result
                    )
                )
            })
        }.start()
    }}

