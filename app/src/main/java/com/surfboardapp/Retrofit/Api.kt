package com.surfboardapp.Retrofit

import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface Api {

    //login
    @FormUrlEncoded
    @POST("login")
    fun getLogin(
        @Field("email") email: String,
        @Field("password") password: String

    ): Call<JsonObject>

    //signup
    @Multipart
    @POST("register")
    fun getRegister(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part image: MultipartBody.Part,
        @Part(
            "zipcode"
        ) zipcode: RequestBody
    ): Call<JsonObject>

    //get user data
    @GET("user/current")
    fun getUserData(
        @Header("Authorization") auth: String
    ): Call<JsonObject>

    //reset password
    @FormUrlEncoded
    @POST("user/reset-password")
    fun getResetPassword(
        @Header("Authorization") auth: String,
        @Field("old-password") old_password: String,
        @Field("password") password: String,
        @Field("password_confirmation") password_confirmation: String
    ): Call<JsonObject>

    //update user
    @FormUrlEncoded
    @POST("user/update")
    fun updateUser(
        @Header("Authorization") auth: String,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("zipcode") zipcode: String,
        @Field("address") address: String,
        @Field("city") city: String,
        @Field("state") state: String,
        @Field("country") country: String
    ): Call<JsonObject>


    //upload Image
    @Multipart
    @POST("user/upload-image")
    fun uploadImage(
        @Header("Authorization") auth: String,
        @Part image: MultipartBody.Part
    ): Call<JsonObject>

    //search location
    @FormUrlEncoded
    @POST("location/search-Location")
    fun searchLocation(
        @Header("Authorization") auth: String,
        @Field("latitude") latitude: Double,
        @Field("longitude") longitude: Double,
        @Field("range") range: String
    ): Call<JsonObject>

    //choose path
    @FormUrlEncoded
    @POST("user/choose-path")
    fun choosePath(
        @Header("Authorization") auth: String,
        @Field("path") path: String
    ): Call<JsonObject>

    //location favourite
    @FormUrlEncoded
    @POST("location/favorite-Location")
    fun favouriteLocation(
        @Header("Authorization") auth: String,
        @Field("location_id") location_id: String,
        @Field("favorite") favorite: String
    ): Call<JsonObject>

    //location holidays
    @FormUrlEncoded
    @POST("location/active-days/get")
    fun locationHolidays(
        @Header("Authorization") auth: String,
        @Field("location_id") location_id: String,
        @Field("month") month: String,
        @Field("year") year: String
    ): Call<JsonObject>

    //location session data
    @FormUrlEncoded
    @POST("location/session-data")
    fun locationSessiondata(
        @Header("Authorization") auth: String,
        @Field("location") location_id: String
    ): Call<JsonObject>

    //stripe payment
    @FormUrlEncoded
    @POST("location/booking_histories/add")
    fun stripePayment(
        @Header("Authorization") auth: String,
        @Field("type") type: String,
        @Field("date") date: String,
        @Field("location_id") location_id: String,
        @Field("token") token: String,
        @Field("cost") cost: String,
        @Field("payment_intent") payment_intent: String,
        @Field("instructor") instructor: String,
        @Field("board") board: String,
        @Field("start_time") start_time: String,
        @Field("end_time") end_time: String,
        @Field("session_id") session_id: String
    ): Call<JsonObject>


    //stripe payment
    @FormUrlEncoded
    @POST("location/booking_histories/add")
    fun stripePaymentEnrollment(
        @Header("Authorization") auth: String,
        @Field("type") type: String,
        @Field("date") date: String,
        @Field("location_id") location_id: String,
        @Field("token") token: String,
        @Field("cost") cost: String,
        @Field("payment_intent") payment_intent: String
    ): Call<JsonObject>


    //stripe payment
    @FormUrlEncoded
    @POST("location/booking_histories/add")
    fun stripePaymentCruiser(
        @Header("Authorization") auth: String,
        @Field("type") type: String,
        @Field("date") date: String,
        @Field("location_id") location_id: String,
        @Field("token") token: String,
        @Field("cost") cost: String,
        @Field("payment_intent") payment_intent: String,
        @Field("instructor") instructor: String,
        @Field("board") board: String,
        @Field("start_time") start_time: String,
        @Field("end_time") end_time: String
    ): Call<JsonObject>


    //check booking status
    @GET("user/check-booking")
    fun checkBookingSession(
        @Header("Authorization") auth: String
    ): Call<JsonObject>

    //progression status
    @GET("user/progression")
    fun progressionStatus(
        @Header("Authorization") auth: String
    ): Call<JsonObject>

    //upcoming sessions
    @GET("sessions/upcoming-sessions")
    fun upcomingSessions(
        @Header("Authorization") auth: String
    ): Call<JsonObject>


//    //Get Time Slot
//    @FormUrlEncoded
//    @POST("time-slot/get")
//    fun getTimeSlot(
//        @Header("Authorization") auth: String,
//        @Field("location_id") location_id: String
//    ): Call<JsonObject>

    //New Get Time Slot
    @FormUrlEncoded
    @POST("location/active-hours")
    fun getNewTimeSlot(
        @Header("Authorization") auth: String,
        @Field("location") location_id: String,
        @Field("day") day: String
    ): Call<JsonObject>


    //create stripe customer
    @GET("user/create-stripe-customer")
    fun checkCustomerId(
        @Header("Authorization") auth: String
    ): Call<JsonObject>

    //createCard
    @FormUrlEncoded
    @POST("https://api.stripe.com/v1/customers/{customer_id}/sources")
    fun createCard(
        @Header("Authorization") auth: String,
        @Path("customer_id") customer_Id: String,
        @Field("source") source: String

    ): Call<JsonObject>

    //listCard
    @GET("{customer_id}/sources?object=card")
    fun listCard(
        @Path("customer_id") customer_Id: String,
        @Header("Authorization") auth: String
    ): Call<JsonObject>

    //payment Intent
    @FormUrlEncoded
    @POST("user/create-payment-intent")
    fun paymentIntent(
        @Header("Authorization") auth: String,
        @Field("type") type: String,
        @Field("location_id") location_id: String,
        @Field("card") card: String,
        @Field("session_id") session_id: String,
        @Field("start_time") start_time: String,
        @Field("end_time") end_time: String
    ): Call<JsonObject>

    //retrievePayment
    @GET("https://api.stripe.com/v1/payment_intents/{id}")
    fun retrievePayment(
        @Header("Authorization") auth: String,
        @Path("id") id: String
    ): Call<JsonObject>

    //confirm payment intent
    @FormUrlEncoded
    @POST("{id}/confirm")
    fun confirmPaymentIntent(
        @Path("id") id: String,
        @Header("Authorization") auth: String,
        @Field("payment_method") payment_method: String
    ): Call<JsonObject>

    //customer wavier
    @FormUrlEncoded
    @POST("location/customer-wavier/get")
    fun customerWaiver(
        @Header("Authorization") auth: String, @Field("location_id") location_id: String
    ): Call<JsonObject>

    //customer wavier accept
    @FormUrlEncoded
    @POST("location/customer-wavier/accept")
    fun customerWaiverAccept(
        @Header("Authorization") auth: String,
        @Field("location") location: String,
        @Field("firstname") firstname: String,
        @Field("minor_firstname") minor_firstname: String,
        @Field("lastname") lastname: String,
        @Field(
            "minor_lastname"
        ) minor_lastname: String
    ): Call<JsonObject>

    // waiver accepted
    @FormUrlEncoded
    @POST("user/update-user-info")
    fun waiverAccepted(
        @Header("Authorization") auth: String,
        @Field("wavier_accepted") wavier_accepted: String,
        @Field(
            "gender"
        ) gender: String,
        @Field("age") age: String,
        @Field("weight") weight: String,
        @Field("previous_experience") previous_experience: String,
        @Field(
            "experience_description"
        ) experience_description: String
    ): Call<JsonObject>

    //create-stripe-customer
    @GET("user/create-stripe-customer")
    fun createStripeCustomer(
        @Header("Authorization") auth: String
    ): Call<JsonObject>

    // progression-level
    @FormUrlEncoded
    @POST("user/progression-level")
    fun progressionLevel(
        @Header("Authorization") auth: String, @Field("location_id") location_id: String
    ): Call<JsonObject>


    //get chat message

    @FormUrlEncoded
    @POST("messages/get-messages")
    fun getMessage(
        @Header("Authorization") auth: String, @Field("session_id") session_id: String
    ): Call<JsonObject>


    //send chat message

    @FormUrlEncoded
    @POST("messages/send-messages")
    fun sendMessage(
        @Header("Authorization") auth: String,
        @Field("message") message: String,
        @Field("session_id") session_id: String
    ): Call<JsonObject>


    //previous session
    @GET("sessions/previous-sessions")
    fun previousSessions(
        @Header("Authorization") auth: String
    ): Call<JsonObject>


    //check booking
    @GET("user/check-booking")
    fun checkBooking(
        @Header("Authorization") auth: String
    ): Call<JsonObject>

    //check booking
    @GET("subscription/check")
    fun checkSubscription(
        @Header("Authorization") auth: String
    ): Call<JsonObject>

    //check booking
    @GET("membership/fetch")
    fun membership_fetch(
        @Header("Authorization") auth: String
    ): Call<JsonObject>


    //session chat rating api

    @FormUrlEncoded
    @POST("sessions/rating")
    fun getRating(
        @Header("Authorization") auth: String,
        @Field("rating") rating: Float,
        @Field("session_id") session_id: String
    ): Call<JsonObject>

    //paymnet_methods
    @FormUrlEncoded
    @POST("payment_methods")
    fun getPaymentMethods(
        @Header("Authorization") auth: String,
        @Field("type") type: String,
        @Field("card[number]") card_number: String,
        @Field(
            "card[exp_month]"
        ) card_exp_month: String,
        @Field("card[exp_year]") card_exp_year: String,
        @Field("card[cvc]") card_cvc: String
    ): Call<JsonObject>


    //notification
    @FormUrlEncoded
    @POST("notification/user-token")
    fun getNotificationUserToken(
        @Header("Authorization") auth: String,
        @Field("token") token: String,
        @Field("os") os: String
    ): Call<JsonObject>


    //get-time-data
    @FormUrlEncoded
    @POST("location/get-time-data")
    fun getTimeDatas(
        @Header("Authorization") auth: String,
        @Field("location") location: String,
        @Field("day") day: String,
        @Field("date") date: String,
        @Field("start_time") start_time: String,
        @Field("end_time") end_time: String

    ): Call<JsonObject>

    //subscription_create
    @FormUrlEncoded
    @POST("subscription/create")
    fun subscriptionCreate(
        @Header("Authorization") auth: String, @Field("pm") pm: String
    ): Call<JsonObject>

    //booking_status
    @FormUrlEncoded
    @POST("location/booking-status")
    fun bookingStatus(
        @Header("Authorization") auth: String, @Field("location") location: String
    ): Call<JsonObject>


    //booking_status
    @GET("chat/fetch-rooms")
    fun fetch_room(
        @Header("Authorization") auth: String
    ): Call<JsonObject>

    //booking_status
    @FormUrlEncoded
    @POST("chat/search-contact")
    fun search_contact(
        @Header("Authorization") auth: String, @Field("search") search: String
    ): Call<JsonObject>

    //booking_status
    @FormUrlEncoded
    @POST("chat/create-room")
    fun create_room(
        @Header("Authorization") auth: String, @Field("user") user: String
    ): Call<JsonObject>

    //booking_status
    @FormUrlEncoded
    @POST("chat/fetch-messages")
    fun fetch_message(
        @Header("Authorization") auth: String,
        @Field("room") room: String,
        @Field("last_id") last_id: String
    ): Call<JsonObject>

    //booking_status
    @FormUrlEncoded
    @POST("chat/send-message")
    fun send_message(
        @Header("Authorization") auth: String,
        @Field("message") message: String,
        @Field("room") room: String
    ): Call<JsonObject>

    //booking_status
    @FormUrlEncoded
    @POST("membership/make-default")
    fun membership_default(
        @Header("Authorization") auth: String, @Field("card") card: String
    ): Call<JsonObject>

    //retrievePayment
    @GET("https://api.stripe.com/v1/payment_methods")
    fun retrievePaymentMethod(
        @Header("Authorization") auth: String,
        @Query("customer") customer: String,
        @Query("type") type: String
    ): Call<JsonObject>


    //retrievePayment
    @GET("https://api.stripe.com/v1/customers/{customer}")
    fun retrieveCustomer(
        @Header("Authorization") auth: String,
        @Path("customer") customer: String
    ): Call<JsonObject>

    //booking_status
    @GET("membership/cancel")
    fun membership_cancel(
        @Header("Authorization") auth: String
    ): Call<JsonObject>

    //booking_status
    @GET("membership/restart")
    fun membership_restart(
        @Header("Authorization") auth: String
    ): Call<JsonObject>

    //booking_status
    @GET("user/boards")
    fun buy_board(
        @Header("Authorization") auth: String
    ): Call<JsonObject>


    //customer wavier
    @FormUrlEncoded
    @POST("sessions/cancel")
    fun sessions_cancel(
        @Header("Authorization") auth: String, @Field("session") session: String
    ): Call<JsonObject>

    //customer wavier
    @FormUrlEncoded
    @POST("forgot-password")
    fun forgot_password(
        @Header("Authorization") auth: String, @Field("email") email: String
    ): Call<JsonObject>

    //customer wavier
    @FormUrlEncoded
    @POST("chat/delete-message")
    fun delete_message(
        @Header("Authorization") auth: String, @Field("id") id: String
    ): Call<JsonObject>

    //customer wavier
    @FormUrlEncoded
    @POST("subscription/confirm")
    fun subscription_confirm(
        @Header("Authorization") auth: String, @Field("subscriptionId") id: String
    ): Call<JsonObject>

    //check booking
    @GET("user/retrive-user-membership")
    fun user_membership(
        @Header("Authorization") auth: String
    ): Call<JsonObject>

    //check booking
    @GET("setting/fetch-waiver-signoff")
    fun wavier_signoff(
        @Header("Authorization") auth: String
    ): Call<JsonObject>

    //get-details
    @FormUrlEncoded
    @POST("sessions/get-details")
    fun get_session_details(
        @Header("Authorization") auth: String, @Field("id") id: String
    ): Call<JsonObject>

    //booking_histories/update
    @FormUrlEncoded
    @POST("location/booking_histories/update")
    fun update_session(
        @Header("Authorization") auth: String,
        @Field("date") date: String,
        @Field("start_time") start_time: String,
        @Field("end_time") end_time: String,
        @Field("board") board: String,
        @Field("instructor") instructor: String,
        @Field("type") type: String,
        @Field("id") id: String
    ): Call<JsonObject>

    //booking_histories/update normal
    @FormUrlEncoded
    @POST("location/booking_histories/update")
    fun update_session(
        @Header("Authorization") auth: String,
        @Field("date") date: String,
        @Field("start_time") start_time: String,
        @Field("end_time") end_time: String,
        @Field("board") board: String,
        @Field("type") type: String,
        @Field("id") id: String
    ): Call<JsonObject>

    //booking_status
    @FormUrlEncoded
    @POST("request-info")
    fun request_info(
        @Header("Authorization") auth: String,
        @Field("name") name: String,
        @Field("email") email: String
    ): Call<JsonObject>


    //check booking
    @GET("setting/get")
    fun stripeApiKey(
        @Header("Authorization") auth: String
    ): Call<JsonObject>
}