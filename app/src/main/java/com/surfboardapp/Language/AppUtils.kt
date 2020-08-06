package com.surfboardapp.Language

import android.content.Context
import android.content.res.Configuration
import java.util.*


class AppUtils {
    private var locale: Locale? = null

    fun setLocale(localeIn: Locale?) {
        locale = localeIn
        if (locale != null) {
            Locale.setDefault(locale!!)
        }
    }

    fun setConfigChange(ctx: Context): String {

        val locale = Locale.getDefault()
        Locale.setDefault(locale)
        System.out.println("GlobalLocale$locale")
        val config = Configuration()
        config.locale = locale
        ctx.resources.updateConfiguration(
            config,
            ctx.resources.displayMetrics
        )
        return locale.toString()
    }
}
