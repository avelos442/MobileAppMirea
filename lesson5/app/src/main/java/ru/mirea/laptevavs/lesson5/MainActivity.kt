package ru.mirea.laptevavs.lesson5

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SimpleAdapter
import ru.mirea.laptevavs.lesson5.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensors: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL) // список датчиков
        val listSensor = binding.listView

        // создание списка
        val arrayList = ArrayList<HashMap<String, Any>>()
        for (sensor in sensors) {
            val sensorTypeList = HashMap<String, Any>()
            sensorTypeList["Name"] = sensor.name
            sensorTypeList["Value"] = sensor.maximumRange
            arrayList.add(sensorTypeList)
        }

        val mHistory = SimpleAdapter(
            this,
            arrayList,
            android.R.layout.simple_list_item_2,
            arrayOf("Name", "Value"),
            intArrayOf(android.R.id.text1, android.R.id.text2)
        )
        listSensor.adapter = mHistory

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        var accelerometerSensor = sensorManager
            .getDefaultSensor(Sensor.TYPE_ACCELEROMETER) //получает датчик акселерометра
        sensorManager.registerListener(
            this, accelerometerSensor,
            SensorManager.SENSOR_DELAY_NORMAL //регистрирует слушателя для акселерометра
        )
    }

    override fun onSensorChanged(event: SensorEvent?) { //метод обратного вызова
        if (event!!.sensor.type === Sensor.TYPE_ACCELEROMETER) {
            val valueAzimuth = event!!.values[0]
            val valuePitch = event!!.values[1]
            val valueRoll = event!!.values[2]
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        TODO("Not yet implemented")
    }
}