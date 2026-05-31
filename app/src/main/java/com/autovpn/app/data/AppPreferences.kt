package com.autovpn.app.data

import android.content.Context
import android.content.SharedPreferences

class AppPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "autovpn_prefs"
        private const val KEY_MONITORED_APPS = "monitored_apps"
        private const val KEY_SELECTED_VPN = "selected_vpn"
        private const val KEY_SERVICE_ENABLED = "service_enabled"
    }

    fun getMonitoredApps(): Set<String> {
        return prefs.getStringSet(KEY_MONITORED_APPS, emptySet()) ?: emptySet()
    }

    fun addMonitoredApp(packageName: String) {
        val apps = getMonitoredApps().toMutableSet()
        apps.add(packageName)
        prefs.edit().putStringSet(KEY_MONITORED_APPS, apps).apply()
    }

    fun removeMonitoredApp(packageName: String) {
        val apps = getMonitoredApps().toMutableSet()
        apps.remove(packageName)
        prefs.edit().putStringSet(KEY_MONITORED_APPS, apps).apply()
    }

    fun getSelectedVpnClient(): String? {
        return prefs.getString(KEY_SELECTED_VPN, null)
    }

    fun setSelectedVpnClient(packageName: String) {
        prefs.edit().putString(KEY_SELECTED_VPN, packageName).apply()
    }

    fun isServiceEnabled(): Boolean {
        return prefs.getBoolean(KEY_SERVICE_ENABLED, false)
    }

    fun setServiceEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_SERVICE_ENABLED, enabled).apply()
    }
}
