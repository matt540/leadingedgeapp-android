package com.surfboardapp.Preference


import android.content.Context
import android.content.SharedPreferences
import com.google.gson.JsonObject
import com.surfboardapp.CustomClasses.CustomMethods
import com.surfboardapp.Models.UserData
import org.json.JSONObject


class Preference {

    private lateinit var preferences: SharedPreferences
    fun getLoginFlag(context: Context): Boolean {
        preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        return preferences.getBoolean("keeplogin", true)
    }

    fun setLoginFlag(context: Context, value: Boolean) {
        preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        preferences.edit().putBoolean("keeplogin", value).apply()
    }

    fun getPrivacyPolicy(context: Context): Boolean {
        preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        return preferences.getBoolean("keepPrivacy", true)
    }

    fun setPrivacyPolicy(context: Context, value: Boolean) {
        preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        preferences.edit().putBoolean("keepPrivacy", value).apply()
    }

    fun setUserData(context: Context, userData: UserData) {
        preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

        val value = JSONObject()
        value.put("id", userData.id)
        value.put("name", userData.name)
        value.put("email", userData.email)
        value.put("zipcode", userData.zipcode)
        value.put("image", userData.image)
        value.put("address", userData.address)
        value.put("city", userData.city)
        value.put("token", getUserToken(context))
        value.put("state", userData.state)
        value.put("country", userData.country)
        value.put("role", userData.role)
        value.put("discover_lesson_status", userData.discover_lesson_status)
        value.put("progression_model_status", userData.progression_model_status)
        value.put("certificate_status", userData.certificate_status)
        value.put("customer_id", userData.customer_id)
        value.put("wavier_accepted", userData.wavier_accepted)



        preferences.edit().putString("LoginData", value.toString()).apply()
    }

    fun getUserData(context: Context): UserData? {
        preferences =
            context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        try {
            val loginObject = JSONObject(preferences.getString("LoginData", "{}")!!)
            if (loginObject.has("name")) {
                val customMethods = CustomMethods()
                return UserData(
                    customMethods.getStringFromData(loginObject, "id"),
                    customMethods.getStringFromData(loginObject, "name"),
                    customMethods.getStringFromData(loginObject, "email"),
                    customMethods.getStringFromData(loginObject, "image"),
                    customMethods.getStringFromData(loginObject, "zipcode"),
                    customMethods.getStringFromData(loginObject, "token"),
                    customMethods.getStringFromData(loginObject, "role"),
                    customMethods.getStringFromData(loginObject, "address"),
                    customMethods.getStringFromData(loginObject, "city"),
                    customMethods.getStringFromData(loginObject, "state"),
                    customMethods.getStringFromData(loginObject, "country"),
                    customMethods.getStringFromData(loginObject, "certificate_status"),
                    customMethods.getStringFromData(loginObject, "progression_model_status"),
                    customMethods.getStringFromData(loginObject, "discover_lesson_status"),
                    CustomMethods().getStringFromData(loginObject, "customer_id"),
                    customMethods.getStringFromData(loginObject, "wavier_accepted")
                )
            } else {
                return null
            }
        } catch (e: Exception) {
            return null
        }
    }

    fun removeKeepLoginData(context: Context) {
        preferences =
            context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        preferences.edit().remove("keeplogin").apply()
        preferences.edit().remove("password").apply()
        preferences.edit().remove("token").apply()
        preferences.edit().remove("LoginData").apply()
        preferences.edit().remove("keepPrivacy").apply()
        preferences.edit().remove("stripe").apply()
    }

    fun setUserPassword(context: Context, password: String) {
        preferences =
            context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        preferences.edit().putString("password", password).apply()
    }

    fun getUserPassword(context: Context): String {
        preferences =
            context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        return preferences.getString("password", "")!!
    }

    fun setUserToken(context: Context, token: String) {
        preferences =
            context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        preferences.edit().putString("token", token).apply()
    }

    fun getUserToken(context: Context): String {
        preferences =
            context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        return preferences.getString("token", "")!!
    }

    fun setNewLat(context: Context, lat: String) {
        preferences =
            context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        preferences.edit().putString("lat", lat).apply()
    }

    fun getNewLat(context: Context): String {
        preferences =
            context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        return preferences.getString("lat", "")!!
    }

    fun setNewLong(context: Context, long: String) {
        preferences =
            context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        preferences.edit().putString("long", long).apply()
    }

    fun getNewLong(context: Context): String {
        preferences =
            context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        return preferences.getString("long", "")!!
    }

    fun setRange(context: Context, range: String) {
        preferences =
            context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        preferences.edit().putString("range", range).apply()
    }

    fun getRange(context: Context): String {
        preferences =
            context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        return preferences.getString("range", "")!!
    }

    fun getConfirmPaymentResponse(context: Context): String {
        preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        return preferences.getString("response", "")!!
    }

    fun setConfirmPaymentResponse(context: Context, response: JsonObject?) {
        preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        preferences.edit().putString("response", response.toString()).apply()
    }

    fun gettokenId(context: Context): String {
        preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        return preferences.getString("tokenId", "")!!
    }

    fun settokenId(context: Context, tokenId: String) {
        preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        preferences.edit().putString("tokenId", tokenId).apply()
    }


    fun getSelectedLanguage(context: Context): String {
        preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        return preferences.getString("language", "en")!!
    }

    fun setSelectedLanguage(context: Context, selectedLanguage: String) {
        preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        preferences.edit().putString("language", selectedLanguage).apply()
    }

    fun getStringStripeKey(context: Context): String {
        preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        return preferences.getString("stripe", "en")!!
    }

    fun setStringStripeKey(context: Context, selectedKey: String) {
        preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        preferences.edit().putString("stripe", selectedKey).apply()
    }

}

