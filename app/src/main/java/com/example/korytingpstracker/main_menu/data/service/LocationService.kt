package com.example.korytingpstracker.main_menu.data.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.example.korytingpstracker.R
import com.example.korytingpstracker.core.ui.MainActivity

class LocationService : Service() {


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        stratNotification()
        isRaning = true
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("MyLog", "onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        isRaning = false
        startTime = 0L
        Log.d("MyLog", "onDestroy")
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

    companion object {
        private var isRaning = false
        fun getStateSeervice(): Boolean = isRaning

        private var startTime = 0L
        fun getStartTime(): Long = startTime
        fun setStartTime(time: Long) {
            startTime = time
        }

        const val CHANEL_ID = "chanel_kolobok"
        const val REQUEST_CODE = 10

    }
}