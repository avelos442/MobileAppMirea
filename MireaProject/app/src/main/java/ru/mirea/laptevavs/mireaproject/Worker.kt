package ru.mirea.laptevavs.mireaproject

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Random
import java.util.concurrent.TimeUnit

class Worker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val startTime = System.currentTimeMillis()
        Log.d("Worker", "Начало фоновой задачи в ${SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(startTime))}")

        val random = Random()
        for (i in 0 until 10) {
            try {
                TimeUnit.SECONDS.sleep(4)
            } catch (e: InterruptedException) {
                e.printStackTrace()
                return Result.failure()
            }

            val randomNumber = random.nextInt(100)
            val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            Log.d("Worker", "Итерация фоновой задачи ${i + 1}. Текущее время: $currentTime. Случайное число: $randomNumber")
        }

        val endTime = System.currentTimeMillis()
        Log.d("Worker", "Фоновая задача завершена в ${SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(endTime))}")
        val duration = endTime - startTime
        Log.d("Worker", "Общее время выполнения фоновой задачи: ${TimeUnit.MILLISECONDS.toSeconds(duration)} секунд")

        return Result.success()
    }
}