package me.fb.ng.ctrl.model.settings

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    private val storage: SettingsStorage
) {

    fun getSettings() = storage.getSettings()

    suspend fun saveSettings(settings: SettingsModel) = withContext(Dispatchers.IO) {
        storage.saveSettings(settings)
    }
}
