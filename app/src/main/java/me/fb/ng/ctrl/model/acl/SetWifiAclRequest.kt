package me.fb.ng.ctrl.model.acl

class SetWifiAclRequest(
    enabled: Boolean
) : HashMap<String, String>() {

    init {
        put("submit_flag", "wlacl_apply")
        put("wl_access_ctrl_on", if (enabled) "1" else "0")
    }
}
