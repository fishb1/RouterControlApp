package me.fb.ng.ctrl.data

import okhttp3.ResponseBody
import retrofit2.http.*

interface RouterApi {

    companion object {
        const val HEADER_AUTH = "Authorization"
        const val CONTROL_WIFI_ACCESS_LIST = "apply.cgi?/WLG_acl.htm"
    }

    @FormUrlEncoded
    @POST
    suspend fun postData(
        @Header(HEADER_AUTH) token: String,
        @Url action: String,
        @FieldMap data: Map<String, String>
    ): ResponseBody

    @GET("WLG_acl.htm")
    suspend fun getWlAclPage(
        @Header(HEADER_AUTH) token: String
    ): ResponseBody
}
