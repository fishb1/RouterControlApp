package me.fb.ng.ctrl.model.acl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import me.fb.ng.ctrl.data.RouterApi
import me.fb.ng.ctrl.model.common.DeviceModel
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

    private val aclData = MutableStateFlow<WifiAclData?>(null)

    fun getWifiAclData(): Flow<WifiAclData> = aclData.filterNotNull()

    suspend fun updateWifiAclData() {
        val settings = settingsStorage.getSettings()
        val token = getBasicToken(settings.login, settings.password)
        val response = routerApi.getWlAclPage(token)
        aclData.value = WifiAclPageParser.getWifiAclData(response.getBody())
    }

    suspend fun setWifiAclEnabled(enabled: Boolean, timestamp: Long) {
        val settings = settingsStorage.getSettings()
        routerApi.postData(
            token = getBasicToken(settings.login, settings.password),
            action = RouterApi.CONTROL_WIFI_ACCESS_LIST + " timestamp=$timestamp",
            data = WlAclControlRequest(enabled)
        )
    }

    suspend fun deleteDevice(device: DeviceModel, timestamp: Long) {
        val settings = settingsStorage.getSettings()
        routerApi.postData(
            token = getBasicToken(settings.login, settings.password),
            action = RouterApi.CONTROL_WIFI_ACCESS_LIST + " timestamp=$timestamp",
            data = WlAclDeleteDeviceRequest(device.id)
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
