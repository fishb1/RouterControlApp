package me.fb.ng.ctrl.ui.wlacl

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.fb.ng.ctrl.model.common.DeviceModel
import me.fb.ng.ctrl.model.acl.WifiAclRepository
import java.util.concurrent.TimeUnit

/**
 * A view model for the control wireless access list screen.
 */
class WirelessAclViewModel @ViewModelInject constructor(
    private val repository: WifiAclRepository
): ViewModel(), DeviceListDelegate {

    companion object {
        private val DATA_TTL = TimeUnit.SECONDS.toMillis(30)
    }

    private val data = repository.getWifiAclData().asLiveData(viewModelScope.coroutineContext)
    val aclEnabled: LiveData<Boolean> = data.map {
        it.aclEnabled
    }
    private var selectedDevice: DeviceModel? = null
    val deviceList = MediatorLiveData<List<DeviceModel>>().apply {
        addSource(data) {
            value = updateSelectedDevice(it.devices, selectedDevice)
        }
    }
    val busy = MutableLiveData<Boolean>()
    val showMessageEvent = MutableLiveData<String>()
    private var lastUpdateTime: Long = 0

    fun updateData() = viewModelScope.launch {
        busy.postValue(true)
        if (System.currentTimeMillis() - lastUpdateTime > DATA_TTL) {
            try {
                repository.updateWifiAclData()
                lastUpdateTime = System.currentTimeMillis()
            } catch (e: Exception) {
                showMessageEvent.value = "Error updating data: " + e.message
            }
        }
        busy.postValue(false)
    }

    fun toggleWifiAccessList() {
        viewModelScope.launch {
            busy.postValue(true)
            val data = data.value
            if (data != null) {
                val enabled = data.aclEnabled
                val timestamp = data.timestamp
                try {
                    repository.setWifiAclEnabled(!enabled, timestamp)
                    updateData()
                } catch (e: Exception) {
                    showMessageEvent.value = "Something went wrong! Try again later!"
                }
            }
            busy.postValue(false)
        }
    }

    override fun onDeviceSelected(device: DeviceModel?) {
        selectedDevice = device
        deviceList.value = updateSelectedDevice(deviceList.value, device)
    }

    override fun deleteDevice(device: DeviceModel) {
        viewModelScope.launch {
            busy.postValue(true)
            val data = data.value
            if (data != null) {
                val timestamp = data.timestamp
                try {
                    repository.deleteDevice(device, timestamp)
                    delay(3000)
                    updateData()
                } catch (e: Exception) {
                    showMessageEvent.value = "Something went wrong! Try again later!"
                }
            }
            busy.postValue(false)
        }
    }

    private fun updateSelectedDevice(
        deviceList: List<DeviceModel>?,
        selectedDevice: DeviceModel?
    ): List<DeviceModel> {
        return deviceList?.map {
            it.copy(selected = it.id == selectedDevice?.id)
        } ?: emptyList()
    }
}
