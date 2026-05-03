// foreground sync service

package dev.kith.mobile.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import dev.kith.mobile.Core

class SyncService : Service() {
    companion object {
        const val CHANNEL_ID = "kith-sync"
        const val NOTIF_ID = 1
        const val PREF_PEER_ID = "peer_id"
        const val POLL_MS = 500L
    }

    private lateinit var clip: ClipboardSync
    private val handler = Handler(Looper.getMainLooper())
    private var running = false
    private var initOk = false

    private val poll = object : Runnable {
        override fun run() {
            if (!running) return
            try {
                val incoming = Core.pop()
                if (incoming != null) {
                    clip.applyIncoming(incoming)
                }
            } catch (e: Throwable) {
                android.util.Log.w("kith", "pop failed: ${e.message}")
            }
            handler.postDelayed(this, POLL_MS)
        }
    }

    override fun onCreate() {
        super.onCreate()
        ensureChannel()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(NOTIF_ID, buildNotification(), ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
        } else {
            startForeground(NOTIF_ID, buildNotification())
        }
        clip = ClipboardSync(applicationContext)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!initOk) {
            val prefs: SharedPreferences = getSharedPreferences("kith", MODE_PRIVATE)
            val peer = prefs.getString(PREF_PEER_ID, null)
            if (peer.isNullOrBlank()) {
                stopSelf()
                return START_NOT_STICKY
            }
            try {
                Core.start(peer)
                initOk = true
            } catch (e: Throwable) {
                android.util.Log.e("kith", "init failed: ${e.message}")
                stopSelf()
                return START_NOT_STICKY
            }
            clip.start()
            running = true
            handler.post(poll)
        }
        return START_STICKY
    }

    override fun onDestroy() {
        running = false
        handler.removeCallbacks(poll)
        if (this::clip.isInitialized) clip.stop()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun ensureChannel() {
        val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (nm.getNotificationChannel(CHANNEL_ID) == null) {
            nm.createNotificationChannel(
                NotificationChannel(CHANNEL_ID, "kith sync", NotificationManager.IMPORTANCE_LOW)
            )
        }
    }

    private fun buildNotification(): Notification =
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("kith")
            .setContentText("syncing")
            .setSmallIcon(android.R.drawable.stat_notify_sync)
            .setOngoing(true)
            .build()
}
