package com.autovpn.app

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.autovpn.app.data.AppPreferences
import com.autovpn.app.data.VpnClients
import com.autovpn.app.ui.theme.AutoVPNTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AutoVPNTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val prefs = remember { AppPreferences(context) }

    var monitoredApps by remember { mutableStateOf(prefs.getMonitoredApps().toList()) }
    var selectedVpn by remember { mutableStateOf(prefs.getSelectedVpnClient()) }
    var serviceEnabled by remember { mutableStateOf(prefs.isServiceEnabled()) }
    var showAppPicker by remember { mutableStateOf(false) }
    var showVpnPicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AutoVPN") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAppPicker = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add app")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Служба мониторинга",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(if (serviceEnabled) "Включена" else "Выключена")
                        Switch(
                            checked = serviceEnabled,
                            onCheckedChange = {
                                if (it) {
                                    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                                    context.startActivity(intent)
                                }
                                serviceEnabled = it
                                prefs.setServiceEnabled(it)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "VPN клиент",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { showVpnPicker = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(selectedVpn?.let {
                            VpnClients.findByPackageName(it)?.name ?: "Выбрать"
                        } ?: "Выбрать VPN клиент")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Отслеживаемые приложения",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(monitoredApps) { packageName ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(packageName)
                            IconButton(onClick = {
                                prefs.removeMonitoredApp(packageName)
                                monitoredApps = prefs.getMonitoredApps().toList()
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Remove")
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAppPicker) {
        AppPickerDialog(
            onDismiss = { showAppPicker = false },
            onAppSelected = { packageName ->
                prefs.addMonitoredApp(packageName)
                monitoredApps = prefs.getMonitoredApps().toList()
                showAppPicker = false
            }
        )
    }

    if (showVpnPicker) {
        VpnPickerDialog(
            onDismiss = { showVpnPicker = false },
            onVpnSelected = { vpnClient ->
                prefs.setSelectedVpnClient(vpnClient.packageName)
                selectedVpn = vpnClient.packageName
                showVpnPicker = false
            }
        )
    }
}

@Composable
fun AppPickerDialog(
    onDismiss: () -> Unit,
    onAppSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val pm = context.packageManager
    val apps = remember {
        pm.getInstalledApplications(0)
            .filter { pm.getLaunchIntentForPackage(it.packageName) != null }
            .sortedBy { pm.getApplicationLabel(it).toString() }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Выберите приложение") },
        text = {
            LazyColumn {
                items(apps) { app ->
                    TextButton(
                        onClick = { onAppSelected(app.packageName) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = pm.getApplicationLabel(app).toString(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}

@Composable
fun VpnPickerDialog(
    onDismiss: () -> Unit,
    onVpnSelected: (com.autovpn.app.data.VpnClient) -> Unit
) {
    val context = LocalContext.current
    val pm = context.packageManager
    val installedVpns = remember {
        VpnClients.SUPPORTED_CLIENTS.filter {
            try {
                pm.getPackageInfo(it.packageName, 0)
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Выберите VPN клиент") },
        text = {
            if (installedVpns.isEmpty()) {
                Text("Не найдено поддерживаемых VPN клиентов")
            } else {
                LazyColumn {
                    items(installedVpns) { vpn ->
                        TextButton(
                            onClick = { onVpnSelected(vpn) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = vpn.name,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}
