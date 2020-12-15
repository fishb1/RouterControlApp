package me.fb.ng.ctrl.model

data class WifiAclData(
    val timestamp: Long,
    val aclEnabled: Boolean,
    val devices: List<DeviceModel>
)
