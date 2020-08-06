package com.surfboardapp.Firebase

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.surfboardapp.Constants
import com.surfboardapp.R

@SuppressLint("Registered")
class MyFirebaseMessagingService : FirebaseMessagingService() {
    private var icon = R.mipmap.ic_launcher
    private var `when` = System.currentTimeMillis()

    override fun onNewToken(token: String) {
        Log.e("tokens", "Refreshed token: $token")

        sendRegistrationToServer()
    }

    private fun sendRegistrationToServer() {
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)


        Log.e("remoteMessage", "From: " + p0.from)

        val title: String
        val body: String
        var type: String = ""
        var room_id: String = ""

        val NOTIFICATION_SOUND_URI = Uri.parse(
            "android.resource://"
                    + applicationContext.packageName + "/"
        )

        if (p0.data.isNotEmpty()) {
            Log.e(
                "remoteMessagesss",
                "Message data payload: " + p0.data
            )
            title = p0.data["title"].toString()
            body = p0.data["message"].toString()
            type = p0.data["type"].toString()
            room_id = p0.data["room"].toString()
        } else {
            title = p0.notification?.title.toString()
            body = p0.notification?.body.toString()
        }
        if (p0.notification != null) {
            Log.e(
                "macro",
                "Message Notification Body: " + p0.notification!!.body
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notification =
                Notification(icon, "Custom Notification", `when`)
            Log.e("NOTIFICATION_SOUND_URI", "" + NOTIFICATION_SOUND_URI)
            val mNotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel =
                NotificationChannel(Constants().channelID, Constants().channelName, importance)
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            mChannel.setSound(NOTIFICATION_SOUND_URI, audioAttributes)
            mChannel.description = Constants().channelDescription
            mChannel.enableLights(true)
            mChannel.lightColor = Color.RED
            mChannel.enableVibration(true)
            mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            mNotificationManager.createNotificationChannel(mChannel)
        } else {
            val notification =
                Notification(icon, "Custom Notification", `when`)
            val mNotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }

        MyNotificationManager.getInstance(baseContext)
            ?.displayNotification(title, body, NOTIFICATION_SOUND_URI, type, room_id)



    }
}