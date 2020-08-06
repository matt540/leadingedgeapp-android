package com.surfboardapp.Fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.surfboardapp.Activity.MainActivity
import com.surfboardapp.Adapter.CardListAdapterMembership
import com.surfboardapp.CustomClasses.CustomLoader
import com.surfboardapp.CustomClasses.CustomMethods
import com.surfboardapp.Models.CardListModelMembership
import com.surfboardapp.Preference.Preference
import com.surfboardapp.R
import com.surfboardapp.Retrofit.Api
import com.surfboardapp.Retrofit.Connection
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

/**
 * A simple [Fragment] subclass.
 */
class MembershipFragment : Fragment() {

    private lateinit var tvScreenTitle: TextView
    private lateinit var back: ImageView
    private lateinit var txt_subscription_id: TextView
    private lateinit var lin_membership_details: LinearLayout
    private lateinit var txt_plan_id: TextView
    private lateinit var txt_customer_id: TextView
    private lateinit var txt_subscription_cost: TextView
    private lateinit var txt_subscription_start_date: TextView
    private lateinit var txt_subscription_end_date: TextView
    private lateinit var txt_warning_message: TextView
    private lateinit var txt_subscription_status: TextView
    private lateinit var txt_add_new_card: TextView
    private lateinit var txt_description: TextView
    private lateinit var btn_membership_submit: Button
    private lateinit var rv_cardDetails_membership: RecyclerView
    lateinit var adapter: CardListAdapterMembership
    lateinit var cardList: ArrayList<CardListModelMembership>
    private var isSame: String = "0"
    private lateinit var past_due: String
    private lateinit var subscription_id: String
    private lateinit var customer: String
    private lateinit var plan_id: String
    private lateinit var cancelt_at_period_end: String
    private lateinit var cost: String
    private lateinit var start: String
    private lateinit var end: String
    private lateinit var status: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_membership, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setBottomSelection()
        apicall()
        click()

    }


    private fun setBottomSelection() {
        val dashboardImage = activity!!.findViewById<ImageView>(R.id.image_dashboard)
        val dashboardText = activity!!.findViewById<TextView>(R.id.text_dashboard)
        dashboardText?.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        dashboardImage?.setColorFilter(ContextCompat.getColor(context!!, R.color.black))

    }

    private fun apicall() {
        val dialog: Dialog = CustomLoader().loader(context)
        val api: Api = Connection().getCon(context!!)
        api.membership_fetch(
            "Bearer " + Preference().getUserData(context!!)?.token
        ).enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                dialog.dismiss()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                dialog.dismiss()
                try {


                    if (response.isSuccessful) {
                        if (response.body() != null) {

                            Log.e("responsebody", "" + response.body())
                            var data = response.body()!!.get("data").asJsonObject
                            var membership = data.get("membership").asJsonObject

                            if (!membership.equals("")) {

                                lin_membership_details.visibility = View.VISIBLE

                                past_due = membership.get("past_due").asString
                                subscription_id = membership.get("subscription_id").asString
                                customer = membership.get("customer").asString
                                plan_id = membership.get("plan_id").asString
                                cancelt_at_period_end =
                                    membership.get("cancelt_at_period_end").asString
                                cost = membership.get("cost").asString
                                start = membership.get("start").asString
                                end = membership.get("end").asString
                                status = membership.get("status").asString


                                txt_subscription_id.text = subscription_id
                                txt_customer_id.text = customer
                                txt_plan_id.text = plan_id
                                txt_subscription_cost.text = "$ " + cost
                                txt_subscription_start_date.text = start
                                txt_subscription_end_date.text = end
                                txt_subscription_status.text = status
                            } else {
                                lin_membership_details.visibility = View.GONE
                            }


                            var allow_start_membership = data.get("allow_start_membership").asString

                            Log.e("status", "" + status)
                            Log.e("cancelt_at_period_end", "" + cancelt_at_period_end)
                            Log.e("past_due", "" + past_due)
                            Log.e("allow_start_membership", "" + allow_start_membership)

                            if ((status.equals("active")) && (cancelt_at_period_end.equals("true"))
                                && (past_due.equals("false")) && (allow_start_membership.equals("false"))
                            ) {

                                txt_description.text =
                                    "If you want to Restart your membership, please click the button below"
                                btn_membership_submit.text = "Restart Membership"
                                btn_membership_submit.setBackgroundColor(
                                    ContextCompat.getColor(
                                        context!!,
                                        R.color.color_green
                                    )
                                )
                            }

                            if ((status.equals("active")) && (cancelt_at_period_end.equals("false"))
                                && (past_due.equals("false")) && (allow_start_membership.equals("false"))
                            ) {
                                txt_description.text =
                                    "If you want to Cancel your membership, please click the button below"
                                btn_membership_submit.text = "Cancel Membership"
                                btn_membership_submit.setBackgroundColor(
                                    ContextCompat.getColor(
                                        context!!,
                                        R.color.color_red
                                    )
                                )
                            }

                            if (((status.equals("canceled")) && (allow_start_membership.equals("true"))) || ((membership.equals(
                                    ""
                                )) && (allow_start_membership.equals("true")))
                            ) {
                                txt_description.text =
//                                    "You are not eligible for Membership" +
//                                            System.getProperty("line.separator") +
//                                            "To become a member please schedule a discovery session or continue through the Progression Program" +
//                                            System.getProperty("line.separator") +
                                            "If you want to Start your membership, please click the button below"
                                btn_membership_submit.text = "Start Membership"
                                btn_membership_submit.setBackgroundColor(
                                    ContextCompat.getColor(
                                        context!!,
                                        R.color.colorPrimaryDark
                                    )
                                )
                            }

                            if ((status.equals("active")) && (cancelt_at_period_end.equals("true"))) {
                                txt_warning_message.visibility = View.VISIBLE
                            } else {
                                txt_warning_message.visibility = View.GONE
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

        retrieveCustomer()
    }

    private fun startMembership() {
        CustomMethods().openPage(
            activity!!,
            MainActivity().normalSession
        )
        activity!!.back.visibility = View.GONE
        activity!!.menu.visibility =
            View.VISIBLE
        (activity as MainActivity).clearSelection()
    }

    private fun cancelMembership() {
        val dialog: Dialog = CustomLoader().loader(context)
        Log.e("token", "" + Preference().getUserData(context!!)?.token)
        val api: Api = Connection().getCon(context!!)
        api.membership_cancel(
            "Bearer " + Preference().getUserData(context!!)?.token
        ).enqueue(object : Callback, retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                dialog.dismiss()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                dialog.dismiss()
                Log.e("responsecancel", "" + response.body())
                Log.e("code", "" + response.code())
                if (response.isSuccessful) {
                    if (response.body() != null) {

                        Toast.makeText(context, "Your Membership Canceled", Toast.LENGTH_LONG)
                            .show()
                        CustomMethods().openPage(activity!!, MainActivity().membership)
                        activity!!.menu.visibility = View.GONE
                        activity!!.back.visibility = View.VISIBLE
                        (activity as MainActivity).clearSelection()

                    }


                }
                if (response.code() == 401) {
                    CustomMethods().logOutUser(context!!)
                }
            }
        })

    }

    private fun restartMembership() {
        val dialog: Dialog = CustomLoader().loader(context)
        val api: Api = Connection().getCon(context!!)
        Log.e("token", "" + Preference().getUserData(context!!)?.token)
        api.membership_restart(
            "Bearer " + Preference().getUserData(context!!)?.token
        ).enqueue(object : Callback, retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                dialog.dismiss()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                dialog.dismiss()
                Log.e("responseRestart", "" + response.body())
                Log.e("code", "" + response.code())
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        Toast.makeText(context, "Your Membership Restart", Toast.LENGTH_LONG).show()
                        CustomMethods().openPage(activity!!, MainActivity().membership)
                        activity!!.menu.visibility = View.GONE
                        activity!!.back.visibility = View.VISIBLE
                        (activity as MainActivity).clearSelection()

                    }
                }
                if (response.code() == 401) {
                    CustomMethods().logOutUser(context!!)
                }
            }

        })
    }

    private fun retrieveCustomer() {

        val api: Api = Connection().getConApi(context!!)
        api.retrieveCustomer(
            "Bearer " + Preference().getStringStripeKey(context!!),
            Preference().getUserData(context!!)!!.customer_id

        ).enqueue(object : Callback, retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.isSuccessful) {
                    if (response.body() != null) {

                        var invoice_settings =
                            response.body()!!.get("invoice_settings").asJsonObject
                        var default_payment_method =
                            invoice_settings.get("default_payment_method").asString
                        getPaymentodMethod(default_payment_method)

                    }
                }
                if (response.code() == 401) {
                    CustomMethods().logOutUser(context!!)
                }

            }
        })
    }

    private fun getPaymentodMethod(defaultPaymentMethod: String) {
        val dialog: Dialog = CustomLoader().loader(context)
        val api: Api = Connection().getConApi(context!!)

        api.retrievePaymentMethod(
            "Bearer " + Preference().getStringStripeKey(context!!),
            Preference().getUserData(context!!)!!.customer_id,
            "card"

        ).enqueue(object : Callback, retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                dialog.dismiss()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                dialog.dismiss()
                Log.e("getPaymentRes", "" + response.body())
                if (response.isSuccessful) {
                    if (response.body() != null) {

                        var data = response.body()!!.get("data").asJsonArray
                        if (data.size() > 0) {


                            for (i in 0 until data.size()) {

                                var jobject = data.get(i).asJsonObject

                                var ids = jobject.get("id").asString

                                Log.e("ids", "" + ids)
                                Log.e("defaultPaymentMethod", "" + defaultPaymentMethod)
                                if (ids.equals(defaultPaymentMethod)) {
                                    isSame = "1"
                                } else {
                                    isSame = "0"
                                }

                                var card = jobject.get("card").asJsonObject
                                val id = ""
                                val brand = card.get("brand").asString
                                val last4 = card.get("last4").asString


                                val cardmodel = CardListModelMembership(ids, brand, last4, isSame)
                                cardList.add(cardmodel)
//                                var isIn = false
//
//                                for (j in cardList) {
//
//                                    if (j.brand == cardmodel.brand && j.last4 == cardmodel.last4) {
//
//                                        isIn = true
//                                    }
//                                }
//                                if (!isIn) {
//                                    cardList.add(cardmodel)
//                                }


                            }
                            adapter = CardListAdapterMembership(
                                context as MainActivity,
                                cardList, isSame
                            )
                            rv_cardDetails_membership.adapter = adapter
                            rv_cardDetails_membership.layoutManager =
                                LinearLayoutManager(context)

                            adapter.notifyDataSetChanged()
                        }

                    }
                }
                if (response.code() == 401) {
                    CustomMethods().logOutUser(context!!)
                }
            }
        })
    }

    private fun click() {
        activity!!.back.setOnClickListener {
            CustomMethods().openPage(activity!!, MainActivity().dashboard)
            activity!!.back.visibility = View.GONE
            activity!!.menu.visibility = View.VISIBLE
            activity!!.bottom_navigation.visibility = View.VISIBLE
            (activity as MainActivity).clearSelection()
        }

        btn_membership_submit.setOnClickListener {

            if (btn_membership_submit.text.equals("Restart Membership")) {

                btn_membership_submit.setBackgroundColor(
                    ContextCompat.getColor(
                        context!!,
                        R.color.color_green
                    )
                )

                restartMembership()

            } else if (btn_membership_submit.text.equals("Cancel Membership")) {
                btn_membership_submit.setBackgroundColor(
                    ContextCompat.getColor(
                        context!!,
                        R.color.color_red
                    )
                )

                cancelMembership()

            } else if (btn_membership_submit.text.equals("Start Membership")) {
                btn_membership_submit.setBackgroundColor(
                    ContextCompat.getColor(
                        context!!,
                        R.color.colorPrimaryDark
                    )
                )

                startMembership()

            } else {

            }

        }

        txt_add_new_card.setOnClickListener {
            CustomMethods().openPage(activity!!, MainActivity().newCard)
            activity!!.menu.visibility = View.GONE
            activity!!.back.visibility = View.VISIBLE
            (activity as MainActivity).clearSelection()
        }
    }

    private fun init() {
        tvScreenTitle = activity!!.findViewById(R.id.tv_screenTitle)
        tvScreenTitle.text = "Membership"

        back = activity!!.findViewById(R.id.back)
        txt_subscription_id = view!!.findViewById(R.id.txt_subscription_id)
        txt_plan_id = view!!.findViewById(R.id.txt_plan_id)
        txt_customer_id = view!!.findViewById(R.id.txt_customer_id)
        txt_subscription_cost = view!!.findViewById(R.id.txt_subscription_cost)
        txt_subscription_start_date = view!!.findViewById(R.id.txt_subscription_start_date)
        txt_warning_message = view!!.findViewById(R.id.txt_warning_message)
        txt_subscription_end_date = view!!.findViewById(R.id.txt_subscription_end_date)
        txt_subscription_status = view!!.findViewById(R.id.txt_subscription_status)
        rv_cardDetails_membership = view!!.findViewById(R.id.rv_cardDetails_membership)
        btn_membership_submit = view!!.findViewById(R.id.btn_membership_submit)
        txt_add_new_card = view!!.findViewById(R.id.txt_add_new_card)
        txt_description = view!!.findViewById(R.id.txt_description)
        lin_membership_details = view!!.findViewById(R.id.lin_membership_details)
        cardList = ArrayList()

    }

}
