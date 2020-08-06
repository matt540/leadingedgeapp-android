package com.surfboardapp.Firebase

import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId

class MyFirebaseInstanceIdService {
    fun onBind(intent: Intent?): IBinder? {
        val token = FirebaseInstanceId.getInstance().token
        //for now we are displaying the token in the log
//copy it as this method is called only when the new token is generated
//and usually new token is only generated when the app is reinstalled or the data is cleared
        Log.d("MyRefreshedToken", token)
        return null
    }
}