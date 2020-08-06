package com.surfboardapp.Fragments


import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.surfboardapp.Activity.MainActivity
import com.surfboardapp.CustomClasses.CustomLoader
import com.surfboardapp.CustomClasses.CustomMethods
import com.surfboardapp.Models.MapData
import com.surfboardapp.Preference.Preference
import com.surfboardapp.R
import com.surfboardapp.Retrofit.Api
import com.surfboardapp.Retrofit.Connection
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Response
import java.util.*
import javax.security.auth.callback.Callback


class BuyABoardFragment : Fragment() {
    private lateinit var drawerLayout: DrawerLayout

    private lateinit var tvScreenTitle: TextView
    private lateinit var txt_message: TextView
    private lateinit var lin_buy_a_board: LinearLayout
    private lateinit var txt_discount_code: TextView
    private lateinit var spinner_name: Spinner
    private lateinit var btn_submit: Button
    var md: ArrayList<MapData>? = null
    lateinit var id: String
    lateinit var name: String
    lateinit var location: String
    lateinit var image: String
    lateinit var foils: String
    lateinit var description: String
    lateinit var latitude: String
    lateinit var longitude: String
    lateinit var affiliate_user: String
    lateinit var Website: String
    lateinit var instructor_count: String
    lateinit var discount_code: String
    lateinit var show_discount_code: String
    var data: JsonArray? = null
    var mp: MapData? = null
    lateinit var selected_id: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_buy_aboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setDrawerSelection()
        apicall()
        onClick()
    }

    private fun apicall() {

        val dialog: Dialog = CustomLoader().loader(context)
        val api: Api = Connection().getCon(context!!)
        api.buy_board(
            "Bearer " + Preference().getUserData(context!!)?.token
        ).enqueue(object : Callback, retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                dialog.dismiss()
                txt_message.visibility = View.VISIBLE
                lin_buy_a_board.visibility = View.GONE
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                dialog.dismiss()
                Log.e("responseBuy", "" + response.body())
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        txt_message.visibility = View.GONE
                        lin_buy_a_board.visibility = View.VISIBLE
                        data = response.body()!!.get("data").asJsonArray
                        if (data!!.size() > 0) {
                            txt_message.visibility = View.GONE
                            lin_buy_a_board.visibility = View.VISIBLE
                            var board_name = ArrayList<String>()
                            for (i in 0 until data!!.size()) {

                                var jobj = data!!.get(i).asJsonObject
                                id = jobj.get("id").asString
                                name = jobj.get("name").asString
                                location = jobj.get("location").asString
                                image = jobj.get("image").asString
                                foils = jobj.get("foils").asString
                                description = jobj.get("description").asString
                                latitude = jobj.get("latitude").asString
                                longitude = jobj.get("longitude").asString
                                affiliate_user = jobj.get("affiliate_user").asString
                                Website = jobj.get("Website").asString
                                instructor_count = jobj.get("instructor_count").asString

                                if (jobj.get("discount_code").isJsonNull) {
                                    discount_code = ""
                                } else {
                                    discount_code = jobj.get("discount_code").asString
                                }



                                mp = MapData(
                                    false,
                                    id,
                                    name,
                                    description,
                                    foils,
                                    instructor_count,
                                    "", "",
                                    ""
                                )
                                md!!.add(mp!!)


                                board_name.add(name)

                                var arrayAdapter: ArrayAdapter<String> =
                                    ArrayAdapter<String>(
                                        context!!,
                                        android.R.layout.simple_spinner_item,
                                        board_name
                                    )
                                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                spinner_name.adapter = arrayAdapter

                                spinner_name.onItemSelectedListener =
                                    object : AdapterView.OnItemSelectedListener {
                                        override fun onNothingSelected(parent: AdapterView<*>?) {

                                        }

                                        override fun onItemSelected(
                                            parent: AdapterView<*>?,
                                            view: View?,
                                            position: Int,
                                            id: Long
                                        ) {

                                            show_discount_code =
                                                data!![spinner_name.selectedItemId.toInt()].asJsonObject.get(
                                                    "discount_code"
                                                ).asString

                                            if (show_discount_code.equals("")) {
                                                txt_discount_code.text = "--"
                                            } else {
                                                txt_discount_code.text = show_discount_code
                                            }


                                        }

                                    }


                            }

                        } else {
                            txt_message.visibility = View.VISIBLE
                            lin_buy_a_board.visibility = View.GONE
                        }

                    } else {
                        txt_message.visibility = View.VISIBLE
                        lin_buy_a_board.visibility = View.GONE
                    }

                }
                if (response.code() == 401) {

                    CustomMethods().logOutUser(context!!)

                }
            }
        })

    }

    private fun onClick() {
        activity!!.back.setOnClickListener {
            CustomMethods().openPage(activity!!, MainActivity().dashboard)
            activity!!.back.visibility = View.GONE
            activity!!.menu.visibility = View.VISIBLE
            activity!!.bottom_navigation.visibility = View.VISIBLE
            (activity as MainActivity).clearSelection()
        }
        btn_submit.setOnClickListener {

            val url = "https://liftfoils.com"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)

//            if (data!!.size() > 0) {
//                selected_id =
//                    data!![spinner_name.selectedItemId.toInt()].asJsonObject.get(
//                        "id"
//                    ).asString
//
//
//                bookingStatus(selected_id)
//
//
//            } else {
//                selected_id = ""
//            }

        }

    }

    private fun bookingStatus(selectedId: String) {
        val dialog: Dialog = CustomLoader().loader(context)
        val api: Api = Connection().getCon(context!!)
        api.bookingStatus(
            "Bearer " + Preference().getUserData(context!!)?.token,
            selectedId
        ).enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                dialog.dismiss()

            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                dialog.dismiss()
                if (response.isSuccessful) {
                    if (response.body() != null) {

                        try {


                            if (response.isSuccessful) {
                                if (response.body() != null) {

                                    Log.e("respanse", "" + response.body())
                                    var datas = response.body()!!.get("data").asJsonObject
                                    var allowed = datas.get("allowed").asString



                                    if (allowed.equals("true")) {


                                        CustomMethods().openPage(
                                            activity!!,
                                            MainActivity().locationFinderInner,
                                            mp!!
                                        )

                                    } else {
                                        if (datas.has("message")) {

                                            var messagess = datas.get("message").asString
                                            Toast.makeText(
                                                context!!,
                                                "" + messagess,
                                                Toast.LENGTH_LONG
                                            )
                                                .show()


                                        }

                                    }

                                }
                            }
                            if (response.code() == 401) {

                                CustomMethods().logOutUser(context!!)

                            }
                        } catch (e: Exception) {

                        }

                    }
                }
                if (response.code() == 401) {

                    CustomMethods().logOutUser(context!!)


                }
            }
        })


    }


    private fun init() {
        activity!!.back.visibility = View.VISIBLE
        tvScreenTitle = activity!!.findViewById(R.id.tv_screenTitle)
        spinner_name = view!!.findViewById(R.id.spinner_name)
        txt_discount_code = view!!.findViewById(R.id.txt_discount_code)
        btn_submit = view!!.findViewById(R.id.btn_submit)
        txt_message = view!!.findViewById(R.id.txt_message)
        lin_buy_a_board = view!!.findViewById(R.id.lin_buy_a_board)
        tvScreenTitle.text = context!!.resources.getString(R.string.buyAboard)
        md = ArrayList()


    }

    private fun setDrawerSelection() {
        drawerLayout = activity!!.findViewById(R.id.drawer_layout)
        val home = drawerLayout.findViewById<LinearLayout>(R.id.nav_buy_ll)
        val homeImg = drawerLayout.findViewById<ImageView>(R.id.nav_buy_img)
        val hometxt = drawerLayout.findViewById<TextView>(R.id.nav_buy_txt)

        home?.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorPrimaryDark))
        homeImg?.setColorFilter(ContextCompat.getColor(context!!, R.color.white))
        hometxt?.setTextColor(ContextCompat.getColor(context!!, R.color.white))
    }


}
