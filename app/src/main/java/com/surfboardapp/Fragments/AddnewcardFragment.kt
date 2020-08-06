package com.surfboardapp.Fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.gson.JsonObject
import com.stripe.android.ApiResultCallback
import com.stripe.android.Stripe
import com.stripe.android.model.Token
import com.stripe.android.view.CardInputWidget
import com.surfboardapp.Activity.MainActivity
import com.surfboardapp.CustomClasses.CustomLoader
import com.surfboardapp.CustomClasses.CustomMethods
import com.surfboardapp.Preference.Preference
import com.surfboardapp.R
import com.surfboardapp.Retrofit.Api
import com.surfboardapp.Retrofit.Connection
import kotlinx.android.synthetic.main.app_bar_main.*
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

/**
 * A simple [Fragment] subclass.
 */
class AddnewcardFragment : Fragment() {
    private lateinit var tvScreenTitle: TextView
    private lateinit var back: ImageView
    private var validate: Boolean = false
    private lateinit var etName: EditText
    private lateinit var etAddress: EditText
    private lateinit var etApt: EditText
    private lateinit var etUnitedStates: Spinner
    private lateinit var etPostalCode: EditText
    private lateinit var etCity: EditText
    private lateinit var etState: Spinner
    private lateinit var btnPay: Button
    var called = false
    private lateinit var cardInputWidget: CardInputWidget

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_addnewcard, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        click()
    }

    private fun click() {


        activity!!.back.setOnClickListener {
            CustomMethods().openPage(
                activity!!,
                MainActivity().membership
            )
            activity!!.back.visibility = View.VISIBLE
            activity!!.menu.visibility =
                View.GONE
            (activity as MainActivity).clearSelection()
        }


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
//
//                Log.e("card", "" + card)

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
    }

    private fun createCard(tokenId: String) {
        val dialog: Dialog = CustomLoader().loader(context)
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
                CustomMethods().openPage(
                    activity!!,
                    MainActivity().membership
                )
                activity!!.back.visibility = View.VISIBLE
                activity!!.menu.visibility =
                    View.GONE
                (activity as MainActivity).clearSelection()

            }

        })

    }

    private fun init() {
        tvScreenTitle = activity!!.findViewById(R.id.tv_screenTitle)
        tvScreenTitle.text = "Add New Card"

        btnPay = view!!.findViewById(R.id.btn_pay)
        cardInputWidget = view!!.findViewById(R.id.card_input_widget)
        activity!!.menu.visibility = View.GONE
        activity!!.back.visibility = View.VISIBLE
        back = activity!!.findViewById(R.id.back)

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
                var state_code =
                    state_list.getJSONObject(etState.selectedItemPosition).getString("code")
            }
        }

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

}
