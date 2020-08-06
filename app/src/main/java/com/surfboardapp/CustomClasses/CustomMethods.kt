package com.surfboardapp.CustomClasses

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.surfboardapp.Activity.LoginActivity
import com.surfboardapp.Activity.MainActivity
import com.surfboardapp.Fragments.*
import com.surfboardapp.Models.CalendarSession
import com.surfboardapp.Models.MapData
import com.surfboardapp.Models.PaymentDetails
import com.surfboardapp.Preference.Preference
import com.surfboardapp.R
import com.surfboardapp.Retrofit.Api
import com.surfboardapp.Retrofit.Connection
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback


class CustomMethods {


    fun getStringFromData(data: JSONObject?, key: String?): String {
        var result = ""
        if (data == null) {
            return ""
        } else {
            if (data.has(key)) {
                try {
                    result = data.getString(key!!)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
        return result

    }

    fun getString(data: JsonObject?, key: String): String {
        var result = ""
        if (data == null) {
            return "nil"
        } else {

            if (data.has(key)) {
                if (!data.isJsonNull) {
                    val v: JsonElement = data.get(key)
                    when {
                        data.get(key).isJsonArray -> {
                            result = data.get(key).asJsonArray.toString()
                        }
                        data.get(key).isJsonObject -> {
                            result = data.get(key).asJsonObject.toString()
                        }
                        v.toString() != "null" -> {
                            result = data.get(key).asString.toString()
                        }
                    }
                }
            }
        }
        return result
    }


    fun hideKeyboard(activity: Activity) {
        try {

            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            var view = activity.currentFocus
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        } catch (e: Exception) {
            Log.e("macro", "" + e)
        }
    }


    fun openPage(
        activity: Activity,
        pagename: String?
    ) {
        when (pagename) {
            MainActivity().calendar -> {
                val mfragment = CalendarFragment()
                (activity as MainActivity).fragmentManager.beginTransaction()
                    .replace(R.id.nav_host, mfragment).commit()
            }
            MainActivity().dashboard -> {
                val mfragment = DashboardFragment()
                (activity as MainActivity).fragmentManager.beginTransaction()
                    .replace(R.id.nav_host, mfragment).commit()
            }
            MainActivity().locationFinder -> {
                val mfragment =
                    LocationFinderFragment()
                (activity as MainActivity).fragmentManager.beginTransaction()
                    .replace(R.id.nav_host, mfragment).commit()
            }
            MainActivity().myAccount -> {
                val mfragment = MyAccountFragment()
                (activity as MainActivity).fragmentManager.beginTransaction()
                    .replace(R.id.nav_host, mfragment).commit()
            }
            MainActivity().resetPassword -> {
                val mfragment = ResetPasswordFragment()
                (activity as MainActivity).fragmentManager.beginTransaction()
                    .replace(R.id.nav_host, mfragment).commit()
            }

            MainActivity().locationFinderInner -> {
                val mfragment = LocationFinderInnerFragment()
                (activity as MainActivity).fragmentManager.beginTransaction()
                    .replace(R.id.nav_host, mfragment).commit()
            }
            MainActivity().sessionPaymentFragment -> {
                val mfragment = SessionPaymentFragment()
                (activity as MainActivity).fragmentManager.beginTransaction()
                    .replace(R.id.nav_host, mfragment).commit()
            }

            MainActivity().progressionsFragment -> {
                val mfragment = ProgressionModel()
                (activity as MainActivity).fragmentManager.beginTransaction()
                    .replace(R.id.nav_host, mfragment).commit()
            }
            MainActivity().progressionModel -> {
                val mfragment = ProgressionsFragment()
                (activity as MainActivity).fragmentManager.beginTransaction()
                    .replace(R.id.nav_host, mfragment).commit()
            }

            MainActivity().getCertified -> {
                val mfragment = GetCertifiedFragment()
                (activity as MainActivity).fragmentManager.beginTransaction()
                    .replace(R.id.nav_host, mfragment).commit()
            }

            MainActivity().buyABoard -> {
                val mfragment = BuyABoardFragment()
                (activity as MainActivity).fragmentManager.beginTransaction()
                    .replace(R.id.nav_host, mfragment).commit()
            }

            MainActivity().videos -> {
                val mfragment = VideosFragment()
                (activity as MainActivity).fragmentManager.beginTransaction()
                    .replace(R.id.nav_host, mfragment).commit()
            }

            MainActivity().chatFragment -> {
                val mfragment = ChatFragment()
                (activity as MainActivity).fragmentManager.beginTransaction()
                    .replace(R.id.nav_host, mfragment).commit()
            }

            MainActivity().normalSession -> {
                val mfragment = NormalSessionFragment()
                (activity as MainActivity).fragmentManager.beginTransaction()
                    .replace(R.id.nav_host, mfragment).commit()
            }

            MainActivity().membership -> {
                val mfragment = MembershipFragment()
                (activity as MainActivity).fragmentManager.beginTransaction()
                    .replace(R.id.nav_host, mfragment).commit()
            }
            MainActivity().newCard -> {
                val mfragment = AddnewcardFragment()
                (activity as MainActivity).fragmentManager.beginTransaction()
                    .replace(R.id.nav_host, mfragment).commit()
            }

//            MainActivity().settings -> {
//                val mfragment = SettingsFragment()
//                (activity as MainActivity).fragmentManager.beginTransaction()
//                    .replace(R.id.nav_host, mfragment).commit()
//            }


        }
    }


    //Store marker details into model
    fun openPage(
        activity: Activity,
        pagename: String?, title: MapData
    ) {
        when (pagename) {

            MainActivity().locationFinderInner -> {
                val mfragment = LocationFinderInnerFragment()
                val a = Bundle()
                a.putSerializable("model", title)
                mfragment.arguments = a
                (activity as MainActivity).fragmentManager.beginTransaction()
                    .replace(R.id.nav_host, mfragment).commit()
            }
        }
    }


    //Send session Id
    fun openPage(
        activity: Activity,
        pagename: String?, sessionId: CalendarSession
    ) {
        when (pagename) {

            MainActivity().chatFragment -> {
                val mfragment = ChatFragment()
                (activity as MainActivity).fragmentManager.beginTransaction()
                    .replace(R.id.nav_host, mfragment).commit()

                val a = Bundle()
                a.putSerializable("model", sessionId)
                mfragment.arguments = a
            }
        }
    }

    //store payment details into model


    fun openPagePaymentDetails(
        activity: Activity,
        pagename: String?, title: PaymentDetails
    ) {

        if (pagename == MainActivity().sessionPaymentFragment) {
            when (pagename) {

                MainActivity().sessionPaymentFragment -> {
                    val mfragment = SessionPaymentFragment()
                    (activity as MainActivity).fragmentManager.beginTransaction()
                        .replace(R.id.nav_host, mfragment).commit()

                    val a = Bundle()
                    a.putSerializable("model", title)
                    mfragment.arguments = a
                }
            }
        } else if (pagename == MainActivity().progressionsFragment) {
            when (pagename) {

                MainActivity().progressionsFragment -> {
                    val mfragment = ProgressionModel()
                    (activity as MainActivity).fragmentManager.beginTransaction()
                        .replace(R.id.nav_host, mfragment).commit()

                    val a = Bundle()
                    a.putSerializable("model", title)
                    mfragment.arguments = a
                }
            }
        }
    }


    fun logOutUser(context: Context) {
        Preference().removeKeepLoginData(context)
        Preference().setLoginFlag(context, true)
        Preference().setPrivacyPolicy(context, true)
        context.startActivity(Intent(context, LoginActivity::class.java))
        (context as Activity).finish()


        val api: Api = Connection().getCon(context)
        api.getNotificationUserToken(
            "Bearer " + Preference().getUserData(context)?.token,
            "",
            "android"
        ).enqueue(object : Callback, retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                Log.e("macro", "respN->" + response.body())

                val data = response.body()!!.get("data").asJsonObject
                if (data.has("id")) {
                    if (!data.get("id").isJsonNull) {

                        val id = data.get("id").asString
                    }
                }
            }
        })
    }
}