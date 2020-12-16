package me.fb.ng.ctrl.model.settings

interface SettingsStorage {

    fun getSettings(): SettingsModel

    fun saveSettings(settings: SettingsModel)
}
