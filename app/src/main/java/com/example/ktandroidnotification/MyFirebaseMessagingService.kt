package com.example.ktandroidnotification

import android.util.Log
import com.example.ktandroidnotification.MainActivity.Companion.TAG
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {

    private lateinit var helper: NotificationHelper

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        Log.d(TAG," onMessageReceived ")
        if(remoteMessage?.data!!.isNotEmpty()){
            Log.d(TAG, " Data : " + remoteMessage.data.toString())

        }

        if(remoteMessage.notification != null){
            Log.d(TAG," Notification : " + remoteMessage.notification!!.body.toString())
            val title: String = remoteMessage.notification?.title!!
            val body: String = remoteMessage.notification?.body!!
            helper.displayNotification(applicationContext, title, body)
        }

    }

}
