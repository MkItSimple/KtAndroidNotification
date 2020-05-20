package com.example.ktandroidnotification

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Api {

    @FormUrlEncoded
    @POST("send")
    fun sendNotification(
        @Field("email") email: String,
        @Field("password") password: String
    ) : Call<ResponseBody>
}