package me.fb.ng.ctrl.model.acl

class WlAclDeleteDeviceRequest(
    id: String
) : HashMap<String, String>() {

    init {
        put("submit_flag", "wlacl_del")
        put("select_del", id)
        put("MacSelect", id)
    }
}
