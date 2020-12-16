package me.fb.ng.ctrl.model.acl

import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import me.fb.ng.ctrl.data.RouterApi
import me.fb.ng.ctrl.model.getBasicToken
import me.fb.ng.ctrl.model.settings.SettingsStorage
import okhttp3.ResponseBody
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WifiAclRepository @Inject constructor(
    private val routerApi: RouterApi,
    private val settingsStorage: SettingsStorage
) {

    private val aclData = ConflatedBroadcastChannel<WifiAclData>()

    fun getWifiAclData() = aclData.asFlow()

    suspend fun updateWifiAclData() {
        val settings = settingsStorage.getSettings()
        val token = getBasicToken(settings.login, settings.password)
        val response = routerApi.getWlAclPage(token)
        aclData.send(WifiAclPageParser.getWifiAclData(response.getBody()))
    }

    suspend fun setWifiAclEnabled(enabled: Boolean, timestamp: Long) {
        val settings = settingsStorage.getSettings()
        routerApi.postData(
            token = getBasicToken(settings.login, settings.password),
            action = RouterApi.CONTROL_WIFI_ACCESS_LIST + " timestamp=$timestamp",
            data = SetWifiAclRequest(enabled)
        )
    }

    private fun ResponseBody.getBody(): String {
        return try {
            string()
        } catch (e: IOException) {
            ""
        }
    }
}
