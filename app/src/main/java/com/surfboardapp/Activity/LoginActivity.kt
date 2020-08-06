package com.surfboardapp.Activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.surfboardapp.CustomClasses.CustomLoader
import com.surfboardapp.CustomClasses.CustomMethods
import com.surfboardapp.Language.AppUtils
import com.surfboardapp.Models.UserData
import com.surfboardapp.Preference.Preference
import com.surfboardapp.R
import com.surfboardapp.Retrofit.Api
import com.surfboardapp.Retrofit.Connection
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.util.*
import javax.security.auth.callback.Callback

class LoginActivity : AppCompatActivity() {

    private var validate: Boolean = false
    private lateinit var call: Call<JsonObject>
    private lateinit var back: ImageView
    private lateinit var btnLogin: Button
    private lateinit var tvForgotPassword: TextView
    private lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    var pressedTwice = false
    lateinit var view: View
    lateinit var roleName: String
    lateinit var idCheckCustomerApi: String
    lateinit var id: String
    lateinit var name: String
    lateinit var email: String
    lateinit var zipcodeCheckCustomerApi: String
    lateinit var image: String
    lateinit var address: String
    lateinit var cityCheckCustomerApi: String
    lateinit var state: String
    lateinit var country: String
    lateinit var discoverLessonStatusCheckCustomerApi: String
    lateinit var progressionModelStatus: String
    lateinit var certificateStatusStatusCheckCustomerApi: String
    lateinit var customerId1: String
    lateinit var wavier_accepted: String
    lateinit var role: JsonArray
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Preference().getSelectedLanguage(this)
        AppUtils().setLocale(Locale(Preference().getSelectedLanguage(this)))
        AppUtils().setConfigChange(this)
        setContentView(R.layout.activity_login)

        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = Firebase.analytics
        init()
        onClick()
    }

    private fun init() {
        view = findViewById(android.R.id.content)
        back = findViewById(R.id.back)
        btnLogin = findViewById(R.id.btn_login)
        tvForgotPassword = findViewById(R.id.tv_forgotPassword)
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
    }

    private fun onClick() {

        back.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
            finish()
        }
        tvForgotPassword.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
            finish()
        }
        btnLogin.setOnClickListener {
            if (!validate()) {
                //Login User
                getLogin()
            }
        }
    }

    private fun validate(): Boolean {
        if (etEmail.text.toString().isEmpty()) {
            etEmail.error = getString(R.string.enterName)
            validate = true
        } else if (etPassword.text.toString().isEmpty()) {
            etPassword.error = getString(R.string.enterEmail)
            validate = true
        }
        return validate
    }

    private fun getLogin() {

        val dialog: Dialog = CustomLoader().loader(this)
        val api: Api = Connection().getCon(this as android.content.Context)

        call = api.getLogin(etEmail.text.toString(), etPassword.text.toString())
        call.enqueue(object : Callback, retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("error", "" + t)
                CustomLoader().stopLoader(dialog)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                CustomLoader().stopLoader(dialog)
                try {
                    if (response.isSuccessful) {
                        if (response.body() != null) {

                            Log.e("responselogin", "" + response.body())
                            val data = response.body()!!.get("data").asJsonObject
                            val user = data!!.get("user").asJsonObject
                            val id = user.get("id").asString

                            val name = user.get("name").asString
                            val email = user.get("email").asString
                            val image = user.get("image").asString
                            val zipcode = user.get("zipcode").asString
                            val token = data.get("token").asString
                            val role = user.get("role").asString
                            val address = user.get("address").asString
                            val city = user.get("city").asString
                            val state = user.get("state").asString
                            val country = user.get("country").asString
                            val certificateStatus = user.get("certificate_status").asString
                            val progressionModelStatus =
                                user.get("progression_model_status").asString
                            val discoverLessonStatus = user.get("discover_lesson_status").asString
                            val customerId = user.get("customer_id").asString
                            val wavier_accepted = user.get("wavier_accepted").asString


                            Preference().setLoginFlag(this@LoginActivity, false)
                            Preference().setUserToken(this@LoginActivity, token)
                            Preference().setUserPassword(
                                this@LoginActivity,
                                etPassword.text.toString()
                            )
                            Preference().setUserData(
                                this@LoginActivity,
                                UserData(
                                    id,
                                    name,
                                    email,
                                    image,
                                    zipcode,
                                    token,
                                    role,
                                    address,
                                    city,
                                    state,
                                    country,
                                    certificateStatus,
                                    progressionModelStatus,
                                    discoverLessonStatus, customerId, wavier_accepted
                                )
                            )

                            if (customerId == "") {
                                createStripeCustomer()
                            }
                            startActivity(
                                Intent(
                                    applicationContext,
                                    MainActivity::class.java
                                ).putExtra("canceled_session", "")
                            )
                            finish()
                        }
                    } else {
                        val jsonObject = JSONObject(response.errorBody()!!.string())
                        val message = jsonObject.getString("message")
                        Toast.makeText(this@LoginActivity, "" + message, Toast.LENGTH_LONG).show()
//                        et_email.setText("")
//                        et_password.setText("")
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@LoginActivity, "" + e, Toast.LENGTH_LONG).show()
                }

            }
        })
    }

    private fun createStripeCustomer() {
//        val dialog: Dialog = CustomLoader().loader(this)

        //Check Customer id is there or not
        val api: Api = Connection().getCon(this)
        call = api.checkCustomerId("Bearer " + Preference().getUserData(this)?.token)
        Log.e("macro", "asd->" + Preference().getUserData(this)?.token)

        call.enqueue(object : Callback, retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                try {
                    if (response.isSuccessful) {
                        if (response.body() != null) {


                            val data = response.body()!!.get("data").asJsonObject

                            if (data.has("id")) {
                                if (!data.get("id").isJsonNull) {

                                    idCheckCustomerApi = data.get("id").asString
                                }
                            }
                            if (data.has("name")) {
                                if (!data.get("name").isJsonNull) {

                                    name = data.get("name").asString
                                }
                            }
                            if (data.has("email")) {
                                if (!data.get("email").isJsonNull) {

                                    email = data.get("email").asString
                                }
                            }
                            if (data.has("zipcode")) {
                                if (!data.get("zipcode").isJsonNull) {

                                    zipcodeCheckCustomerApi = data.get("zipcode").asString
                                }
                            }
                            if (data.has("image")) {
                                if (!data.get("image").isJsonNull) {

                                    image = data.get("image").asString
                                }
                            }
                            if (data.has("address")) {
                                if (!data.get("address").isJsonNull) {

                                    address = data.get("address").asString
                                }
                            }

                            if (data.has("city")) {
                                if (!data.get("city").isJsonNull) {

                                    cityCheckCustomerApi = data.get("city").asString

                                }
                            }
                            if (data.has("state")) {
                                if (!data.get("state").isJsonNull) {

                                    state = data.get("state").asString
                                }
                            }
                            if (data.has("country")) {
                                if (!data.get("country").isJsonNull) {

                                    country = data.get("country").asString
                                }
                            }

                            if (data.has("discover_lesson_status")) {
                                if (!data.get("discover_lesson_status").isJsonNull) {

                                    discoverLessonStatusCheckCustomerApi =
                                        data.get("discover_lesson_status").asString
                                }
                            }
                            if (data.has("progression_model_status")) {
                                if (!data.get("progression_model_status").isJsonNull) {
                                    progressionModelStatus =
                                        data.get("progression_model_status").asString
                                }
                            }
                            if (data.has("certificate_status")) {
                                if (!data.get("certificate_status").isJsonNull) {
                                    certificateStatusStatusCheckCustomerApi =
                                        data.get("certificate_status").asString

                                }
                            }
                            if (data.has("customer_id")) {
                                if (!data.get("customer_id").isJsonNull) {

                                    customerId1 = data.get("customer_id").asString
                                }
                            }

                            if (data.has("wavier_accepted")) {
                                if (!data.get("wavier_accepted").isJsonNull) {

                                    wavier_accepted = data.get("wavier_accepted").asString
                                }
                            }

                            val token = Preference().getUserToken(this@LoginActivity)
                            if (data.has("roles")) {
                                if (!data.get("roles").isJsonNull) {
                                    role = data.get("roles").asJsonArray

                                }
                            }

                            for (i in 0 until data.size()) {
                                val jsonObj = role.get(i).asJsonObject
//                                val modelId = jsonObj.get("model_id").asString
                                roleName = jsonObj.get("name").asString

//                                val pivot = role.get(i).asJsonObject
//                                val modelIdp = pivot.get("model_id").asString
//                                val roleId = pivot.get("role_id").asString
//                                val modelType = pivot.get("model_type").asString


                            }

                            Log.e("macro", "inside->")

                            Preference().setLoginFlag(this@LoginActivity, false)
                            Preference().setUserPassword(
                                this@LoginActivity,
                                etPassword.text.toString()
                            )
                            Preference().setUserData(
                                this@LoginActivity,
                                UserData(
                                    idCheckCustomerApi,
                                    name,
                                    email,
                                    zipcodeCheckCustomerApi,
                                    image,
                                    address,
                                    cityCheckCustomerApi,
                                    state,
                                    country,
                                    discoverLessonStatusCheckCustomerApi,
                                    progressionModelStatus,
                                    certificateStatusStatusCheckCustomerApi,
                                    customerId1,
                                    roleName,
                                    token,
                                    wavier_accepted
                                )
                            )

                        }
                    }
                    if (response.code() == 401) {

                        CustomMethods().logOutUser(this@LoginActivity)

                    }

                } catch (e: Exception) {
                }
            }
        })
    }

    override fun onBackPressed() {
        object : CountDownTimer(2000, 2000) {
            override fun onFinish() {
                pressedTwice = false
            }

            override fun onTick(millisUntilFinished: Long) {

            }
        }.start()
        if (pressedTwice) {
            finish()
        } else {
            Snackbar.make(view, "Click Again to exit", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            pressedTwice = true
        }
    }
}
