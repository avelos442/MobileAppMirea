package ru.mirea.laptevavs.mireaproject

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import org.osmdroid.views.overlay.Polyline
import ru.mirea.laptevavs.mireaproject.databinding.FragmentMapBinding
import android.preference.PreferenceManager

class MapFragment : Fragment() {
    private var mapView: MapView? = null
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private val REQUEST_CODE_PERMISSION = 100
    private lateinit var locationNewOverlay: MyLocationNewOverlay
    private lateinit var places: List<Place> // список заведений

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        _binding = FragmentMapBinding.inflate(inflater, container, false)

        // Настройка конфигурации osmdroid
        Configuration.getInstance().load(requireContext(), PreferenceManager.getDefaultSharedPreferences(requireContext()))

        // инициализация карты
        mapView = binding.mapView
        mapView?.apply {
            setTileSource(TileSourceFactory.MAPNIK)  // Убедитесь, что используете правильный провайдер карт
            setZoomRounding(true)
            setMultiTouchControls(true)
            val mapController = controller
            mapController.setZoom(12.0) // Установите начальный уровень зума
            val startPoint = GeoPoint(55.794229, 37.700772)
            mapController.setCenter(startPoint)
        }

        // инициализация списка заведений
        initPlaces()

        // отображение заведений на карте
        showPlacesOnMap()

        // настройка и добавление текущего местоположения
        setupLocationOverlay()

        // отображение маршрута между двумя точками
        showRouteOnMap(GeoPoint(55.794229, 37.700772), GeoPoint(55.789396, 37.536832))

        return binding.root
    }

    private fun initPlaces() {
        places = listOf(
            Place("МИРЭА - Российский технологический университет", 55.794229, 37.700772, "Стромынка 20, РТУ МИРЭА — один из крупнейших российских университетов."),
            Place("Мой дом", 55.789396, 37.536832, "Ходынский больвар 2, Лучшее место в городе!"),
            Place("Школа Танцев", 55.772645, 37.583299, "Большой Кондратьевский переулок, 4с1, Душевная школа танцев со множествой направлений.")
        )
    }

    // отображение заведений на карте
    private fun showPlacesOnMap() {
        places.forEach { place(it) }
    }

    // отображение заведения на карте
    private fun place(place: Place) {
        val marker = Marker(mapView)
        marker.position = GeoPoint(place.latitude, place.longitude)
        marker.title = place.name
        marker.snippet = place.description
        mapView?.overlays?.add(marker)
    }

    // настройка и добавление текущего местоположения
    private fun setupLocationOverlay() {
        val locationProvider = GpsMyLocationProvider(context)
        locationNewOverlay = MyLocationNewOverlay(locationProvider, mapView)
        locationNewOverlay.enableMyLocation()

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_CODE_PERMISSION)
            return
        }

        mapView?.overlays?.add(locationNewOverlay)
        locationNewOverlay.runOnFirstFix {
            val myLocation = locationNewOverlay.myLocation
            myLocation?.let {
                activity?.runOnUiThread {
                    val mapController = mapView?.controller
                    mapController?.setCenter(it)
                }
            }
        }
    }

    // отображение маршрута между двумя точками
    private fun showRouteOnMap(start: GeoPoint, end: GeoPoint) {
        val route = Polyline()
        route.addPoint(start)
        route.addPoint(end)
        route.color = resources.getColor(R.color.purple_500, null)  // Убедитесь, что цвет добавлен в res/values/colors.xml
        mapView?.overlays?.add(route)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // хранение информации о заведении
    data class Place(
        val name: String,
        val latitude: Double,
        val longitude: Double,
        val description: String
    )
}
