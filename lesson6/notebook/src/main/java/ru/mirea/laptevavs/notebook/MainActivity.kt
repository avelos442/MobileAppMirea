package ru.mirea.laptevavs.notebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        //Проверка доступности внешнего хранилища
        if (isExternalStorageAvailable()) {
            val buttonRead: Button = findViewById(R.id.buttonDownload)
            val buttonWrite: Button = findViewById(R.id.buttonSave)
            buttonRead.setOnClickListener {
                Thread {
                    val inputQuote: EditText = findViewById(R.id.Quote)
                    val inputPath: EditText = findViewById(R.id.NameFile)
                    val fileContent = readFileFromExternalStorage(inputPath.text.toString())
                    runOnUiThread {
                        inputQuote.setText(fileContent.toString())
                        Toast.makeText(this, "Прочитано", Toast.LENGTH_SHORT).show()
                    }
                }.start()
            }

            buttonWrite.setOnClickListener {
                Thread {
                    val inputQuote: EditText = findViewById(R.id.Quote)
                    val inputPath: EditText = findViewById(R.id.NameFile)
                    writeFileToExternalStorage(
                        inputPath.text.toString(),
                        inputQuote.text.toString()
                    )
                    runOnUiThread {
                        Toast.makeText(this, "Записано", Toast.LENGTH_SHORT).show()
                    }
                }.start()
            }

        } else {
            Toast.makeText(
                this,
                "External storage is not available now",
                Toast.LENGTH_SHORT
            ).show()
        }


    }

    private fun isExternalStorageAvailable(): Boolean =
        Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()

    private fun writeFileToExternalStorage(fileName: String, quote: String) {
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val file = File(path, fileName)
        try {
            val fileOutputStream = FileOutputStream(file.absoluteFile)
            val output = OutputStreamWriter(fileOutputStream)

            output.write(quote)
            output.close()
        } catch (e: IOException) {
            Log.w(TAG, "Error writing $file", e)
        }
    }

    private fun readFileFromExternalStorage(fileName: String): MutableList<String>? {
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val file = File(path, fileName)
        try {
            val fileInputStream = FileInputStream(file.getAbsoluteFile())
            val inputStreamReader = InputStreamReader(fileInputStream, StandardCharsets.UTF_8)
            val lines: MutableList<String> = ArrayList()
            val reader = BufferedReader(inputStreamReader)
            var line = reader.readLine()
            while (line != null) {
                lines.add(line)
                line = reader.readLine()
            }
            return lines
        } catch (e: Exception) {
            Log.w(
                "ExternalStorage",
                String.format("Read	from file %s failed", e.message)
            )
        }
        return null
    }

    private companion object {
        val TAG = MainActivity::class.java.simpleName
    }
}