package me.fb.ng.ctrl.ui.wlacl

import me.fb.ng.ctrl.model.common.DeviceModel

interface DeviceListDelegate {

    fun onDeviceSelected(device: DeviceModel?)
}
