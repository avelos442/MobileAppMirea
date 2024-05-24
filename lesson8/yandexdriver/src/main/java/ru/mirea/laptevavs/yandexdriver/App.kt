package ru.mirea.laptevavs.yandexdriver

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class App : Application() {

    companion object {
        private const val MAPKIT_API_KEY = "55324613-dfa2-4723-82b2-223f154ba228"
    }

    override fun onCreate() {
        super.onCreate()

        MapKitFactory.setApiKey(MAPKIT_API_KEY)
    }
}