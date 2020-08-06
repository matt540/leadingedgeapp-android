package com.surfboardapp.Activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import com.surfboardapp.CustomClasses.CustomLoader
import com.surfboardapp.CustomClasses.CustomMethods
import com.surfboardapp.Language.AppUtils
import com.surfboardapp.Preference.Preference
import com.surfboardapp.R
import com.surfboardapp.Retrofit.Api
import com.surfboardapp.Retrofit.Connection
import retrofit2.Call
import retrofit2.Response
import java.util.*
import javax.security.auth.callback.Callback

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var back: ImageView
    lateinit var email: EditText
    private lateinit var submit: Button
    var pressedTwice = false
    lateinit var view: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Preference().getSelectedLanguage(this)
        AppUtils().setLocale(Locale(Preference().getSelectedLanguage(this)))
        AppUtils().setConfigChange(this)
        setContentView(R.layout.activity_forgot_password)

        init()
        click()
    }

    private fun click() {
        back.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        submit.setOnClickListener {
            if (email.text.toString().equals("")) {
                Toast.makeText(this, "Please Fill the field above", Toast.LENGTH_LONG).show()
            } else {
                val dialog: Dialog = CustomLoader().loader(this)
                val api: Api = Connection().getCon(this)
                api.forgot_password(
                    "Bearer " + Preference().getUserData(this)?.token,
                    "" + email.text.toString()

                ).enqueue(object : Callback, retrofit2.Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        dialog.dismiss()
                    }

                    override fun onResponse(
                        call: Call<JsonObject>,
                        response: Response<JsonObject>
                    ) {
                        dialog.dismiss()
                        if (response.isSuccessful) {
                            if (response.body() != null) {

                                startActivity(
                                    Intent(
                                        this@ForgotPasswordActivity,
                                        LoginActivity::class.java
                                    )
                                )
                                finish()
                            }
                        }
                        if (response.code() == 401) {
                            CustomMethods().logOutUser(this@ForgotPasswordActivity)
                        }
                    }
                })
            }
        }
    }

    private fun init() {

        back = findViewById(R.id.back)
        email = findViewById(R.id.et_email)
        submit = findViewById(R.id.btn_login)
        view = findViewById(android.R.id.content)
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
