package me.fb.ng.ctrl.model

import android.util.Base64

fun getBasicToken(login: String, password: String): String {
    val toEncode = "$login:$password".encodeToByteArray()
    return "Basic %s".format(String(Base64.encode(toEncode, Base64.NO_WRAP)))
}
