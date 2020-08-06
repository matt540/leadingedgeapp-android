package com.surfboardapp.Retrofit

import android.content.Context
import com.google.gson.GsonBuilder
import com.surfboardapp.R
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Connection {
    fun getCon(context: Context): Api {


        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(30000, TimeUnit.SECONDS)
            .readTimeout(3000, TimeUnit.SECONDS)
            .writeTimeout(3000, TimeUnit.SECONDS)
            .build()


        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl(context.getString(R.string.url))
            .addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient).build()
        return retrofit.create(Api::class.java)


    }

    fun getConApi(context: Context): Api {


        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl("https://api.stripe.com/v1/customers/")
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
        return retrofit.create(Api::class.java)
    }

    fun getConApiPayment(context: Context): Api {


        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl("https://api.stripe.com/v1/payment_intents/")
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
        return retrofit.create(Api::class.java)
    }

    fun getNormalSessionApi(context: Context): Api {


        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl("https://api.stripe.com/v1/")
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
        return retrofit.create(Api::class.java)
    }

}