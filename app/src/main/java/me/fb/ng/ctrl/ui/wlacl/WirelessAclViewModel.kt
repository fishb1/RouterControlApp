package me.fb.ng.ctrl.ui.wlacl

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import me.fb.ng.ctrl.model.WifiAclRepository

/**
 * A view model for the control wireless access list screen.
 */
class WirelessAclViewModel @ViewModelInject constructor(
    private val repository: WifiAclRepository
): ViewModel() {

    private val data = repository.getWifiAclData().asLiveData(viewModelScope.coroutineContext)
    private val aclEnabled: LiveData<Boolean> = data.map {
        it.aclEnabled
    }
    val acl: LiveData<String> = data.map {
        it.devices.joinToString("\n")
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
}
