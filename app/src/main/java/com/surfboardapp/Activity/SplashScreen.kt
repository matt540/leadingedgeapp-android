package com.surfboardapp.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.surfboardapp.Language.AppUtils
import com.surfboardapp.Language.LanguageApplication
import com.surfboardapp.Preference.Preference
import com.surfboardapp.R
import java.util.*


class SplashScreen : AppCompatActivity() {

    lateinit var btnSkip: TextView
    lateinit var view: VideoView
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN)
        Preference().getSelectedLanguage(this)
        AppUtils().setLocale(Locale(Preference().getSelectedLanguage(this)))
        AppUtils().setConfigChange(this)
        setContentView(R.layout.activity_splash_screen)
        firebaseAnalytics = Firebase.analytics

        var application = LanguageApplication()
        init()
        setVideo()
        click()



        btnSkip.visibility = View.GONE
        object : CountDownTimer(1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                btnSkip.visibility = View.VISIBLE
            }
        }.start()
    }

    private fun click() {
        btnSkip.setOnClickListener {

            startActivity(Intent(this, AboutActivity::class.java))
            finish()

//            if (!Preference().getLoginFlag(this)) {
//
//                startActivity(
//                    Intent(this, MainActivity::class.java).putExtra(
//                        "canceled_session",
//                        ""
//                    )
//                )
//                finish()
//            } else {
//
//                startActivity(Intent(this, SignUpActivity::class.java))
//                finish()
//            }
        }
    }

    private fun setVideo() {
        val path = "android.resource://" + packageName + "/" + R.raw.op
        view.setVideoURI(Uri.parse(path))
        view.setOnPreparedListener { view.start() }
        view.setOnCompletionListener {

            //            if (!Preference().getLoginFlag(this)) {
//
//                startActivity(
//                    Intent(this, MainActivity::class.java).putExtra(
//                        "canceled_session",
//                        ""
//                    )
//                )
//                finish()
//            } else {
//
//                startActivity(Intent(this, SignUpActivity::class.java))
//                finish()
//            }


            startActivity(Intent(this, AboutActivity::class.java))
            finish()
        }
    }

    private fun init() {
        btnSkip = findViewById(R.id.btn_skip)
        view = findViewById(R.id.videoView)
    }
}
