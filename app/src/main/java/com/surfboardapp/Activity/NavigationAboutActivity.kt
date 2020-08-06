package com.surfboardapp.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.surfboardapp.R

class NavigationAboutActivity : AppCompatActivity() {

    private lateinit var img_back_nav: ImageView
    private lateinit var btn_lets_fly_nav: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_about)


        img_back_nav = findViewById(R.id.img_back_nav)
        btn_lets_fly_nav = findViewById(R.id.btn_lets_fly_nav)


        click()


    }

    private fun click() {

        img_back_nav.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).putExtra("canceled_session", ""))
            finish()
        }
        btn_lets_fly_nav.isEnabled = false
        btn_lets_fly_nav.setOnClickListener {
            startActivity(
                Intent(this, MainActivity::class.java).putExtra(
                    "canceled_session",
                    ""
                )
            )
            finish()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java).putExtra("canceled_session", ""))
        finish()
    }
}
