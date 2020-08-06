package com.surfboardapp.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.surfboardapp.Preference.Preference
import com.surfboardapp.R

class AboutActivity : AppCompatActivity() {

    private lateinit var btn_lets_fly: Button
    private var pressedTwice = false
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        firebaseAnalytics = Firebase.analytics

        btn_lets_fly = findViewById(R.id.btn_lets_fly)

        btn_lets_fly.setOnClickListener {

            if (!Preference().getLoginFlag(this)) {

                startActivity(
                    Intent(this, MainActivity::class.java).putExtra(
                        "canceled_session",
                        ""
                    )
                )
                finish()
            } else {

                startActivity(Intent(this, SignUpActivity::class.java))
                finish()
            }

        }
    }

    override fun onBackPressed() {
        if (pressedTwice) {
            super.onBackPressed()
            return
        }
        this.pressedTwice = true
        Snackbar.make(
            findViewById(android.R.id.content),
            "Please click BACK again to exit",
            Snackbar.LENGTH_LONG
        ).show()
        Handler().postDelayed({ pressedTwice = false }, 2000)
    }
}
