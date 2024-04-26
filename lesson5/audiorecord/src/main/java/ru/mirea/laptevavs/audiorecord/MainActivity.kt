package ru.mirea.laptevavs.audiorecord

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ru.mirea.laptevavs.audiorecord.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE_PERMISSION = 200
    private var isWork = false
    private var isStartRecording = true
    private var isStartPlaying = false
    lateinit var binding: ActivityMainBinding
    lateinit var recordButton: Button
    lateinit var playButton: Button
    lateinit var recordFilePath: String
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recordButton = binding.button
        playButton = binding.button2
        playButton.setEnabled(false)
        recordFilePath = File(
            getExternalFilesDir(Environment.DIRECTORY_MUSIC),
            "/audiorecordtest.3gp"
        ).absolutePath

        val audioRecordPermissionStatus = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        )
        val storagePermissionStatus = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (audioRecordPermissionStatus == PackageManager.PERMISSION_GRANTED && storagePermissionStatus
            == PackageManager.PERMISSION_GRANTED
        ) {
            isWork = true
        } else {
            //	Выполняется	запрос	к	пользователь	на	получение	необходимых	разрешений
            ActivityCompat.requestPermissions(
                this, arrayOf<String>(
                    Manifest.permission.RECORD_AUDIO,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), REQUEST_CODE_PERMISSION
            )
        }
        fun startRecording(){
            recorder = MediaRecorder()
            recorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
            recorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            recorder!!.setOutputFile(recordFilePath)
            recorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            try {
                recorder!!.prepare()
            }catch (e: Exception){
                Log.e("Recorder", "prepare() failed")
            }
            recorder!!.start()
        }
        fun stopRecording(){
            recorder?.stop()
            recorder?.release()
            recorder = null
        }

        fun startPlaying(){
            player = MediaPlayer()
            try {
                player!!.setDataSource(recordFilePath)
                player!!.prepare()
                player!!.start()
            }catch(e: Exception){
                Log.e("Player", "prepare() failed")
            }
        }
        fun stopPlaying(){
            player?.release()
            player = null
        }

        recordButton.setOnClickListener {
            if(isStartRecording){
                recordButton.text = "Stop recording"
                playButton.isEnabled = false
                startRecording()
            }else{
                recordButton.text = "Start recording"
                playButton.isEnabled = true
                stopRecording()
            }
            isStartRecording = !isStartRecording
        }
        playButton.setOnClickListener {
            if(isStartPlaying){
                recordButton.text = "Stop recording"
                playButton.isEnabled = false
                startPlaying()
            }else{
                recordButton.text = "Start recording"
                playButton.isEnabled = true
                stopPlaying()
            }
            isStartPlaying = !isStartPlaying
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        //	производится	проверка	полученного	результата	от	пользователя	на	запрос	разрешения	Camera
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_PERMISSION -> isWork = grantResults[0] == PackageManager.PERMISSION_GRANTED
        }
        if (!isWork) finish()
    }
}