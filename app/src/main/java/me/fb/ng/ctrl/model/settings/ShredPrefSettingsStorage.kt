package me.fb.ng.ctrl.model.settings

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class ShredPrefSettingsStorage @Inject constructor(
    private val context: Application
) : SettingsStorage {

    companion object {
        private const val PREFS_NAME = "settings"
        private const val PREF_LOGIN = "login"
        private const val PREF_PASSWORD = "password"
        private const val PREF_ROUTER_IP = "router_ip"
    }

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    override fun getSettings(): SettingsModel {
        val login = prefs.getString(PREF_LOGIN, null)
        val password = prefs.getString(PREF_PASSWORD, null)
        val routerIp = prefs.getString(PREF_ROUTER_IP, null)
        return if (login == null || password == null || routerIp == null) {
            SettingsModel.DEFAULT
        } else {
            SettingsModel(
                login = login,
                password = password,
                routerIp = routerIp
            )
        }
    }

    override fun saveSettings(settings: SettingsModel) {
        prefs.edit {
            putString(PREF_LOGIN, settings.login)
            putString(PREF_PASSWORD, settings.password)
            putString(PREF_ROUTER_IP, settings.routerIp)
        }
    }
}
