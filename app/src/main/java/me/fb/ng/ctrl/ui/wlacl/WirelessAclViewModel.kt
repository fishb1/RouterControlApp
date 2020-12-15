package me.fb.ng.ctrl.ui.wlacl

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import me.fb.ng.ctrl.model.DeviceModel
import me.fb.ng.ctrl.model.WifiAclRepository

/**
 * A view model for the control wireless access list screen.
 */
class WirelessAclViewModel @ViewModelInject constructor(
    private val repository: WifiAclRepository
): ViewModel(), DeviceListDelegate {

    private val data = repository.getWifiAclData().asLiveData(viewModelScope.coroutineContext)
    private val aclEnabled: LiveData<Boolean> = data.map {
        it.aclEnabled
    }
    private var selectedDevice: DeviceModel? = null
    val deviceList = MediatorLiveData<List<DeviceModel>>().apply {
        addSource(data) {
            value = it.devices.map { device ->
                device.copy(selected = device.id == selectedDevice?.id)
            }
        }
    }
    val btnText = aclEnabled.map {
        if (it) {
            "Disable"
        } else {
            "Enable"
        }
    }
    val busy = MutableLiveData<Boolean>()
    val showMessageEvent = MutableLiveData<String>()

    fun updateData() = viewModelScope.launch {
        busy.postValue(true)
        try {
            repository.updateWifiAclData()
        } catch (e: Exception) {
            showMessageEvent.value = "Error updating data: " + e.message
        }
        busy.postValue(false)
    }

    fun toggleWifiAccessList() {
        viewModelScope.launch {
            busy.postValue(true)
            val data = data.value ?: return@launch
            val enabled = data.aclEnabled
            val timestamp = data.timestamp
            try {
                repository.setWifiAclEnabled(!enabled, timestamp)
                updateData()
            } catch (e: Exception) {
                showMessageEvent.value = "Something went wrong! Try again later!"
            }
            busy.postValue(false)
        }
    }

    override fun onDeviceSelected(device: DeviceModel?) {
        selectedDevice = device
        deviceList.value = deviceList.value?.map {
            it.copy(selected = it.id == device?.id)
        }
    }
}
