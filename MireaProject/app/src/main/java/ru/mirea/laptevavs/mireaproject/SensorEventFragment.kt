package ru.mirea.laptevavs.mireaproject


import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class SensorEventFragment : Fragment(), SensorEventListener {

    private lateinit var textViewBarometer: TextView
    private lateinit var sensorManager: SensorManager
    private var barometerSensor: Sensor? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sensor_event, container, false)

        textViewBarometer = view.findViewById(R.id.directionTextView)

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        barometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)

        return view
    }

    override fun onResume() {
        super.onResume()
        barometerSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_PRESSURE) {
                val pressure = it.values[0]
                processData(pressure)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Метод не используется, но необходим для реализации интерфейса SensorEventListener
    }

    private fun updateText(barometerData: String) {
        textViewBarometer.text = barometerData
    }

    private fun processData(pressure: Float) {
        val result = when {
            pressure > 1013 -> "Высокое давление"
            pressure < 1013 -> "Низкое давление"
            else -> "Стандартное давление"
        }
        updateText(result)
    }
}