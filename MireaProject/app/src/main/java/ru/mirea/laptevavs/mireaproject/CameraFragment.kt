package ru.mirea.laptevavs.mireaproject

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ru.mirea.laptevavs.mireaproject.R

class CameraFragment : Fragment() {

    private lateinit var imageView1: ImageView
    private lateinit var imageView2: ImageView
    private lateinit var imageViewWide: ImageView

    private var selectedView: ImageView? = null

    private var isWork = false

    private val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_camera, container, false)

        imageView1 = view.findViewById(R.id.imageView1)
        imageView2 = view.findViewById(R.id.imageView2)
        imageViewWide = view.findViewById(R.id.imageViewWide)

        val cameraPermissionStatus =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
        val storagePermissionStatus =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (cameraPermissionStatus == PackageManager.PERMISSION_GRANTED && storagePermissionStatus
            == PackageManager.PERMISSION_GRANTED
        ) {
            isWork = true
        } else {
            //	Выполняется запрос к пользователь на получение необходимых разрешений
            val REQUEST_CODE_PERMISSION = 200
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), REQUEST_CODE_PERMISSION
            )
        }

        // Устанавливаем обработчик нажатия на imageView1
        imageView1.setOnClickListener {
            selectedView = imageView1
            dispatchTakePictureIntent(imageView1)
        }

        // Устанавливаем обработчик нажатия на imageView2
        imageView2.setOnClickListener {
            selectedView = imageView2
            dispatchTakePictureIntent(imageView2)
        }

        imageViewWide.setOnClickListener {
            selectedView = imageViewWide
            dispatchTakePictureIntent(imageViewWide)
        }

        return view
    }

    private fun dispatchTakePictureIntent(imageView: ImageView) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            selectedView?.setImageBitmap(imageBitmap) // Или imageView2.setImageBitmap(imageBitmap), в зависимости от того, на какое изображение было нажато
        }
    }
}