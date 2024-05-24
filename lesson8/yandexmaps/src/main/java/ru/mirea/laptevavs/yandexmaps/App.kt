package ru.mirea.laptevavs.yandexmaps

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class App : Application() {
    private val MAPKIT_API_KEY = "55324613-dfa2-4723-82b2-223f154ba228"
    override fun onCreate() {
        super.onCreate()
        // Set the api key before calling initialize on MapKitFactory.
        MapKitFactory.setApiKey(MAPKIT_API_KEY)
    }
}