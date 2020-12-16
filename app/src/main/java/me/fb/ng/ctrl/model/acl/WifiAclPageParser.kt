package me.fb.ng.ctrl.model.acl

import me.fb.ng.ctrl.model.common.DeviceModel
import java.util.*

object WifiAclPageParser {

    fun getWifiAclData(html: String): WifiAclData {
        // Look for devices
        val reg1 = Regex("wlaclArray(\\d+)=\"(.*)\"")
        val result = reg1.findAll(html)
        val devices = result.map {
            val id = it.groupValues[1]
            val (name, mac) = it.groupValues[2].split(" ")
            DeviceModel(
                id,
                name,
                mac.toUpperCase(Locale.getDefault())
            )
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
        return WifiAclData(timestamp, enabled, devices)
    }
}
