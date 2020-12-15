package me.fb.ng.ctrl.model

import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import me.fb.ng.ctrl.data.RouterApi
import okhttp3.ResponseBody
import java.io.IOException
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WifiAclRepository @Inject constructor(
    private val routerApi: RouterApi
) {

    companion object {
        const val TOKEN = "Basic YWRtaW46cGFzc3dvcmQ="
    }

    private val aclData = ConflatedBroadcastChannel<WifiAclData>()

    fun getWifiAclData() = aclData.asFlow()

    suspend fun updateWifiAclData() {
        val response = routerApi.getWlAclPage(TOKEN)
        val html = response.getBody()
        // Look for devices
        val reg1 = Regex("wlaclArray(\\d+)=\"(.*)\"")
        val result = reg1.findAll(html)
        val devices = result.map {
            val id = it.groupValues[1]
            val (name, mac) = it.groupValues[2].split(" ")
            DeviceModel(id, name, mac.toUpperCase(Locale.getDefault()))
        }.toList()
        // Look for timestamp
        val reg2 = Regex("var ts='(\\d+)'")
        val res2 = reg2.find(html)
        val timestamp = if (res2 != null && res2.groupValues.size > 1) {
            res2.groupValues[1].toLong()
        } else {
            0L
        }
        // Check box value
        val reg3 = Regex("'1' == '1'")
        val res3 = reg3.find(html)
        val enabled = (res3?.groups?.size ?: 0) > 0
        val data = WifiAclData(timestamp, enabled, devices)
        aclData.send(data)
    }

//        submit_flag=wlacl_apply&
//        select_edit=&
//        select_del=&
//        wl_access_ctrl_on=0&
    suspend fun setWifiAclEnabled(
        enabled: Boolean,
        timestamp: Long
    ): Boolean {
        val data = mutableMapOf(
            "submit_flag" to "wlacl_apply",
            "wl_access_ctrl_on" to if (enabled) "1" else "0"
        )
        routerApi.postData(
            token = TOKEN,
            action = RouterApi.CONTROL_WIFI_ACCESS_LIST + " timestamp=$timestamp",
            data = data
        )
        return true
    }

    private fun ResponseBody.getBody(): String {
        return try {
            string()
        } catch (e: IOException) {
            ""
        }
    }
}
