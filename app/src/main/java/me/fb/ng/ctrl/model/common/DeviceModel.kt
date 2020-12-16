package me.fb.ng.ctrl.model.common

data class DeviceModel(
    val id: String,
    val name: String,
    val mac: String,
    val ip: String? = null,
    val selected: Boolean = false
)
