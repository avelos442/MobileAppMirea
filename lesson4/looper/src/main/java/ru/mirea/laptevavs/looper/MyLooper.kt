package ru.mirea.laptevavs.looper




import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.os.Message
import java.util.concurrent.TimeUnit

class MyLooper(
    mainThreadHandler: Handler,
) : Thread() {
    private val TAG = Looper::class.java.simpleName

    lateinit var mHandler: Handler
    private var mainHandler: Handler = mainThreadHandler

    override fun run() {
        Log.d(TAG, "run")
        Looper.prepare()

        //fromExampleCode()

        fromTaskCode()
        Looper.loop()


    }

    private fun fromTaskCode() {
        mHandler = object : Handler(Looper.myLooper()!!) {
            override fun handleMessage(msg: Message) {
                val startTime = msg.data.getLong("START_TIME")
                val age = msg.data.getInt("AGE")
                val position = msg.data.getString("JOB")!!
                Log.d("MyLooper get message:", "Возраст: $age.\nКем работаете: $position")

                TimeUnit.SECONDS.sleep(age.toLong())

                val currentTime = System.currentTimeMillis()
                val delay = currentTime - startTime

                val message = Message()
                val bundle = Bundle()

                bundle.putString("result", "New age: $delay")
                message.data = bundle

                mainHandler.sendMessage(message)
            }
        }
    }

    private fun fromExampleCode() {
        mHandler = object : Handler(Looper.myLooper()!!) {
            override fun handleMessage(msg: Message) {
                val data = msg.data.getString("KEY")!!
                Log.d("MyLooper get message:", data)

                val count = data.length
                val message = Message()
                val bundle = Bundle()
                bundle.putString("result", "The number of letters in the word $data is $count")
                message.data = bundle
                mainHandler.sendMessage(message)
            }
        }
    }
}