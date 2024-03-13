package com.example.korytingpstracker.main_menu.data.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.location.Location
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceManager
import com.example.korytingpstracker.R
import com.example.korytingpstracker.core.ui.MainActivity
import com.example.korytingpstracker.main_menu.data.dto.LocationDto
import com.example.korytingpstracker.settings.ui.models.AppSettingsPrefKeys
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import org.osmdroid.util.GeoPoint

class LocationService : Service() {
    private var lastLocation: Location? = null
    private var distance = 0.0f
    private lateinit var locProvider: FusedLocationProviderClient
    private lateinit var locRequest: LocationRequest
    private lateinit var geoPointList: ArrayList<GeoPoint>
    private lateinit var sharedPref: SharedPreferences
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locResult: LocationResult) {
            super.onLocationResult(locResult)
            val currentLocation = locResult.lastLocation
            if (lastLocation != null && currentLocation != null) {
                if (currentLocation.speed > ERROR_BOUNDARY_SPEED) {
                    distance += currentLocation.let { lastLocation?.distanceTo(it) } ?: 0.0f
                    geoPointList.add(GeoPoint(currentLocation.latitude, currentLocation.longitude))
                    val locData = LocationDto(
                        speed = currentLocation.speed,
                        distance = distance,
                        geoPointList = geoPointList
                    )
                    sendLocationData(locData)
                }
            }
            lastLocation = currentLocation
            Log.d("MyLog", "Distance: ${distance}")
        }
    }

    private fun sendLocationData(locData: LocationDto) {
        val intent = Intent(LOC_INTENT)
        intent.putExtra(LOC_INTENT, locData)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        stratNotification()
        isRaning = true
        startLocationUpdates()
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        geoPointList = arrayListOf()
        sharedPref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        initLocation()
    }

    override fun onDestroy() {
        super.onDestroy()
        isRaning = false
        startTime = 0L
        locProvider.removeLocationUpdates(locationCallback)
    }

    private fun stratNotification() {
        val notificationChanel = NotificationChannel(
            CHANEL_ID,
            "notification service kolobok",
            NotificationManager.IMPORTANCE_HIGH
        )
        val nManager = getSystemService(NotificationManager::class.java) as NotificationManager
        nManager.createNotificationChannel(notificationChanel)
        val mIntent = Intent(this, MainActivity::class.java)
        val pandingIntent =
            PendingIntent.getActivity(
                this, REQUEST_CODE, mIntent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        val notification = NotificationCompat
            .Builder(this, CHANEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Kolobok running")
            .setContentIntent(pandingIntent)
            .setOngoing(true)
            .build()
        ServiceCompat.startForeground(
            this,
            REQUEST_CODE,
            notification,
            ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
        )
    }

    private fun initLocation() {
        val updateTimeCurrent = sharedPref.getString(
            getString(AppSettingsPrefKeys.TIMEPREFKEY.value),
            resources.getStringArray(R.array.location_time_update_value)[0]
        )
        val updateLocTime = updateTimeCurrent?.toLong() ?: UPDATE_TIME_DEFAULT
        locRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, updateLocTime)
            .setMinUpdateIntervalMillis(updateLocTime)
            .build()
        locProvider = LocationServices.getFusedLocationProviderClient(baseContext)
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locProvider.requestLocationUpdates(locRequest, locationCallback, Looper.myLooper())
    }

    companion object {
        private var isRaning = false
        fun getStateSeervice(): Boolean = isRaning

        private var startTime = 0L
        fun getStartTime(): Long = startTime
        fun setStartTime(time: Long) {
            startTime = time
        }

        const val LOC_INTENT = " loc intent"
        private const val CHANEL_ID = "chanel_kolobok"
        private const val REQUEST_CODE = 10
        private const val ERROR_BOUNDARY_SPEED = 0.3f
        private const val UPDATE_TIME_DEFAULT = 5000L

    }
}