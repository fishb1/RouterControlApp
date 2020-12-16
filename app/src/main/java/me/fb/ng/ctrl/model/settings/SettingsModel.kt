package me.fb.ng.ctrl.model.settings

data class SettingsModel(
    val login: String,
    val password: String,
    val routerIp: String
) {

    companion object {
        val DEFAULT = SettingsModel(
            login = "admin",
            password = "password",
            routerIp = "192.168.1.1"
        )
    }
}
