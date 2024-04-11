package ru.mirea.laptevavs.sharer1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var intent = Intent(Intent.ACTION_SEND)
        intent.setType("*/*")
        intent.putExtra(Intent.EXTRA_TEXT, "Mirea")
        startActivity(Intent.createChooser(intent, "Выбор за вами!"))


        intent = Intent(Intent.ACTION_PICK)
        intent.setType("image/*")
        val callback: ActivityResultCallback<ActivityResult?> =
            ActivityResultCallback { result ->
                if (result != null) {
                    if (result.resultCode == RESULT_OK) {
                        val data = result.data
                        Log.d(MainActivity::class.java.simpleName, "Data:" + data!!.dataString)
                    }
                }
            }
        val imageActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            callback
        )
        imageActivityResultLauncher.launch(intent)
    }
}