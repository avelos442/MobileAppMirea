package ru.mirea.laptevavs.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.mirea.laptevavs.camera.databinding.ActivityMainBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE_PERMISSION = 100
    private val CAMERA_REQUEST = 0
    private var isWork = false
    private val imageUri: Uri? = null
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val cameraPermissionStatus =
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val storagePermissionStatus =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (cameraPermissionStatus == PackageManager.PERMISSION_GRANTED && storagePermissionStatus // получение разрешений
            == PackageManager.PERMISSION_GRANTED
        ) {
            isWork = true
        } else {
            //	Выполняется запрос к пользователь на получение необходимых разрешений
            val REQUEST_CODE_PERMISSION = 200
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), REQUEST_CODE_PERMISSION
            )
        }
        //создание временного файла изображений
        fun createImageFile(): File{
            val timeStamp: String =
                SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
            val imageFileName = "IMAGE_" + timeStamp + "_"
            val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return File.createTempFile(imageFileName, ".jpg", storageDirectory)
        }
        val photoFile = createImageFile()
        val authorities = applicationContext.packageName + ".fileprovider"
        var imageUri = FileProvider.getUriForFile(this@MainActivity, authorities, photoFile) //получение uri


        val callback: ActivityResultCallback<ActivityResult> =
            ActivityResultCallback<ActivityResult> { result ->
                if (result.getResultCode() === RESULT_OK) {
                    val data: Intent? = result.getData()
                    binding?.imageView?.setImageURI(imageUri)
                }
            }
        val cameraActivityResultLauncher: ActivityResultLauncher<Intent> =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult(),
                callback
            )
        binding.button12.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            //	проверка на наличие разрешений для камеры
            if (isWork) {
                try {
                    val photoFile = createImageFile()
                    //	генерирование пути к файлу на основе authorities
                    val authorities = applicationContext.packageName + ".fileprovider"
                    println(authorities)
                    imageUri = FileProvider.getUriForFile(this@MainActivity, authorities, photoFile)
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)//
                    cameraActivityResultLauncher.launch(cameraIntent)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

}