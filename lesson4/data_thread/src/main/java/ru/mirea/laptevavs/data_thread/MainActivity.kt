package ru.mirea.laptevavs.data_thread

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.mirea.laptevavs.data_thread.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val runn1 = Runnable { binding.textView.text = "runn1" }
        val runn2 = Runnable { binding.textView.text = "runn2" }
        val runn3 = Runnable { binding.textView.text = "runn3" }
        val t = Thread {
            try {
                TimeUnit.SECONDS.sleep(2)
                runOnUiThread(runn1)
                TimeUnit.SECONDS.sleep(1)
                binding.textView.postDelayed(runn3, 2000)
                binding.textView.post(runn2)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        t.start()
    }
}