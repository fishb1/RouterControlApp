package me.fb.ng.ctrl.model.acl

import me.fb.ng.ctrl.model.common.DeviceModel

data class WifiAclData(
    val timestamp: Long,
    val aclEnabled: Boolean,
    val devices: List<DeviceModel>
)
