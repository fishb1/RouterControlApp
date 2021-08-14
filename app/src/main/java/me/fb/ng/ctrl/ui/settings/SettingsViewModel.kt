package me.fb.ng.ctrl.ui.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.fb.ng.ctrl.model.settings.SettingsModel
import me.fb.ng.ctrl.model.settings.SettingsRepository
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepo: SettingsRepository
): ViewModel() {

    private val settings = settingsRepo.getSettings()
    val login = MutableLiveData(settings.login)
    val password = MutableLiveData(settings.password)
    val routerIp = MutableLiveData(settings.routerIp)

    fun saveSettings() {
        val login = login.value ?: return
        val password = password.value ?: return
        val routerIp = routerIp.value ?: return
        viewModelScope.launch {
            settingsRepo.saveSettings(SettingsModel(
                login = login,
                password = password,
                routerIp = routerIp
            ))
        }
    }
}
