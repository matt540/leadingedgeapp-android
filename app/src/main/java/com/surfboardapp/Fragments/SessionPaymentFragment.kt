package com.surfboardapp.Fragments


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.stripe.android.ApiResultCallback
import com.stripe.android.PaymentIntentResult
import com.stripe.android.Stripe
import com.stripe.android.model.StripeIntent
import com.stripe.android.model.Token
import com.stripe.android.view.CardInputWidget
import com.surfboardapp.Activity.MainActivity
import com.surfboardapp.Adapter.CardDetailAdapter
import com.surfboardapp.CustomClasses.CustomLoader
import com.surfboardapp.CustomClasses.CustomMethods
import com.surfboardapp.Models.CardListModel
import com.surfboardapp.Models.PaymentDetails
import com.surfboardapp.Preference.Preference
import com.surfboardapp.R
import com.surfboardapp.Retrofit.Api
import com.surfboardapp.Retrofit.Connection
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback
import kotlin.String as String1


class SessionPaymentFragment : Fragment() {
    private lateinit var tvScreenTitle: TextView
    private var validate: Boolean = false
    private var model: PaymentDetails? = null
    private lateinit var btnPay: Button
    private lateinit var cardInputWidget: CardInputWidget
    private lateinit var date: String1
    private lateinit var timeSlot: String1
    private lateinit var cost: String1
    private lateinit var type: String1
    private lateinit var isFromProgression: String1
    private lateinit var locationId: String1
    private lateinit var session_id: String1
    private lateinit var start_time: String1
    private lateinit var end_time: String1
    private lateinit var state_code: String1
    private lateinit var selected_instructor: String1
    private lateinit var selected_board: String1
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var cardListDetail: ArrayList<CardListModel>
    private lateinit var recyclerCardlist: RecyclerView
    lateinit var adapter: CardDetailAdapter
    var called = false
    private lateinit var etName: EditText
    private lateinit var etAddress: EditText
    private lateinit var etApt: EditText
    private lateinit var etUnitedStates: Spinner
    private lateinit var etPostalCode: EditText
    private lateinit var etCity: EditText
    private lateinit var etState: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_session_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        //create stripe customer
        createStripeCustomer()
        //customer_id
        // customer_id from Login response
        if (Preference().getUserData(context!!)!!.customer_id != "") {
            getCardList()
        }
        onClick()
    }

    private fun createStripeCustomer() {
//        val dialog: Dialog = CustomLoader().loader(context)
        val api: Api = Connection().getCon(context!!)
        api.createStripeCustomer("Bearer " + Preference().getUserData(context!!)!!.token)
            .enqueue(object : Callback, retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
//                    CustomLoader().stopLoader(dialog)
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
//                    CustomLoader().stopLoader(dialog)
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            Log.e("macro", "respB->" + response.body())

                            //response body

                            val data = response.body()!!.get("data").asJsonObject
//                            if (data.has("id")) {
//                                if (!data.get("id").isJsonNull) {
//
//                                    val id = data.get("id").asString
//                                }
//                            }
//                            if (data.has("name")) {
//                                if (!data.get("name").isJsonNull) {
//
//                                    val name = data.get("name").asString
//                                }
//                            }
//                            if (data.has("email")) {
//                                if (!data.get("email").isJsonNull) {
//
//                                    val email = data.get("email").asString
//                                }
//                            }
//                            if (data.has("zipcode")) {
//                                if (!data.get("zipcode").isJsonNull) {
//
//                                    val zipCode = data.get("zipcode").asString
//                                }
//                            }
//                            if (data.has("image")) {
//                                if (!data.get("image").isJsonNull) {
//                                    val image = data.get("image").asString
//                                }
//                            }
//                            if (data.has("address")) {
//                                if (!data.get("address").isJsonNull) {
//
//                                    val address = data.get("address").asString
//                                }
//                            }
//                            if (data.has("city")) {
//                                if (!data.get("city").isJsonNull) {
//
//                                    val city = data.get("city").asString
//                                }
//                            }
//                            if (data.has("state")) {
//                                if (!data.get("state").isJsonNull) {
//
//                                    val state = data.get("state").asString
//                                }
//                            }
//                            if (data.has("country")) {
//                                if (!data.get("country").isJsonNull) {
//
//                                    val country = data.get("country").asString
//                                }
//                            }
//                            if (data.has("discover_lesson_status")) {
//                                if (!data.get("discover_lesson_status").isJsonNull) {
//
//                                    val discoverLessonStatus =
//                                        data.get("discover_lesson_status").asString
//                                }
//                            }
//                            if (data.has("progression_model_status")) {
//                                if (!data.get("progression_model_status").isJsonNull) {
//
//                                    val progressionModelStatus =
//                                        data.get("progression_model_status").asString
//                                }
//                            }
//                            if (data.has("certificate_status")) {
//                                if (!data.get("certificate_status").isJsonNull) {
//
//                                    val certificateStatus = data.get("certificate_status").asString
//                                }
//                            }
//                            if (data.has("customer_id")) {
//                                if (!data.get("customer_id").isJsonNull) {
//
//                                    customer_id = data.get("customer_id").asString
//                                }
//                            }
//                            if (data.has("wavier_accepted")) {
//                                if (!data.get("wavier_accepted").isJsonNull) {
//
//                                    val wavierAccepted = data.get("wavier_accepted").asString
//                                }
//                            }
//                            if (data.has("gender")) {
//                                if (!data.get("gender").isJsonNull) {
//
//                                    val gender = data.get("gender").asString
//                                }
//                            }
//                            if (data.has("age")) {
//                                if (!data.get("age").isJsonNull) {
//
//                                    val age = data.get("age").asString
//                                }
//                            }
//                            if (data.has("weight")) {
//                                if (!data.get("weight").isJsonNull) {
//
//                                    val weight = data.get("weight").asString
//                                }
//                            }
//                            if (data.has("previous_experience")) {
//                                if (!data.get("previous_experience").isJsonNull) {
//
//                                    val previousExperience =
//                                        data.get("previous_experience").asString
//                                }
//                            }
//                            if (data.has("experience_description")) {
//                                if (!data.get("experience_description").isJsonNull) {
//
//                                    val experienceDescription =
//                                        data.get("experience_description").asString
//                                }
//                            }
//                            val roles = data.get("roles").asJsonArray
//                            for (i in 0 until roles.size()) {
//                                val jsonObj = roles.get(i).asJsonObject
//                                val model_id = jsonObj.get("model_id").asString
//                                val name1 = jsonObj.get("name").asString
//
//                                val pivot = jsonObj.get("pivot").asJsonObject
//                                val model_idP = pivot.get("model_id").asString
//                                val role_id = pivot.get("role_id").asString
//                                val model_type = pivot.get("model_type").asString
//                            }
                        }
                    }
                    if (response.code() == 401) {

                        CustomMethods().logOutUser(context!!)

                    }
                }
            })

    }

    private fun onClick() {

        btnPay.setOnClickListener {
            //create Stripe token
            if (!called) {
                called = true

                val card = cardInputWidget.card
                card?.validateNumber()
                card?.validateCVC()
                card?.name
                card?.addressLine1
//                card?.addressZip
                card?.addressCity
                card?.addressState
                card?.addressCountry

                Log.e("card", "" + card)

                if (card != null) {

                    if (!validation()) {


                        if (!card.validateCard()) {
                            // Show errors
                            Toast.makeText(
                                context,
                                "Invalid Card Data",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {

                            cardInputWidget.clear()
                            etName.setText("")
                            etAddress.setText("")
                            etPostalCode.setText("")
                            etCity.setText("")
                            etUnitedStates.setSelection(0)
                            etState.setSelection(0)

                            val stripe =
                                Stripe(
                                    context as Context,
                                    resources.getString(R.string.pk_token_stripe)
                                )
                            stripe.createCardToken(
                                card,
                                callback = object : ApiResultCallback<Token> {
                                    override fun onSuccess(token: Token) {
                                        Log.e(
                                            "Token!",
                                            "Token Created!!" + token.id
                                        )
                                        Toast.makeText(
                                            context,
                                            "Token Created!!",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
//                                    chargeCard(token.id)
                                        Preference().settokenId(context!!, token.id)

                                        //create card api[stripe api]
                                        createCard(token.id)
                                    }

                                    override fun onError(error: java.lang.Exception) {

                                        Toast.makeText(
                                            context,
                                            error.message,
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                        error.printStackTrace()
                                        called = false
                                    }
                                })


                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Please Enter Field First",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        called = false
                    }
                } else {

                    Toast.makeText(
                        context,
                        "Invalid Card Data",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    called = false
                }

            }

        }

        activity!!.back.setOnClickListener {
            CustomMethods().openPage(activity!!, MainActivity().locationFinder)
        }
    }

    private fun createCard(tokenId: kotlin.String) {
        val dialog: Dialog = CustomLoader().loader(context!!)
        val api: Api = Connection().getCon(context!!)
        api.createCard(
            "Bearer " + Preference().getStringStripeKey(context!!),
            Preference().getUserData(context!!)!!.customer_id,
            "" + tokenId

        ).enqueue(object : Callback, retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                CustomLoader().stopLoader(dialog)
            }

            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
                Log.e("responsebody", "asd->" + response.body())

                //List of Cards
                //change customer id IN URL which you get from Login
                //In Stripe API pass Secret Key in header
                CustomLoader().stopLoader(dialog)
                getCardList()
            }

        })
    }

    private fun getCardList() {
//        val dialog: Dialog = CustomLoader().loader(context!!)
        val api: Api = Connection().getConApi(context!!)
        api.listCard(
            Preference().getUserData(context!!)!!.customer_id,
            "Bearer " + Preference().getStringStripeKey(context!!)

        ).enqueue(object : Callback, retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
//                CustomLoader().stopLoader(dialog)
            }

            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
                try {

                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            Log.e("responsecard", "" + response.body())
//                            CustomLoader().stopLoader(dialog)
                            val data = response.body()!!.get("data").asJsonArray
                            if (data.size() > 0) {

                                cardListDetail.clear()
                                for (i in 0 until data.size()) {


                                    val jobj: JsonObject = data.get(i).asJsonObject
                                    val id = jobj.get("id").asString
                                    val brand = jobj.get("brand").asString
                                    val last4 = jobj.get("last4").asString
                                    Log.e("macro", "id--->$id")

                                    val cardmodel = CardListModel(id, brand, last4)

                                    var isIn = false

                                    for (j in cardListDetail) {

                                        if (j.brand == cardmodel.brand && j.last4 == cardmodel.last4) {

                                            isIn = true
                                        }
                                    }
                                    if (!isIn) {
                                        cardListDetail.add(cardmodel)
                                    }

                                }

                                adapter = CardDetailAdapter(
                                    context as MainActivity,
                                    cardListDetail,
                                    locationId,
                                    cost,
                                    timeSlot,
                                    type,
                                    date,
                                    isFromProgression,
                                    session_id,
                                    start_time,
                                    end_time,
                                    selected_instructor,
                                    selected_board
                                ) {
                                    val dialog: Dialog = CustomLoader().loader(context)
                                    val api: Api = Connection().getCon(requireActivity())
                                    api.paymentIntent(
                                        "Bearer " + Preference().getUserData(requireActivity())!!.token,
                                        "" + type,
                                        "" + locationId,
                                        "" + it.id,
                                        "" + session_id,
                                        "" + start_time,
                                        "" + end_time
                                    ).enqueue(object : Callback,
                                        retrofit2.Callback<JsonObject> {
                                        override fun onFailure(
                                            call: Call<JsonObject>,
                                            t: Throwable
                                        ) {
                                            CustomLoader().stopLoader(dialog)
                                            Log.e("error", "" + t)
                                        }


                                        override fun onResponse(
                                            call: Call<JsonObject>,
                                            response: Response<JsonObject>

                                        ) {
                                            CustomLoader().stopLoader(dialog)


                                            if (response.body() != null) {

                                                val data: JsonObject =
                                                    response.body()!!.get("data").asJsonObject
                                                val id = data.get("id").asString

                                                //RetrievePayment
                                                //In BaseUrl pass ID
                                                //In Stripe API pass Secret Key in header
                                                retrievePayments(id)


                                            }
                                        }

                                    })
                                }
                                recyclerCardlist.adapter = adapter
                                recyclerCardlist.layoutManager =
                                    LinearLayoutManager(context)

                                adapter.notifyDataSetChanged()

                            }
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "" + e, Toast.LENGTH_LONG).show()
                }

            }

        })
    }


    private fun init() {
        model = arguments!!.getSerializable("model") as PaymentDetails?
        tvScreenTitle = activity!!.findViewById(R.id.tv_screenTitle)
        tvScreenTitle.text = context!!.resources.getString(R.string.sessionPayment)
        drawerLayout = activity!!.findViewById(R.id.drawer_layout)
        btnPay = view!!.findViewById(R.id.btn_pay)
        cardInputWidget = view!!.findViewById(R.id.card_input_widget)

        activity!!.menu.visibility = View.GONE
        activity!!.bottom_navigation.visibility = View.VISIBLE
        activity!!.back.visibility = View.VISIBLE

        type = model!!.type
        locationId = model!!.id
        session_id = model!!.session_id
        start_time = model!!.start_time
        end_time = model!!.end_time
        if (type.equals("normal")) {
            selected_instructor = null.toString()
        } else {
            selected_instructor = model!!.instructor!!
        }

        selected_board = model!!.board


        cost = model!!.cost
        timeSlot = model!!.timeSlotId

        date = model!!.date
        isFromProgression = model!!.isFromProgression



        recyclerCardlist = view!!.findViewById(R.id.recycler_cardlist)
        cardListDetail = ArrayList()


        etName = view!!.findViewById(R.id.etName)
        etAddress = view!!.findViewById(R.id.etAddress)
        etApt = view!!.findViewById(R.id.etApt)
        etUnitedStates = view!!.findViewById(R.id.etUnitedStates)
        etPostalCode = view!!.findViewById(R.id.etPostalCode)
        etCity = view!!.findViewById(R.id.etCity)
        etState = view!!.findViewById(R.id.etState)

        val jsonFile =
            context!!.resources.assets.open("countrieslist.json").bufferedReader()
                .use { it.readText() }


        var jobject: JSONArray = JSONArray(jsonFile)

        var country_list_array = ArrayList<kotlin.String>()
        for (i in 0 until jobject.length()) {

            var array_obj = jobject.getJSONObject(i)
            var country_name = array_obj.getString("name")
            country_list_array.add(country_name)
        }
        Log.e("country_list_array", "" + country_list_array)

        var arrayAdapter: ArrayAdapter<kotlin.String> =
            ArrayAdapter<kotlin.String>(
                context!!,
                android.R.layout.simple_spinner_item,
                country_list_array
            )
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        etUnitedStates.adapter = arrayAdapter

        etUnitedStates.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
//
                    var state_list_array = ArrayList<kotlin.String>()
                    var state_list = jobject.getJSONObject(position).getJSONArray("states")
                    Log.e("state_list", "" + state_list)
                    if (state_list.length() > 0) {


                        for (j in 0 until state_list.length()) {
                            state_list_array.add(state_list.getJSONObject(j).getString("name").toString())
                        }
                    } else {
                        state_list_array.add("")
                    }
                    Log.e("state_list_array", "" + state_list_array)
                    var arrayAdapter1: ArrayAdapter<kotlin.String> =
                        ArrayAdapter<kotlin.String>(
                            context!!,
                            android.R.layout.simple_spinner_item,
                            state_list_array
                        )
                    arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    etState.adapter = arrayAdapter1

                }

            }
        etState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var state_list = jobject.getJSONObject(etUnitedStates.selectedItemPosition)
                    .getJSONArray("states")
                state_code =
                    state_list.getJSONObject(etState.selectedItemPosition).getString("code")
            }
        }

//        state_list_array.add(state_list.toString())
//

    }

    private fun validation(): Boolean {

        when {
            etName.text.toString().isEmpty() -> {
                etName.error = "Please Enter Name"
                validate = true
            }
            etAddress.text.toString().isEmpty() -> {
                etAddress.error = "Please Enter Address"
                validate = true
            }
//            etPostalCode.text.toString().isEmpty() -> {
//                etPostalCode.error = "Please Enter Postal Code"
//                validate = true
//            }
            etCity.text.toString().isEmpty() -> {
                etCity.error = "Please Enter City"
                validate = true
            }
        }
        return validate
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.e("activity", "acyivity")
        super.onActivityResult(requestCode, resultCode, data)

        val stripe =
            Stripe(
                context as Context,
                resources.getString(R.string.pk_token_stripe)
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
                        bookingCall(paymentIntent.id.toString())


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

    private fun retrievePayments(id: kotlin.String) {
        val dialog: Dialog = CustomLoader().loader(context)
        val api: Api = Connection().getConApi(requireActivity())
        api.retrievePayment(
            "Bearer " + Preference().getStringStripeKey(context!!), id

        ).enqueue(object : Callback, retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                CustomLoader().stopLoader(dialog)

            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        CustomLoader().stopLoader(dialog)


                        val retrieveId: kotlin.String = response.body()!!.get("id").asString
                        val paymentMethod: kotlin.String =
                            response.body()!!.get("payment_method").asString


                        //ConfirmPayment Intent
                        //In BaseUrl pass retrieveId
                        //In Stripe API pass Secret Key in header
                        confirmPaymentIntents(retrieveId, paymentMethod, id)
                        Log.e("intent",""+response.body())


                    }
                }
            }

        })
    }

    private fun confirmPaymentIntents(
        retrieveId: kotlin.String,
        paymentMethod: kotlin.String,
        id: kotlin.String
    ) {
        val dialog: Dialog = CustomLoader().loader(context)
        val api: Api = Connection().getConApiPayment(requireActivity())
        api.confirmPaymentIntent(
            retrieveId,
            "Bearer " + Preference().getStringStripeKey(context!!),
            "" + paymentMethod

        ).enqueue(object : Callback, retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                CustomLoader().stopLoader(dialog)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                CustomLoader().stopLoader(dialog)
                Log.e("confirmPaymnetre", "" + response.body())
                if (response.isSuccessful) {
                    if (response.body() != null) {

                        Preference().setConfirmPaymentResponse(
                            context!!,
                            response.body()?.asJsonObject
                        )
                        var status = response.body()!!.get("status").asString
                        if (status.equals("requires_source_action")) {

                            val stripe =
                                Stripe(
                                    context as Context,
                                    resources.getString(R.string.pk_token_stripe)
                                )

                            var client_secret = response.body()!!.get("client_secret").asString
                            stripe.handleNextActionForPayment(requireActivity(), client_secret)
                        } else {
                            bookingCall(id)
                        }


                    }
                }
            }

        })


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(cardlistModel: CardListModel) {

        bookingCall(cardlistModel.id)

    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    private fun bookingCall(id: kotlin.String) {
        val dialog: Dialog = CustomLoader().loader(context)
        val api: Api = Connection().getCon(requireActivity())

        if (type.equals("discovery_enrollment")) {
            //stripe payment(user booking history)
            api.stripePaymentEnrollment(
                "Bearer " + Preference().getUserData(requireActivity())!!.token,
                "" + type,
                "" + date,
                "" + locationId,
                "" + id,
                "" + cost,
                "" + Preference().getConfirmPaymentResponse(requireActivity())

            ).enqueue(object : Callback,
                retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    CustomLoader().stopLoader(dialog)

                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.e("macro", "tokenP->" + Preference().gettokenId(requireActivity()))


                    CustomLoader().stopLoader(dialog)
                    if (response.isSuccessful) {
                        if (response.body() != null) {


                            Toast.makeText(
                                context,
                                "" + resources.getString(R.string.successfully_paid),
                                Toast.LENGTH_LONG
                            ).show()

                            CustomMethods().openPage(requireActivity(), MainActivity().calendar)
                            activity!!.back.visibility = View.GONE
                            activity!!.menu.visibility = View.VISIBLE
                            CustomMethods().hideKeyboard(requireActivity())

                            (context as MainActivity).clearSelection()

                        }
                    } else {
                        if (response.code() == 401) {
                            Toast.makeText(
                                context,
                                "" + resources.getString(R.string.token_expired),
                                Toast.LENGTH_LONG
                            ).show()
                            CustomMethods().logOutUser(requireActivity())
                        } else {
                            Toast.makeText(
                                context,
                                "" + resources.getString(R.string.something_went_wrong),
                                Toast.LENGTH_LONG
                            ).show()
                            CustomMethods().openPage(requireActivity(), MainActivity().dashboard)
                            activity!!.back.visibility = View.GONE
                            activity!!.menu.visibility = View.VISIBLE
                            (context as MainActivity).clearSelection()
                            /* val jsonObject = JSONObject(response.errorBody()!!.string())
                             val message = jsonObject.getString("message")
                             Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show()*/
                        }

                    }
                }

            })

        }
        if (type.equals("discovery_lesson")) {
            api.stripePayment(
                "Bearer " + Preference().getUserData(requireContext())!!.token,
                "" + type,
                "" + date,
                "" + locationId,
                "" + id,
                "" + cost,
                "" + Preference().getConfirmPaymentResponse(requireActivity()),
                "" + selected_instructor,
                "" + selected_board,
                "" + start_time,
                "" + end_time,
                "" + session_id

            ).enqueue(object : Callback,
                retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    CustomLoader().stopLoader(dialog)

                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.e("type", "" + type)
                    Log.e("date", "" + date)
                    Log.e("locationId", "" + locationId)
                    Log.e("id", "" + id)
                    Log.e("cost", "" + cost)
                    Log.e("paymentInternt", "" + Preference().getConfirmPaymentResponse(requireActivity()))
                    Log.e("selected_instructor", "" + selected_instructor)
                    Log.e("selected_board", "" + selected_board)
                    Log.e("start_time", "" + start_time)
                    Log.e("end_time", "" + end_time)
                    Log.e("session_id", "" + session_id)

                    CustomLoader().stopLoader(dialog)


                    Log.e("macro", "resp->$response")
                    Log.e("response", "resp->${response.body()}")

                    if (response.isSuccessful) {
                        if (response.body() != null) {


                            Toast.makeText(
                                context,
                                "" + resources.getString(R.string.successfully_paid),
                                Toast.LENGTH_LONG
                            ).show()

                            CustomMethods().openPage(requireActivity(), MainActivity().calendar)
                            activity!!.back.visibility = View.GONE
                            activity!!.menu.visibility = View.VISIBLE
                            CustomMethods().hideKeyboard(requireActivity())

                            (context as MainActivity).clearSelection()

                        }
                    } else {
                        if (response.code() == 401) {
                            Toast.makeText(
                                context,
                                "" + resources.getString(R.string.token_expired),
                                Toast.LENGTH_LONG
                            ).show()
                            CustomMethods().logOutUser(requireContext())
                        } else {
                            Toast.makeText(
                                context,
                                "" + resources.getString(R.string.something_went_wrong),
                                Toast.LENGTH_LONG
                            ).show()
                            CustomMethods().openPage(requireActivity(), MainActivity().dashboard)
                            activity!!.back.visibility = View.GONE
                            activity!!.menu.visibility = View.VISIBLE
                            (context as MainActivity).clearSelection()
                            /* val jsonObject = JSONObject(response.errorBody()!!.string())
                             val message = jsonObject.getString("message")
                             Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show()*/
                        }

                    }
                }

            })
        }
        if ((type.equals("cruiser")) || (type.equals("explorer")) || (type.equals("sport")) || (type.equals(
                "pro"
            )) || ((type.equals("certificate")))
        ) {
            api.stripePaymentCruiser(
                "Bearer " + Preference().getUserData(requireContext())!!.token,
                "" + type,
                "" + date,
                "" + locationId,
                "" + id,
                "" + cost,
                "" + Preference().getConfirmPaymentResponse(requireActivity()),
                "" + selected_instructor,
                "" + selected_board,
                "" + start_time,
                "" + end_time

            ).enqueue(object : Callback,
                retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    CustomLoader().stopLoader(dialog)

                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.e("type", "" + type)
                    Log.e("date", "" + date)
                    Log.e("locationId", "" + locationId)
                    Log.e("id", "" + id)
                    Log.e("cost", "" + cost)
                    Log.e(
                        "payment_intent",
                        "" + Preference().getConfirmPaymentResponse(requireContext())
                    )

                    CustomLoader().stopLoader(dialog)


                    Log.e("macro", "resp->$response")
                    Log.e("response", "resp->${response.body()}")

                    if (response.isSuccessful) {
                        if (response.body() != null) {

                            Toast.makeText(
                                context,
                                "" + resources.getString(R.string.successfully_paid),
                                Toast.LENGTH_LONG
                            ).show()

                            CustomMethods().openPage(requireActivity(), MainActivity().calendar)
                            activity!!.back.visibility = View.GONE
                            activity!!.menu.visibility = View.VISIBLE
                            CustomMethods().hideKeyboard(requireActivity())

                            (context as MainActivity).clearSelection()

                        }
                    } else {
                        if (response.code() == 401) {
                            Toast.makeText(
                                context,
                                "" + resources.getString(R.string.token_expired),
                                Toast.LENGTH_LONG
                            ).show()
                            CustomMethods().logOutUser(requireContext())
                        } else {
                            Toast.makeText(
                                context,
                                "" + resources.getString(R.string.something_went_wrong),
                                Toast.LENGTH_LONG
                            ).show()
                            CustomMethods().openPage(requireActivity(), MainActivity().dashboard)
                            activity!!.back.visibility = View.GONE
                            activity!!.menu.visibility = View.VISIBLE
                            (context as MainActivity).clearSelection()
                            /* val jsonObject = JSONObject(response.errorBody()!!.string())
                             val message = jsonObject.getString("message")
                             Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show()*/
                        }

                    }
                }

            })
        }
        if (type.equals("normal")) {
            api.stripePaymentCruiser(
                "Bearer " + Preference().getUserData(requireContext())!!.token,
                "" + type,
                "" + date,
                "" + locationId,
                "" + id,
                "" + cost,
                "" + Preference().getConfirmPaymentResponse(requireActivity()),
                "",
                "" + selected_board,
                "" + start_time,
                "" + end_time

            ).enqueue(object : Callback,
                retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    CustomLoader().stopLoader(dialog)

                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.e("type", "" + type)
                    Log.e("date", "" + date)
                    Log.e("locationId", "" + locationId)
                    Log.e("id", "" + id)
                    Log.e("cost", "" + cost)

                    CustomLoader().stopLoader(dialog)


                    Log.e("macro", "resp->$response")
                    Log.e("response", "resp->${response.body()}")

                    if (response.isSuccessful) {
                        if (response.body() != null) {

                            Toast.makeText(
                                context,
                                "" + resources.getString(R.string.successfully_paid),
                                Toast.LENGTH_LONG
                            ).show()

                            CustomMethods().openPage(requireActivity(), MainActivity().calendar)
                            activity!!.back.visibility = View.GONE
                            activity!!.menu.visibility = View.VISIBLE
                            CustomMethods().hideKeyboard(requireActivity())

                            (context as MainActivity).clearSelection()

                        }
                    } else {
                        if (response.code() == 401) {
                            Toast.makeText(
                                context,
                                "" + resources.getString(R.string.token_expired),
                                Toast.LENGTH_LONG
                            ).show()
                            CustomMethods().logOutUser(requireActivity())
                        } else {
                            Toast.makeText(
                                context,
                                "" + resources.getString(R.string.something_went_wrong),
                                Toast.LENGTH_LONG
                            ).show()
                            CustomMethods().openPage(requireActivity(), MainActivity().dashboard)
                            activity!!.back.visibility = View.GONE
                            activity!!.menu.visibility = View.VISIBLE
                            (context as MainActivity).clearSelection()
                            /* val jsonObject = JSONObject(response.errorBody()!!.string())
                             val message = jsonObject.getString("message")
                             Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show()*/
                        }

                    }
                }

            })
        }

    }


}
