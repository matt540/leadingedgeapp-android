package com.surfboardapp.Firebase

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.surfboardapp.Activity.FirstActivity
import com.surfboardapp.Activity.MainActivity
import com.surfboardapp.Activity.SignUpActivity
import com.surfboardapp.Constants
import com.surfboardapp.Preference.Preference
import com.surfboardapp.R


class MyNotificationManager private constructor(private val mCtx: Context) {
    private lateinit var mBuilder: NotificationCompat.Builder
    @RequiresApi(api = Build.VERSION_CODES.N)
    fun displayNotification(
        title: String?,
        body: String?,
        sound: Uri,
        type: String,
        roomId: String
    ) {
        Log.e("asdasd", "" + sound)
        mBuilder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //            mBuilder.setSound(sound);
                NotificationCompat.Builder(mCtx, Constants().channelID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setSound(sound)
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText(body)
                    )
                    .setContentText(body)

            } else {
                NotificationCompat.Builder(mCtx, Constants().channelID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setSound(sound)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setVibrate(LongArray(0))
                    .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentText(body)

            }

        if (type.equals("chat")) {

            if (!Preference().getLoginFlag(mCtx)) {

                val resultIntent =
                    Intent(mCtx, FirstActivity::class.java).putExtra("notification_room_id", roomId)


                val pendingIntent =
                    PendingIntent.getActivity(
                        mCtx,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )


                mBuilder.setContentIntent(pendingIntent)
            } else {

                val resultIntent = Intent(mCtx, SignUpActivity::class.java)


                val pendingIntent =
                    PendingIntent.getActivity(
                        mCtx,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )


                mBuilder.setContentIntent(pendingIntent)
            }


        } else if (type.equals("canceled_session")) {
            if (!Preference().getLoginFlag(mCtx)) {


                val resultIntent = Intent(mCtx, MainActivity::class.java).putExtra(
                    "canceled_session",
                    "canceled_session"
                )

                val pendingIntent =
                    PendingIntent.getActivity(
                        mCtx,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )


                mBuilder.setContentIntent(pendingIntent)
            } else {

                val resultIntent = Intent(mCtx, SignUpActivity::class.java)


                val pendingIntent =
                    PendingIntent.getActivity(
                        mCtx,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )


                mBuilder.setContentIntent(pendingIntent)
            }
        } else {

            if (!Preference().getLoginFlag(mCtx)) {


                val resultIntent =
                    Intent(mCtx, MainActivity::class.java).putExtra("canceled_session", "")

                val pendingIntent =
                    PendingIntent.getActivity(
                        mCtx,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )


                mBuilder.setContentIntent(pendingIntent)
            } else {

                val resultIntent = Intent(mCtx, SignUpActivity::class.java)


                val pendingIntent =
                    PendingIntent.getActivity(
                        mCtx,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )


                mBuilder.setContentIntent(pendingIntent)
            }


        }


        val mNotifyMgr =
            mCtx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotifyMgr.notify(1, mBuilder.build())
    }

    companion object {
        private var mInstance: MyNotificationManager? = null
        @Synchronized
        fun getInstance(context: Context): MyNotificationManager? {
            if (mInstance == null) {
                mInstance = MyNotificationManager(context)
            }
            return mInstance
        }
    }

}