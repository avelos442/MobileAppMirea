package ru.mirea.laptevavs.osmmaps

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import org.osmdroid.config.Configuration
import org.osmdroid.library.R
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.ScaleBarOverlay
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import ru.mirea.laptevavs.osmmaps.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private var mapView: MapView? = null
    private var binding: ActivityMainBinding? = null
    var isWork = false
    var locationNewOverlay: MyLocationNewOverlay? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            isWork = true
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                REQUEST_CODE_PERMISSION
            )
        }
        Configuration.getInstance().load(
            applicationContext,
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )
        mapView = binding!!.mapView
        mapView!!.setZoomRounding(true)
        mapView!!.setMultiTouchControls(true)
        val mapController = mapView!!.controller
        mapController.setZoom(15.0)
        val startPoint = GeoPoint(55.794229, 37.700772)
        mapController.setCenter(startPoint)
        locationNewOverlay =
            MyLocationNewOverlay(GpsMyLocationProvider(applicationContext), mapView)
        locationNewOverlay!!.enableMyLocation()
        mapView!!.overlays.add(locationNewOverlay)
        val compassOverlay = CompassOverlay(
            applicationContext, InternalCompassOrientationProvider(
                applicationContext
            ), mapView
        )
        compassOverlay.enableCompass()
        mapView!!.overlays.add(compassOverlay)
        val context = this.applicationContext
        val dm = context.resources.displayMetrics
        val scaleBarOverlay = ScaleBarOverlay(mapView)
        scaleBarOverlay.setCentred(true)
        scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10)
        mapView!!.overlays.add(scaleBarOverlay)
        place("МИРЭА - Российский техногологический университет", 55.794259, 37.701448)
        place("Ходынское поле", 55.787918, 37.530346)
        place("Красная площадь", 55.753544, 37.621202)
        val marker = Marker(mapView)
        marker.position = GeoPoint(55.794229, 37.700772)
        marker.setOnMarkerClickListener { marker, mapView ->
            Toast.makeText(
                applicationContext, "Click",
                Toast.LENGTH_SHORT
            ).show()
            true
        }
        mapView!!.overlays.add(marker)
        marker.icon = ResourcesCompat.getDrawable(resources, R.drawable.osm_ic_follow_me_on, null)
        marker.title = "Title"
    }

    fun place(text: String?, aLatitude: Double?, aLongitude: Double?) {
        val marker = Marker(mapView)
        marker.position = GeoPoint(aLatitude!!, aLongitude!!)
        marker.setOnMarkerClickListener { marker, mapView ->
            Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
            true
        }
        mapView!!.overlays.add(marker)
    }

    public override fun onResume() {
        super.onResume()
        Configuration.getInstance().load(
            applicationContext,
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )
        if (mapView != null) {
            mapView!!.onResume()
        }
    }

    public override fun onPause() {
        super.onPause()
        Configuration.getInstance().save(
            applicationContext,
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )
        if (mapView != null) {
            mapView!!.onPause()
        }
    }

    companion object {
        private const val REQUEST_CODE_PERMISSION = 100
    }
}