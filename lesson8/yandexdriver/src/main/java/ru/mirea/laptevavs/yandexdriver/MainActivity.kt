package ru.mirea.laptevavs.yandexdriver

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingRouter
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.directions.driving.DrivingSession.DrivingRouteListener
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError


class MainActivity : AppCompatActivity(), DrivingRouteListener {
    private val task = true
    private var isWork = false
    private /*final*/ var ROUTE_START_LOCATION = Point(55.789396, 37.536832)
    private /*final*/ var ROUTE_END_LOCATION = Point(55.794259, 37.701448)
    private val SCREEN_CENTER = Point(
        (ROUTE_START_LOCATION.latitude + ROUTE_END_LOCATION.latitude) / 2,
        (ROUTE_START_LOCATION.longitude + ROUTE_END_LOCATION.longitude) / 2
    )
    private var mapView: MapView? = null
    private var mapObjects: MapObjectCollection? = null
    private var drivingRouter: DrivingRouter? = null
    private var drivingSession: DrivingSession? = null
    private val colors = intArrayOf(-0x10000, -0xff0100, 0x00FFBBBB, -0xffff01)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MapKitFactory.initialize(this)
        DirectionsFactory.initialize(this)
        mapView = findViewById<MapView>(R.id.mapview)
        mapView?.getMap()?.isRotateGesturesEnabled = false
        // Устанавливаем начальную точку и масштаб
        mapView?.getMap()?.move(CameraPosition(SCREEN_CENTER, 10f, 0f, 0f))
        // Ининциализируем объект для создания маршрута водителя
        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter()
        mapObjects = mapView?.getMap()?.mapObjects?.addCollection()
        val coarsePermissionStatus = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val finePermissionStatus = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val backgroundPermissionStatus = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
        if (coarsePermissionStatus == PackageManager.PERMISSION_GRANTED &&
            finePermissionStatus == PackageManager.PERMISSION_GRANTED
        ) {
            isWork = true
        } else {
            //	Выполняется запрос к пользователю на получение необходимых разрешений
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION /*,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION*/
                ), REQUEST_CODE_PERMISSION
            )
        }
        if (isWork) {
            submitRequest()
        }
    }

    private fun submitRequest() {
        val drivingOptions = DrivingOptions()
        val vehicleOptions = VehicleOptions()
        // кол-во альтернативных путей
        drivingOptions.setRoutesCount(4)
        if (task) {
            ROUTE_START_LOCATION = Point(55.789396, 37.536832)
            ROUTE_END_LOCATION = Point(55.794259, 37.701448)
        }
        val requestPoints = ArrayList<RequestPoint>()
        // Установка точек маршрута
        requestPoints.add(
            RequestPoint(
                ROUTE_START_LOCATION,
                RequestPointType.WAYPOINT,
                null
            )
        )
        requestPoints.add(
            RequestPoint(
                ROUTE_END_LOCATION,
                RequestPointType.WAYPOINT,
                null
            )
        )
        // Отправка запроса на сервер
        drivingSession =
            drivingRouter!!.requestRoutes(requestPoints, drivingOptions, vehicleOptions, this)
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView!!.onStart()
    }

    override fun onStop() {
        super.onStop()
        MapKitFactory.getInstance().onStop()
        mapView!!.onStop()
    }

    // обработка полученных маршрутов
    override fun onDrivingRoutes(list: List<DrivingRoute>) {
        var color: Int
        for (i in list.indices) {
            // настроиваем цвета для каждого маршрута
            color = colors[i]
            // добавление маршрута на карту
            mapObjects!!.addPolyline(list[i].geometry).setStrokeColor(color)
        }

        // задание для маркера
        if (task) {
            val marker = mapView!!.map.mapObjects.addPlacemark(
                ROUTE_END_LOCATION,
                ImageProvider.fromResource(
                    this,
                    com.yandex.maps.mobile.R.drawable.search_layer_pin_selected_default
                )
            )
            marker.setIconStyle(IconStyle().setScale(1f))
            marker.addTapListener { mapObject, point ->
                Toast.makeText(
                    application, "Стромынка 20. МИРЭА",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
        }
    }

    override fun onDrivingRoutesError(error: Error) {
        var errorMesage = getString(R.string.unknown_error_message)
        if (error is RemoteError) {
            errorMesage = getString(R.string.remote_error_message)
        } else if (error is NetworkError) {
            errorMesage = getString(R.string.network_error_message)
        }
        Toast.makeText(this@MainActivity, errorMesage, Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        //	производится проверка полученного результата от пользователя на запрос разрешения
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            //	permission	granted
            isWork = (grantResults.size > 1
                    && (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    || grantResults[1] == PackageManager.PERMISSION_GRANTED))
        }
        if (isWork) {
            submitRequest()
        }
    }

    companion object {
        private const val REQUEST_CODE_PERMISSION = 100
    }
}