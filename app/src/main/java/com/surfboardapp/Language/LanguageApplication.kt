package com.surfboardapp.Language

import android.app.Application
import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.Tracker
import java.util.*


class LanguageApplication : Application() {
    private var sAnalytics: GoogleAnalytics? = null
    private var sTracker: Tracker? = null
    override fun onCreate() {
        setLocale()
        super.onCreate()
        sAnalytics = GoogleAnalytics.getInstance(this)

    }

    @Synchronized
    fun getDefaultTracker(): Tracker? { // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (sTracker == null) {
            sTracker = sAnalytics!!.newTracker("")

        }
        return sTracker
    }

    private fun setLocale() {

        val jaLocale = Locale("en")
        AppUtils().setLocale(jaLocale)
        AppUtils().setConfigChange(this)
    }
}