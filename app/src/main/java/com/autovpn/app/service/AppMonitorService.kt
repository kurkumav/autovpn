package com.autovpn.app.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.autovpn.app.data.AppPreferences
import com.autovpn.app.data.VpnClients

class AppMonitorService : AccessibilityService() {

    private lateinit var prefs: AppPreferences
    private var lastTriggeredApp: String? = null
    private var lastTriggerTime: Long = 0

    companion object {
        private const val TAG = "AppMonitorService"
        private const val TRIGGER_COOLDOWN_MS = 5000L
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        prefs = AppPreferences(this)
        Log.d(TAG, "Service connected")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return
        if (!prefs.isServiceEnabled()) return

        val packageName = event.packageName?.toString() ?: return
        val monitoredApps = prefs.getMonitoredApps()

        if (packageName in monitoredApps) {
            val currentTime = System.currentTimeMillis()

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

    override fun onInterrupt() {
        Log.d(TAG, "Service interrupted")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Service destroyed")
    }
}
