package com.autovpn.app.data

data class VpnClient(
    val name: String,
    val packageName: String,
    val startAction: String? = null
)

object VpnClients {
    val SUPPORTED_CLIENTS = listOf(
        VpnClient(
            name = "Hiddify",
            packageName = "com.hiddify.app",
            startAction = "com.hiddify.app.START_VPN"
        ),
        VpnClient(
            name = "v2rayNG",
            packageName = "com.v2ray.ang",
            startAction = "com.v2ray.ang.action.START_V2RAY"
        ),
        VpnClient(
            name = "Shadowsocks",
            packageName = "com.github.shadowsocks",
            startAction = "com.github.shadowsocks.action.CONNECT"
        ),
        VpnClient(
            name = "Clash for Android",
            packageName = "com.github.kr328.clash",
            startAction = null
        ),
        VpnClient(
            name = "Surfshark",
            packageName = "com.surfshark.vpnclient.android",
            startAction = null
        )
    )

    fun findByPackageName(packageName: String): VpnClient? {
        return SUPPORTED_CLIENTS.find { it.packageName == packageName }
    }
}
