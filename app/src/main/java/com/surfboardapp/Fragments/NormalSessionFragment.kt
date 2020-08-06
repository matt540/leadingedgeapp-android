package com.surfboardapp.Fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.JsonObject
import com.stripe.android.Stripe
import com.stripe.android.view.CardInputWidget
import com.surfboardapp.Activity.MainActivity
import com.surfboardapp.CustomClasses.CustomLoader
import com.surfboardapp.CustomClasses.CustomMethods
import com.surfboardapp.Models.CardListModel
import com.surfboardapp.Preference.Preference
import com.surfboardapp.R
import com.surfboardapp.Retrofit.Api
import com.surfboardapp.Retrofit.Connection
import kotlinx.android.synthetic.main.app_bar_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

/**
 * A simple [Fragment] subclass.
 */
class NormalSessionFragment : Fragment() {

    private lateinit var tvScreenTitle: TextView
    private lateinit var card_input_widget_normal_session: CardInputWidget
    private lateinit var btn_pay_normal_seesion: Button
    private lateinit var txt_membership_price: TextView
    var called = false
    var card_number: String = ""
    var card_expiry_month: String = ""
    var card_expiry_year: String = ""
    var card_expiry_cvc: String = ""
    var id_data: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_normal_session, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        apicall()
        click()
    }

    private fun apicall() {
        val api: Api = Connection().getCon(context!!)
        api.user_membership(
            "Bearer " + Preference().getUserData(context!!)?.token
        ).enqueue(object : Callback, retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                txt_membership_price.text = "Something went wrong"
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {

                        var data = response.body()!!.get("data").asJsonObject
                        var price = data.get("price").asString
                        txt_membership_price.text = "Membership Fees $ " + price


                    } else {
                        txt_membership_price.text = "Something went wrong"
                    }
                }
                if (response.code() == 401) {

                    CustomMethods().logOutUser(context!!)

                }
            }
        })
    }

    private fun click() {
        btn_pay_normal_seesion.setOnClickListener {

            if (!called) {
                called = true

                val card = card_input_widget_normal_session.card
                card?.validateNumber()
                card?.validateCVC()
                if (card != null) {
                    if (!card.validateCard()) {
                        // Show errors
                        Toast.makeText(
                            context,
                            "Invalid Card Data",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {

                        card_number = card.number!!
                        card_expiry_month = card.expMonth?.toString()!!
                        card_expiry_year = card.expYear?.toString()!!
                        card_expiry_cvc = card.cvc!!

                        val dialog: Dialog = CustomLoader().loader(context)
                        val api: Api = Connection().getNormalSessionApi(context!!)
                        api.getPaymentMethods(
                            "Bearer " + Preference().getStringStripeKey(context!!), "card",
                            "" + card_number, "" + card_expiry_month, "" + card_expiry_year,
                            "" + card_expiry_cvc
                        )
                            .enqueue(object : Callback, retrofit2.Callback<JsonObject> {
                                override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                                    dialog.dismiss()
                                    called = false
                                }

                                override fun onResponse(
                                    call: Call<JsonObject>,
                                    response: Response<JsonObject>
                                ) {
                                    dialog.dismiss()
                                    Log.e("res", "" + response)
                                    Log.e("resbody", "" + response.body())
                                    try {
                                        if (response.isSuccessful) {
                                            if (response.body() != null) {

                                                var id = response.body()!!.get("id").asString

                                                subscriptionCreate(id)


                                            }
                                        }
                                        if (response.code() == 401) {

                                            CustomMethods().logOutUser(context!!)

                                        }

                                    } catch (e: Exception) {

                                    }

                                }

                            })


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
    }

    private fun subscriptionCreate(id: String) {

        val api: Api = Connection().getCon(context!!)
        api.subscriptionCreate(
            "Bearer " + Preference().getUserData(context!!)?.token,
            id
        ).enqueue(object : Callback, retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                Log.e("fail", "" + t.toString())
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                Log.e("responsesubs", "" + response.body())
                try {
                    if (response.isSuccessful) {
                        if (response.body() != null) {

                            var data = response.body()!!.get("data").asJsonObject
                            id_data = data.get("id").asString

                            Log.e("id_data", "" + id_data)

                            var latest_invoice = data.get("latest_invoice").asJsonObject
                            var payment_intent = latest_invoice.get("payment_intent").asJsonObject

                            var client_secret = payment_intent.get("client_secret").asString
                            var status = payment_intent.get("status").asString

                            if (status.equals("requires_source_action")) {

                                val stripe =
                                    Stripe(
                                        context as Context,
                                        resources.getString(R.string.pk_token_stripe)
                                    )

                                stripe.handleNextActionForPayment(requireActivity(), client_secret)

                            } else {
                                bookingCall(id_data)
                            }


                        }
                    }
                    if (response.code() == 401) {

                        CustomMethods().logOutUser(context!!)

                    }

                } catch (e: Exception) {

                }
            }

        })
    }

    private fun init() {
        tvScreenTitle = activity!!.findViewById(R.id.tv_screenTitle)
        tvScreenTitle.text = context!!.resources.getString(R.string.normalLesson)

        card_input_widget_normal_session =
            view!!.findViewById(R.id.card_input_widget_normal_session)
        btn_pay_normal_seesion = view!!.findViewById(R.id.btn_pay_normal_seesion)
        txt_membership_price = view!!.findViewById(R.id.txt_membership_price)


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(cardlistModel: CardListModel) {

        bookingCall(id_data)

    }

    private fun bookingCall(idData: String) {
        val dialog: Dialog = CustomLoader().loader(requireContext())
        val api: Api = Connection().getCon(requireContext())
        api.subscription_confirm(
            "Bearer " + Preference().getUserData(requireContext())?.token,
            "" + idData

        ).enqueue(object : Callback, retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                dialog.dismiss()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                dialog.dismiss()
                Log.e("responsescon", "" + response.body())
                if (response.isSuccessful) {
                    if (response.body() != null) {

                        val builder = AlertDialog.Builder(context)
                        //set title for alert dialog
                        builder.setTitle(R.string.app_name)
                        //set message for alert dialog
                        builder.setMessage("Congratulations Your Membership is Activated")

                        //performing positive action
                        builder.setPositiveButton("OK") { dialogInterface, which ->
                            dialogInterface.dismiss()
                            CustomMethods().openPage(
                                activity!!,
                                MainActivity().dashboard
                            )
                            activity!!.back.visibility = View.GONE
                            activity!!.menu.visibility = View.VISIBLE
                            (activity as MainActivity).clearSelection()
                        }

                        // Create the AlertDialog
                        val alertDialog: AlertDialog = builder.create()
                        // Set other dialog properties
                        alertDialog.setCancelable(false)
                        alertDialog.show()

                    }
                }
                if (response.code() == 401) {
                    CustomMethods().logOutUser(requireContext())
                }

            }
        })

    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}
