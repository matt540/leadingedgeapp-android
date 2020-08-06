package com.surfboardapp.Activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import com.stripe.android.ApiResultCallback
import com.stripe.android.PaymentIntentResult
import com.stripe.android.Stripe
import com.stripe.android.model.StripeIntent
import com.surfboardapp.CustomClasses.CustomLoader
import com.surfboardapp.CustomClasses.CustomMethods
import com.surfboardapp.Fragments.*
import com.surfboardapp.Language.AppUtils
import com.surfboardapp.Language.LanguageApplication
import com.surfboardapp.Models.CardListModel
import com.surfboardapp.Preference.Preference
import com.surfboardapp.R
import com.surfboardapp.Retrofit.Api
import com.surfboardapp.Retrofit.Connection
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.custom_bottom_navigation.*
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Response
import java.util.*
import javax.security.auth.callback.Callback


class MainActivity : FragmentActivity() {


    //Global Variables
    val calendar = "My Upcoming Sessions"
    val dashboard = "Dashboard"
    val locationFinder = "Location Finder"
    val myAccount = "My Account"
    val resetPassword = "Reset Password"
    val locationFinderInner = "Location Finder Inner"
    val sessionPaymentFragment = "Payment"
    val progressionsFragment = "Progression"
    val progressionModel = "Progression Model"
    val getCertified = "Get Certified"
    val buyABoard = "Buy A Board"
    val videos = "Video List"
    val chatFragment = "Super Admin"
    val normalSession = "Normal Session"
    val membership = "Membership"
    val newCard = "Add New Card"
    val chat = "Chat"
//    val settings = "Settings"


    private var pressedTwice = false

    lateinit var view: View
    lateinit var fragmentManager: FragmentManager
    private lateinit var fragmentTransaction: FragmentTransaction
    private lateinit var drawerLayout: DrawerLayout
    lateinit var menu: ImageView
    private lateinit var tvUsername: TextView
    private lateinit var imageView: ImageView
    private lateinit var tvScreentitle: TextView
    private lateinit var appbar: CardView
    lateinit var back: ImageView
    private lateinit var dialogProgression: View
    private lateinit var dialogLanguage: View
    private lateinit var ok: TextView
    private lateinit var language: String
    private var canceled_session: String = ""
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Preference().getSelectedLanguage(this)
        AppUtils().setLocale(Locale(Preference().getSelectedLanguage(this)))
        AppUtils().setConfigChange(this)
        setContentView(R.layout.activity_main)

        firebaseAnalytics = Firebase.analytics

        var application = LanguageApplication()

        initFragmentManager()
        init()
        clicks()
        navigation()
        bottomNavigation()
        setProfilePhoto()

        FirebaseApp.initializeApp(this)
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                // Get new Instance ID token
                val token = task.result?.token
                Log.e("macro", "token->$token")

                //notification api
                getNotificationUserToken(token)
            })
    }

    private fun getNotificationUserToken(token: String?) {
        val dialog: Dialog = CustomLoader().loader(this)
        val api: Api = Connection().getCon(this as android.content.Context)
        api.getNotificationUserToken(
            "Bearer " + Preference().getUserData(this)?.token,
            token!!,
            "android"
        ).enqueue(object : Callback, retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                CustomLoader().stopLoader(dialog)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                CustomLoader().stopLoader(dialog)
                Log.e("macro", "respN->" + response.body())

                val data = response.body()!!.get("data").asJsonObject
                if (data.has("id")) {
                    if (!data.get("id").isJsonNull) {

                        val id = data.get("id").asString
                    }
                }
//                if (data.has("user_id")) {
//                    if (!data.get("user_id").isJsonNull) {
//                        val userId = data.get("user_id").asString
//                    }
//                }
//                if (data.has("token")) {
//                    if (!data.get("token").isJsonNull) {
//                        val tokenApi = data.get("token").asString
//                    }
//                }
//                if (data.has("os")) {
//                    if (!data.get("os").isJsonNull) {
//                        val os = data.get("os").asString
//                    }
//                }
//                if (data.has("created_at")) {
//                    if (!data.get("created_at").isJsonNull) {
//                        val createdAt = data.get("created_at").asString
//                    }
//                }
//                if (data.has("updated_at")) {
//                    if (!data.get("updated_at").isJsonNull) {
//                        val updatedAt = data.get("updated_at").asString
//                    }
//                }
//                if (data.has("deleted_at")) {
//                    if (!data.get("deleted_at").isJsonNull) {
//                        val deletedAt = data.get("deleted_at").asString
//                    }
//                }
            }
        })
    }

    @SuppressLint("InflateParams")
    fun bottomNavigation() {

        linear_dashboard.setOnClickListener {
            CustomMethods().openPage(
                this,
                MainActivity().dashboard
            )
            clearSelection()
        }
        linear_location.setOnClickListener {
            CustomMethods().openPage(
                this,
                MainActivity().locationFinder
            )
            clearSelection()
        }
        linear_myAccount.setOnClickListener {
            CustomMethods().openPage(
                this,
                MainActivity().myAccount
            )
            clearSelection()
        }
        linear_sessions.setOnClickListener {
            CustomMethods().openPage(
                this,
                MainActivity().calendar
            )
            clearSelection()
        }

        linear_progression.setOnClickListener {
            if ((Preference().getUserData(this)!!.progression_model_status == "0") && (Preference().getUserData(
                    this
                )!!.discover_lesson_status == "1")
            ) {
                val builder = Dialog(this)
                val layoutInflater = LayoutInflater.from(this)
                dialogProgression =
                    layoutInflater.inflate(R.layout.progression_popup, null)
                val tvSessionDesc =
                    dialogProgression.findViewById<TextView>(R.id.tv_description)

                ok = dialogProgression.findViewById(R.id.txt_ok)

                tvSessionDesc.setText(R.string.desc_progression_page)

                builder.setCancelable(false)
                builder.setContentView(dialogProgression)
                builder.show()


                ok.setOnClickListener {
                    builder.cancel()
                    builder.dismiss()
                    CustomMethods().openPage(this, MainActivity().locationFinder)
                    clearSelection()
                }

            } else if ((Preference().getUserData(this)!!.progression_model_status == "0") && (Preference().getUserData(
                    this
                )!!.discover_lesson_status == "2")
            ) {

                val builder = Dialog(this)
                val layoutInflater = LayoutInflater.from(this)
                dialogProgression =
                    layoutInflater.inflate(R.layout.progression_popup, null)
                val tvSessionDesc =
                    dialogProgression.findViewById<TextView>(R.id.tv_description)

                ok = dialogProgression.findViewById(R.id.txt_ok)

                tvSessionDesc.setText(R.string.desc_progression_page1)

                builder.setCancelable(false)
                builder.setContentView(dialogProgression)
                builder.show()


                ok.setOnClickListener {
                    builder.cancel()
                    builder.dismiss()
                    CustomMethods().openPage(this, MainActivity().locationFinder)
                    clearSelection()
                }


            } else {
                CustomMethods().openPage(
                    this,
                    MainActivity().progressionModel
                )
                clearSelection()
            }

        }

    }

    @SuppressLint("InflateParams")
    private fun navigation() {
        drawerLayout.findViewById<LinearLayout>(R.id.nav_home_ll)
            .setOnClickListener {
                navigationMenu(1)
                bottom_navigation.visibility = View.VISIBLE
                back.visibility = View.GONE
            }
        drawerLayout.findViewById<LinearLayout>(R.id.nav_location_ll)
            .setOnClickListener {
                navigationMenu(2)
                bottom_navigation.visibility = View.VISIBLE
                back.visibility = View.GONE
            }
        drawerLayout.findViewById<LinearLayout>(R.id.nav_cal_ll)
            .setOnClickListener {
                navigationMenu(3)
                bottom_navigation.visibility = View.VISIBLE
                back.visibility = View.GONE
            }

        drawerLayout.findViewById<LinearLayout>(R.id.nav_certified_ll)
            .setOnClickListener {
                navigationMenu(4)
                bottom_navigation.visibility = View.GONE
                menu.visibility = View.GONE
                back.visibility = View.VISIBLE
            }

        drawerLayout.findViewById<LinearLayout>(R.id.nav_buy_ll)
            .setOnClickListener {
                navigationMenu(5)
                bottom_navigation.visibility = View.GONE
                menu.visibility = View.GONE
                back.visibility = View.VISIBLE
            }

        drawerLayout.findViewById<LinearLayout>(R.id.nav_videos_ll)
            .setOnClickListener {
                navigationMenu(6)
                bottom_navigation.visibility = View.GONE
                menu.visibility = View.GONE
                back.visibility = View.VISIBLE
            }



        drawerLayout.findViewById<LinearLayout>(R.id.nav_share_ll)
            .setOnClickListener {
                navigationMenu(7)
                bottom_navigation.visibility = View.VISIBLE
                back.visibility = View.GONE
            }
        drawerLayout.findViewById<LinearLayout>(R.id.nav_setting_ll)
            .setOnClickListener {
                navigationMenu(8)
                bottom_navigation.visibility = View.VISIBLE
                back.visibility = View.GONE
            }
        drawerLayout.findViewById<Button>(R.id.loginOutBtn)
            .setOnClickListener {
                navigationMenu(9)
                bottom_navigation.visibility = View.VISIBLE
                back.visibility = View.GONE
            }

        drawerLayout.findViewById<LinearLayout>(R.id.nav_membership).setOnClickListener {
            navigationMenu(10)
            bottom_navigation.visibility = View.VISIBLE
            menu.visibility = View.GONE
            back.visibility = View.VISIBLE
            drawerLayout.closeDrawers()

        }
        drawerLayout.findViewById<LinearLayout>(R.id.nav_chat).setOnClickListener {
            navigationMenu(11)
            bottom_navigation.visibility = View.GONE
            drawerLayout.closeDrawers()

        }
        drawerLayout.findViewById<LinearLayout>(R.id.nav_privacy).setOnClickListener {
            navigationMenu(12)
            drawerLayout.closeDrawers()

        }
        drawerLayout.findViewById<LinearLayout>(R.id.nav_about).setOnClickListener {
            navigationMenu(13)
            drawerLayout.closeDrawers()

        }
        drawerLayout.findViewById<LinearLayout>(R.id.nav_change_language_ll).setOnClickListener {

            navigationMenu(10)
            drawerLayout.closeDrawers()

            changeLanguage()
//            bottom_navigation.visibility = View.VISIBLE
//            back.visibility = View.GONE


        }

    }

    @SuppressLint("InflateParams")
    private fun changeLanguage() {
        val builder = Dialog(this)
        val layoutInflater = LayoutInflater.from(this)
        dialogLanguage =
            layoutInflater.inflate(R.layout.language_popup, null)
        val btnEnglish =
            dialogLanguage.findViewById<Button>(R.id.btnEnglish)
        val btnSpanish =
            dialogLanguage.findViewById<Button>(R.id.btnSpanish)
        val btnGerman =
            dialogLanguage.findViewById<Button>(R.id.btnGerman)
        val btnFrench =
            dialogLanguage.findViewById<Button>(R.id.btnFrench)
        val btnItalian =
            dialogLanguage.findViewById<Button>(R.id.btnItalian)


        btnEnglish.setOnClickListener {
            language = "en"
            AppUtils().setLocale(Locale(language))
            AppUtils().setConfigChange(this)
            Preference().setSelectedLanguage(this, language)
            Log.e("macro", "lang->" + Preference().getSelectedLanguage(this))
            builder.cancel()
            builder.dismiss()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            clearSelection()
        }
        btnSpanish.setOnClickListener {
            language = "es"
            AppUtils().setLocale(Locale(language))
            AppUtils().setConfigChange(this)
            Preference().setSelectedLanguage(this, language)
            Log.e("macro", "lang->" + Preference().getSelectedLanguage(this))
            builder.cancel()
            builder.dismiss()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            clearSelection()
        }
        btnGerman.setOnClickListener {
            language = "de"
            AppUtils().setLocale(Locale(language))
            AppUtils().setConfigChange(this)
            Preference().setSelectedLanguage(this, language)
            Log.e("macro", "lang->" + Preference().getSelectedLanguage(this))
            builder.cancel()
            builder.dismiss()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            clearSelection()
        }
        btnFrench.setOnClickListener {
            language = "fr"
            AppUtils().setLocale(Locale(language))
            AppUtils().setConfigChange(this)
            Preference().setSelectedLanguage(this, language)
            builder.cancel()
            builder.dismiss()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            clearSelection()
        }
        btnItalian.setOnClickListener {
            language = "it"
            AppUtils().setLocale(Locale(language))
            AppUtils().setConfigChange(this)
            Preference().setSelectedLanguage(this, language)
            builder.cancel()
            builder.dismiss()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            clearSelection()
        }
        builder.setCancelable(false)
        builder.setContentView(dialogLanguage)
        builder.show()
    }


    private fun setProfilePhoto() {
        try {
            tvUsername.text = Preference().getUserData(this@MainActivity)!!.name
            if (Preference().getUserData(this)!!.image == "") {
                Picasso.get().load(R.drawable.account).placeholder(
                    R.drawable.account
                )
                    .into(imageView)
            } else {
                Picasso.get().load(Preference().getUserData(this)!!.image)
                    .placeholder(R.drawable.account).into(imageView)
            }
        } catch (e: Exception) {
            tvUsername.text = ""
            Picasso.get().load(R.drawable.account).placeholder(
                R.drawable.account
            )
                .into(imageView)
        }
    }

    private fun initFragmentManager() {
        fragmentManager = supportFragmentManager
        fragmentTransaction = fragmentManager.beginTransaction()
    }

    private fun clicks() {
        menu.setOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }
    }

    private fun init() {
        view = findViewById(android.R.id.content)
        drawerLayout = findViewById(R.id.drawer_layout)
        menu = findViewById(R.id.menu)
        tvUsername = findViewById(R.id.tv_userName)
        imageView = findViewById(R.id.imageView)
        tvScreentitle = findViewById(R.id.tv_screenTitle)
        appbar = findViewById(R.id.appbar)
        back = findViewById(R.id.back)

        clearSelection()
        val home = drawerLayout.findViewById<LinearLayout>(R.id.nav_home_ll)
        val homeImg = drawerLayout.findViewById<ImageView>(R.id.nav_home_img)
        val hometxt = drawerLayout.findViewById<TextView>(R.id.nav_home_txt)
        home?.setBackgroundColor(
            ContextCompat.getColor(
                baseContext,
                R.color.colorPrimaryDark
            )
        )
        homeImg?.setColorFilter(
            ContextCompat.getColor(
                baseContext,
                R.color.white
            )
        )
        hometxt?.setTextColor(
            ContextCompat.getColor(
                baseContext,
                R.color.white
            )
        )
        fragmentTransaction.add(
            R.id.nav_host,
            DashboardFragment()
        ).commit()
        bottom_navigation.visibility = View.VISIBLE
        back.visibility = View.GONE
        menu.visibility = View.VISIBLE


        canceled_session = intent.getStringExtra("canceled_session")

        if (canceled_session.equals("canceled_session")) {
            initFragmentManager()
            clearSelection()
            clearbottomNavigation()
            fragmentTransaction.replace(
                R.id.nav_host,
                CalendarFragment()
            ).commit()
        }

        stripeApiCall()
    }

    private fun stripeApiCall() {
        val api: Api = Connection().getCon(this)
        api.stripeApiKey(
            "Bearer " + Preference().getUserData(this)?.token
        ).enqueue(object : Callback, retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.isSuccessful) {
                    if (response.body() != null) {
                        var data = response.body()!!.get("data").asJsonObject
                        var key = data.get("key").asString

                        Preference().setStringStripeKey(this@MainActivity, key)
                    }
                }

            }
        })
    }

    private fun clearbottomNavigation() {

        val dashboardImage = findViewById<ImageView>(R.id.image_dashboard)
        val dashboardText = findViewById<TextView>(R.id.text_dashboard)
        dashboardText?.setTextColor(ContextCompat.getColor(this, R.color.white))
        dashboardImage?.setColorFilter(ContextCompat.getColor(this, R.color.white))
    }

    private fun navigationMenu(position: Int) {
        when (position) {
            1 -> {
                initFragmentManager()
                clearSelection()
                fragmentTransaction.replace(R.id.nav_host, DashboardFragment()).commit()
            }
            2 -> {
                initFragmentManager()
                clearSelection()
                fragmentTransaction.replace(
                    R.id.nav_host,
                    LocationFinderFragment()
                ).commit()
            }
            3 -> {
                initFragmentManager()
                clearSelection()
                fragmentTransaction.replace(
                    R.id.nav_host,
                    CalendarFragment()
                ).commit()
            }
            4 -> {
                initFragmentManager()
                clearSelection()
                fragmentTransaction.replace(R.id.nav_host, GetCertifiedFragment()).commit()
            }
            5 -> {
                initFragmentManager()
                clearSelection()
                fragmentTransaction.replace(R.id.nav_host, BuyABoardFragment()).commit()
            }
            6 -> {
                initFragmentManager()
                clearSelection()
                fragmentTransaction.replace(R.id.nav_host, VideosFragment()).commit()

            }
            7 -> {
                var appPackageName: String = baseContext.packageName
                var sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                sendIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Check this LinkAffliaite App at: https://play.google.com/store/apps/details?id=" + appPackageName
                )
                sendIntent.type = "text/plain"
                startActivity(sendIntent)
            }
            8 -> {
                initFragmentManager()
                clearSelection()
                fragmentTransaction.replace(R.id.nav_host, MyAccountFragment()).commit()
            }
            9 -> {
                CustomMethods().logOutUser(this)
            }
            10 -> {

                if ((Preference().getUserData(this)!!.certificate_status.equals("2")) &&
                    (Preference().getUserData(this)!!.discover_lesson_status.equals("2")) &&
                    (Preference().getUserData(this)!!.progression_model_status.equals("5"))
                ) {
                    initFragmentManager()
                    clearSelection()
                    fragmentTransaction.replace(R.id.nav_host, MembershipFragment()).commit()
                } else {
                    Toast.makeText(this, "You are not eligible for Membership", Toast.LENGTH_SHORT)
                        .show()
                    Toast.makeText(
                        this,
                        "To become a member please schedule a discovery session or continue through the Progression Program",
                        Toast.LENGTH_SHORT
                    ).show()
                    clearSelection()
                    startActivity(
                        Intent(
                            this,
                            MainActivity::class.java
                        ).putExtra("canceled_session", "")
                    )
                    finish()
                }

//                fragmentTransaction.replace(R.id.nav_host, SettingsFragment()).commit()
            }
            11 -> {

                clearSelection()
                startActivity(
                    Intent(
                        this,
                        FirstActivity::class.java
                    ).putExtra("notification_room_id", "")
                )


            }
            12 -> {
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data =
                    Uri.parse("https://linkaffiliateapp.com/images/Link%20Program%20Terms%20of%20Use%20and%20Privacy%20Policy.pdf")
                startActivity(openURL)


            }
            13 -> {
                clearSelection()
                startActivity(
                    Intent(
                        this,
                        NavigationAboutActivity::class.java
                    )
                )


            }

        }
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    fun clearSelection() {

        drawerLayout.findViewById<LinearLayout>(R.id.nav_setting_ll)
            .setBackgroundColor(
                ContextCompat.getColor(
                    baseContext,
                    R.color.white
                )
            )
        drawerLayout.findViewById<ImageView>(R.id.nav_setting_img)
            .setColorFilter(
                ContextCompat.getColor(
                    baseContext,
                    R.color.colorMenuItem
                )
            )
        drawerLayout.findViewById<TextView>(R.id.nav_setting_txt)
            .setTextColor(
                ContextCompat.getColor(
                    baseContext,
                    R.color.colorMenuItem
                )
            )
        drawerLayout.findViewById<LinearLayout>(R.id.nav_home_ll)
            .setBackgroundColor(
                ContextCompat.getColor(
                    baseContext,
                    R.color.white
                )
            )
        drawerLayout.findViewById<ImageView>(R.id.nav_home_img)
            .setColorFilter(
                ContextCompat.getColor(
                    baseContext,
                    R.color.colorMenuItem
                )
            )
        drawerLayout.findViewById<TextView>(R.id.nav_home_txt)
            .setTextColor(
                ContextCompat.getColor(
                    baseContext,
                    R.color.colorMenuItem
                )
            )

        drawerLayout.findViewById<LinearLayout>(R.id.nav_cal_ll)
            .setBackgroundColor(
                ContextCompat.getColor(
                    baseContext,
                    R.color.white
                )
            )
        drawerLayout.findViewById<ImageView>(R.id.nav_cal_img)
            .setColorFilter(
                ContextCompat.getColor(
                    baseContext,
                    R.color.colorMenuItem
                )
            )
        drawerLayout.findViewById<TextView>(R.id.nav_cal_txt)
            .setTextColor(
                ContextCompat.getColor(
                    baseContext,
                    R.color.colorMenuItem
                )
            )


        drawerLayout.findViewById<LinearLayout>(R.id.nav_certified_ll)
            .setBackgroundColor(
                ContextCompat.getColor(
                    baseContext,
                    R.color.white
                )
            )
        drawerLayout.findViewById<ImageView>(R.id.nav_certified_img)
            .setColorFilter(
                ContextCompat.getColor(
                    baseContext,
                    R.color.colorMenuItem
                )
            )
        drawerLayout.findViewById<TextView>(R.id.nav_certified_txt)
            .setTextColor(
                ContextCompat.getColor(
                    baseContext,
                    R.color.colorMenuItem
                )
            )


        drawerLayout.findViewById<LinearLayout>(R.id.nav_buy_ll)
            .setBackgroundColor(
                ContextCompat.getColor(
                    baseContext,
                    R.color.white
                )
            )
        drawerLayout.findViewById<ImageView>(R.id.nav_buy_img)
            .setColorFilter(
                ContextCompat.getColor(
                    baseContext,
                    R.color.colorMenuItem
                )
            )
        drawerLayout.findViewById<TextView>(R.id.nav_buy_txt)
            .setTextColor(
                ContextCompat.getColor(
                    baseContext,
                    R.color.colorMenuItem
                )
            )




        drawerLayout.findViewById<LinearLayout>(R.id.nav_videos_ll)
            .setBackgroundColor(
                ContextCompat.getColor(
                    baseContext,
                    R.color.white
                )
            )
        drawerLayout.findViewById<ImageView>(R.id.nav_videos_img)
            .setColorFilter(
                ContextCompat.getColor(
                    baseContext,
                    R.color.colorMenuItem
                )
            )
        drawerLayout.findViewById<TextView>(R.id.nav_videos_txt)
            .setTextColor(
                ContextCompat.getColor(
                    baseContext,
                    R.color.colorMenuItem
                )
            )

        drawerLayout.findViewById<LinearLayout>(R.id.nav_location_ll)
            .setBackgroundColor(
                ContextCompat.getColor(
                    baseContext,
                    R.color.white
                )
            )
        drawerLayout.findViewById<ImageView>(R.id.nav_location_img)
            .setColorFilter(
                ContextCompat.getColor(
                    baseContext,
                    R.color.colorMenuItem
                )
            )
        drawerLayout.findViewById<TextView>(R.id.nav_location_txt)
            .setTextColor(
                ContextCompat.getColor(
                    baseContext,
                    R.color.colorMenuItem
                )
            )

        drawerLayout.findViewById<LinearLayout>(R.id.nav_share_ll)
            .setBackgroundColor(
                ContextCompat.getColor(
                    baseContext,
                    R.color.white
                )
            )
        drawerLayout.findViewById<ImageView>(R.id.nav_share_img)
            .setColorFilter(
                ContextCompat.getColor(
                    baseContext,
                    R.color.colorMenuItem
                )
            )
        drawerLayout.findViewById<TextView>(R.id.nav_share_txt)
            .setTextColor(
                ContextCompat.getColor(
                    baseContext,
                    R.color.colorMenuItem
                )
            )

        val dashboardImage = findViewById<ImageView>(R.id.image_dashboard)
        val dashboardText = findViewById<TextView>(R.id.text_dashboard)
        dashboardText?.setTextColor(ContextCompat.getColor(this, R.color.white))
        dashboardImage?.setColorFilter(ContextCompat.getColor(this, R.color.white))

        val sessionsImage = findViewById<ImageView>(R.id.image_session)
        val sessionsText = findViewById<TextView>(R.id.text_session)
        sessionsText?.setTextColor(ContextCompat.getColor(this, R.color.white))
        sessionsImage?.setColorFilter(ContextCompat.getColor(this, R.color.white))

        val locationImage = findViewById<ImageView>(R.id.image_location)
        val locationText = findViewById<TextView>(R.id.text_location)
        locationText?.setTextColor(ContextCompat.getColor(this, R.color.white))
        locationImage?.setColorFilter(ContextCompat.getColor(this, R.color.white))

        val progressionImage = findViewById<ImageView>(R.id.image_progression)
        val progressionText = findViewById<TextView>(R.id.text_progression)
        progressionText?.setTextColor(ContextCompat.getColor(this, R.color.white))
        progressionImage?.setColorFilter(ContextCompat.getColor(this, R.color.white))

        val myAccountImage = findViewById<ImageView>(R.id.image_myAccount)
        val myAccountText = findViewById<TextView>(R.id.text_myAccount)
        myAccountText?.setTextColor(ContextCompat.getColor(this, R.color.white))
        myAccountImage?.setColorFilter(ContextCompat.getColor(this, R.color.white))
    }

    override fun onBackPressed() {

        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            when (fragmentManager.findFragmentById(R.id.nav_host)) {
                is LocationFinderFragment -> {
                    CustomMethods().openPage(this, MainActivity().dashboard)
                    clearSelection()
                }
                is CalendarFragment -> {
                    CustomMethods().openPage(this, MainActivity().dashboard)
                    clearSelection()
                }
                is MyAccountFragment -> {
                    CustomMethods().openPage(this, MainActivity().dashboard)
                    clearSelection()
                }
                is SessionPaymentFragment -> {
                    CustomMethods().openPage(this, MainActivity().locationFinder)
                    clearSelection()
                }
                is LocationFinderInnerFragment -> {
                    CustomMethods().openPage(this, MainActivity().locationFinder)
                    clearSelection()
                }
                is ProgressionsFragment -> {
                    CustomMethods().openPage(this, MainActivity().dashboard)
                    clearSelection()
                }
                is BuyABoardFragment -> {
                    CustomMethods().openPage(this, MainActivity().dashboard)
                    clearSelection()
                }
                is GetCertifiedFragment -> {
                    CustomMethods().openPage(this, MainActivity().dashboard)
                    clearSelection()
                }
                is VideosFragment -> {
                    CustomMethods().openPage(this, MainActivity().dashboard)
                    clearSelection()
                    back.visibility = View.GONE
                    menu.visibility = View.VISIBLE
                    bottom_navigation.visibility = View.VISIBLE
                }
                is MembershipFragment -> {
                    CustomMethods().openPage(this, MainActivity().dashboard)
                    clearSelection()
                    back.visibility = View.GONE
                    menu.visibility = View.VISIBLE
                }
                is NormalSessionFragment -> {
                    CustomMethods().openPage(this, MainActivity().dashboard)
                    clearSelection()
                }
                is ChatFragment -> {
                    CustomMethods().openPage(this, MainActivity().calendar)
                    clearSelection()
                }
                is AddnewcardFragment -> {
                    CustomMethods().openPage(this, MainActivity().membership)
                    clearSelection()
                    back.visibility = View.VISIBLE
                    menu.visibility = View.GONE
                }
                else -> {
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

        } else {
            drawerLayout.closeDrawer(GravityCompat.START)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.e("activity", "acyivity")
        super.onActivityResult(requestCode, resultCode, data)


        val stripe =
            Stripe(
                this,
                "" + resources.getString(R.string.pk_token_stripe)
            )

        stripe.onPaymentResult(requestCode, data, object : ApiResultCallback<PaymentIntentResult> {
            override fun onSuccess(result: PaymentIntentResult) {
                val paymentIntent = result.intent
                Log.e("payment", "" + result)
                Log.e("paymentIntent", "" + result.intent)
                when (paymentIntent.status) {
                    StripeIntent.Status.Succeeded -> {
                        Log.e("pass", "pass")
                        val gson = GsonBuilder().setPrettyPrinting().create()
                        Log.e("gson", "" + gson)

                        EventBus.getDefault().post(CardListModel("" + paymentIntent.id, "", ""))


                    }
                    StripeIntent.Status.RequiresPaymentMethod -> {
                        Log.e("failed", "failed")
                        // Payment failed – allow retrying using a different payment method

                    }
                    StripeIntent.Status.RequiresConfirmation -> {
                        // After handling a required action on the client, the status of the PaymentIntent is
                        // requires_confirmation. You must send the PaymentIntent ID to your backend
                        // and confirm it to finalize the payment. This step enables your integration to
                        // synchronously fulfill the order on your backend and return the fulfillment result
                        // to your client.

                        Log.e("require", "asdads")
                        Log.e("paymentIntentId", "" + paymentIntent.id)
//                        bookingCall(paymentIntent.id.toString())


                    }
                    else -> {
                        Log.e("here", "here")
                    }
                }
            }

            override fun onError(e: Exception) {
                // Payment request failed – allow retrying using the same payment method
                Log.e("error", "error")

            }
        })


    }
}
