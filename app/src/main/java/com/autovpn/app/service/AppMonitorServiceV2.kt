package com.autovpn.app.service

import android.app.Service
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import com.autovpn.app.data.AppPreferences
import com.autovpn.app.data.VpnClients

class AppMonitorServiceV2 : Service() {

    private lateinit var prefs: AppPreferences
    private lateinit var usageStatsManager: UsageStatsManager
    private val handler = Handler(Looper.getMainLooper())
    private var lastTriggeredApp: String? = null
    private var lastTriggerTime: Long = 0
    private var isRunning = false

    companion object {
        private const val TAG = "AppMonitorServiceV2"
        private const val TRIGGER_COOLDOWN_MS = 5000L
        private const val CHECK_INTERVAL_MS = 1000L
    }

    override fun onCreate() {
        super.onCreate()
        prefs = AppPreferences(this)
        usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        Log.d(TAG, "Service created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!isRunning) {
            isRunning = true
            startMonitoring()
            Log.d(TAG, "Service started")
        }
        return START_STICKY
    }

    private fun startMonitoring() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (isRunning && prefs.isServiceEnabled()) {
                    checkForegroundApp()
                    handler.postDelayed(this, CHECK_INTERVAL_MS)
                }
            }
        }, CHECK_INTERVAL_MS)
    }

    private fun checkForegroundApp() {
        val currentTime = System.currentTimeMillis()
        val events = usageStatsManager.queryEvents(currentTime - 2000, currentTime)

        var lastEvent: UsageEvents.Event? = null
        while (events.hasNextEvent()) {
            val event = UsageEvents.Event()
            events.getNextEvent(event)

            if (event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                lastEvent = event
            }
        }

        lastEvent?.let { event ->
            val packageName = event.packageName
            val monitoredApps = prefs.getMonitoredApps()

            if (packageName in monitoredApps) {
                if (packageName == lastTriggeredApp &&
                    currentTime - lastTriggerTime < TRIGGER_COOLDOWN_MS) {
                    return
                }

                Log.d(TAG, "Detected monitored app: $packageName")
                startVpnClient()

                lastTriggeredApp = packageName
                lastTriggerTime = currentTime
            }
        }
    }

    private fun startVpnClient() {
        val vpnPackageName = prefs.getSelectedVpnClient() ?: return
        val vpnClient = VpnClients.findByPackageName(vpnPackageName)

        try {
            val intent = if (vpnClient?.startAction != null) {
                Intent(vpnClient.startAction).apply {
                    setPackage(vpnPackageName)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            } else {
                packageManager.getLaunchIntentForPackage(vpnPackageName)?.apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            }

            intent?.let {
                startActivity(it)
                Log.d(TAG, "Started VPN client: $vpnPackageName")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start VPN client", e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        handler.removeCallbacksAndMessages(null)
        Log.d(TAG, "Service destroyed")
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
