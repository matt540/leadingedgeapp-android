package com.surfboardapp.Fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.gson.JsonObject
import com.surfboardapp.Activity.MainActivity
import com.surfboardapp.Adapter.DashboardImage
import com.surfboardapp.CustomClasses.CustomLoader
import com.surfboardapp.CustomClasses.CustomMethods
import com.surfboardapp.Models.UserData
import com.surfboardapp.Preference.Preference
import com.surfboardapp.R
import com.surfboardapp.Retrofit.Api
import com.surfboardapp.Retrofit.Connection
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.util.*
import javax.security.auth.callback.Callback


class DashboardFragment : Fragment() {
    private lateinit var pager: ViewPager
    private val DELAY_MS1: Long = 2000
    private val PERIOD_MS1: Long = 2000
    private lateinit var builder: Dialog
    private lateinit var builderPrivacy: Dialog
    lateinit var dialog: View
    lateinit var dialogPrivacy: View
    private lateinit var btnDiscover: Button
    private lateinit var btnCertificate: Button
    private lateinit var btn_privacy_policy: Button
    private lateinit var txt_privacy_policy_link: TextView
    private lateinit var chckbox_privacy: CheckBox
    lateinit var value: String
    private lateinit var call: Call<JsonObject>

    private lateinit var tvScreenTitle: TextView
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dashboard_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setDrawerSelection()
        setBottomSelection()
        onClick()
        //get user
        apiCall()
    }

    private fun apiCall() {
        val dialog: Dialog = CustomLoader().loader(activity as MainActivity)
        val api = Connection().getCon(context!!)
        api.getUserData("Bearer " + Preference().getUserData(context!!)?.token)
            .enqueue(object : Callback,
                retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    CustomLoader().stopLoader(dialog)
                    Log.e("error", "" + t)
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    CustomLoader().stopLoader(dialog)
                    try {
                        if (response.isSuccessful) {
                            if (response.body() != null) {

                                Log.e("Macro", "" + response.body())
                                val data = response.body()!!.get("data").asJsonObject
                                val user = data!!.get("user").asJsonObject
                                val id = user.get("id").asString
                                val name = user.get("name").asString
                                val email = user.get("email").asString
                                val address = user.get("address").asString
                                val city = user.get("city").asString
                                val state = user.get("state").asString
                                val country = user.get("country").asString
                                val image = user.get("image").asString
                                val zipcode = user.get("zipcode").asString
                                val token = Preference().getUserToken(context!!)
                                val certificateStatus = user.get("certificate_status").asString
                                val progressionModelStatus =
                                    user.get("progression_model_status").asString
                                val discoverLessonStatus =
                                    user.get("discover_lesson_status").asString
                                val role = user.get("role").asString
                                val customerId = user.get("customer_id").asString
                                val wavier_accepted = user.get("wavier_accepted").asString
                                Preference().setUserData(
                                    context!!,
                                    UserData(
                                        id,
                                        name,
                                        email,
                                        image,
                                        zipcode,
                                        token,
                                        role,
                                        address,
                                        city,
                                        state,
                                        country,
                                        certificateStatus,
                                        progressionModelStatus,
                                        discoverLessonStatus, customerId, wavier_accepted
                                    )
                                )
                            }
                        } else {
                            val jsonObject = JSONObject(response.errorBody()!!.string())
                            val message = jsonObject.getString("message")
                            Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show()
                        }
                        if (response.code() == 401) {

                            CustomMethods().logOutUser(context!!)

                        }
                    } catch (e: Exception) {
                        Log.e("Macro", "" + e)
                    }
                }
            })
    }

    private fun onClick() {
        btnDiscover.setOnClickListener {
            builder.dismiss()

            //select path for discover
            choosePathApiDiscover()


        }
        btnCertificate.setOnClickListener {
            builder.dismiss()
            //select path for certification
            choosePathApiCertified()
        }
        btn_privacy_policy.setOnClickListener {

            try {


                if (chckbox_privacy.isChecked) {

                    builderPrivacy.dismiss()
                    Preference().setPrivacyPolicy(context!!, false)
                } else {
                    Toast.makeText(
                        context!!,
                        "Please Select the CheckBox First to continue",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: NullPointerException) {

            }
        }
    }


    private fun choosePathApiDiscover() {
        val dialog: Dialog = CustomLoader().loader(context)
        val api: Api = Connection().getCon(context!!)
        call = api.choosePath(
            "Bearer " + Preference().getUserData(context!!)?.token,
            "discovery_lession"
        )
        call.enqueue(object : Callback, retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                CustomLoader().stopLoader(dialog)

            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                CustomLoader().stopLoader(dialog)
                try {

                    if (response.isSuccessful) {
                        if (response.body() != null) {

                            val data = response.body()!!.get("data").asJsonObject
                            val id = data.get("id").asString
                            val name = data.get("name").asString
                            val email = data.get("email").asString
//                        val emailVerifiedAt = data.get("email_verified_at").asString
//                        val created_at = data.get("created_at").asString
//                        val updated_at = data.get("updated_at").asString
//                        val deleted_at = data.get("deleted_at").asString
                            val image = data.get("image").asString
                            val zipcode = data.get("zipcode").asString
                            val address = data.get("address").asString
                            val city = data.get("city").asString
                            val state = data.get("state").asString
                            val country = data.get("country").asString
                            val discoverLessonStatus = data.get("discover_lesson_status").asString
                            val progressionModelStatus =
                                data.get("progression_model_status").asString
                            val certificateStatus = data.get("certificate_status").asString
                            val wavier_accepted = data.get("wavier_accepted").asString
                            val customerId = Preference().getUserData(context!!)!!.customer_id



                            Preference().setUserData(
                                context!!,
                                UserData(
                                    id,
                                    name,
                                    email,
                                    image,
                                    zipcode,
                                    Preference().getUserToken(context!!),
                                    Preference().getUserData(context!!)!!.role,
                                    address,
                                    city,
                                    state,
                                    country,
                                    certificateStatus,
                                    progressionModelStatus,
                                    discoverLessonStatus, customerId, wavier_accepted
                                )
                            )
                            CustomMethods().openPage(activity!!, MainActivity().locationFinder)

                            (activity as MainActivity).clearSelection()

                        }
                    } else {

                        Toast.makeText(
                            context,
                            "" + resources.getString(R.string.something_went_wrong),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    if (response.code() == 401) {

                        CustomMethods().logOutUser(context!!)

                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "" + e, Toast.LENGTH_LONG).show()

                }
            }
        })
    }


    private fun choosePathApiCertified() {
        val dialog: Dialog = CustomLoader().loader(context)
        val api: Api = Connection().getCon(context!!)
        call = api.choosePath(
            "Bearer " + Preference().getUserData(context!!)?.token,
            "certificate"
        )
        call.enqueue(object : Callback, retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                CustomLoader().stopLoader(dialog)

            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                CustomLoader().stopLoader(dialog)
                try {

                    if (response.isSuccessful) {
                        if (response.body() != null) {

                            val data = response.body()!!.get("data").asJsonObject
                            val id = data.get("id").asString
                            val name = data.get("name").asString
                            val email = data.get("email").asString
//                        val emailVerifiedAt = data.get("email_verified_at").asString
//                        val created_at = data.get("created_at").asString
//                        val updated_at = data.get("updated_at").asString
//                        val deleted_at = data.get("deleted_at").asString
                            val image = data.get("image").asString
                            val zipcode = data.get("zipcode").asString
                            val address = data.get("address").asString
                            val city = data.get("city").asString
                            val state = data.get("state").asString
                            val country = data.get("country").asString
                            val wavier_accepted = data.get("wavier_accepted").asString
                            val discoverLessonStatus = data.get("discover_lesson_status").asString
                            val progressionModelStatus =
                                data.get("progression_model_status").asString
                            val certificateStatus = data.get("certificate_status").asString
                            val customerId = Preference().getUserData(context!!)!!.customer_id



                            Preference().setUserData(
                                context!!,
                                UserData(
                                    id,
                                    name,
                                    email,
                                    image,
                                    zipcode,
                                    Preference().getUserToken(context!!),
                                    Preference().getUserData(context!!)!!.role,
                                    address,
                                    city,
                                    state,
                                    country,
                                    certificateStatus,
                                    progressionModelStatus,
                                    discoverLessonStatus, customerId, wavier_accepted
                                )
                            )
                            CustomMethods().openPage(activity!!, MainActivity().locationFinder)

                            (activity as MainActivity).clearSelection()

                        }
                    } else {

                        Toast.makeText(
                            context,
                            "" + resources.getString(R.string.something_went_wrong),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    if (response.code() == 401) {

                        CustomMethods().logOutUser(context!!)

                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "" + e, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun setBottomSelection() {

        val dashboardImage = activity!!.findViewById<ImageView>(R.id.image_dashboard)
        val dashboardText = activity!!.findViewById<TextView>(R.id.text_dashboard)
        dashboardText?.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        dashboardImage?.setColorFilter(ContextCompat.getColor(context!!, R.color.black))
    }

    private fun setDrawerSelection() {
        drawerLayout = activity!!.findViewById(R.id.drawer_layout)
        val home = drawerLayout.findViewById<LinearLayout>(R.id.nav_home_ll)
        val homeImg = drawerLayout.findViewById<ImageView>(R.id.nav_home_img)
        val hometxt = drawerLayout.findViewById<TextView>(R.id.nav_home_txt)

        home?.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorPrimaryDark))
        homeImg?.setColorFilter(ContextCompat.getColor(context!!, R.color.white))
        hometxt?.setTextColor(ContextCompat.getColor(context!!, R.color.white))
    }

    @SuppressLint("InflateParams")
    private fun init() {
        pager = view!!.findViewById(R.id.pager)
        tvScreenTitle = activity!!.findViewById(R.id.tv_screenTitle)
        tvScreenTitle.text = context!!.resources.getString(R.string.dashboard)

        val mResources = intArrayOf(
            R.drawable.one,
            R.drawable.dashboard4,
            R.drawable.dashboard2,
            R.drawable.dashboard3,
            R.drawable.dashboard1,
            R.drawable.dashboard5
        )

        val adapter = DashboardImage(context!!, mResources)
        pager.adapter = adapter
        MyTimerHeader(
            DELAY_MS1,
            PERIOD_MS1,
            pager,
            mResources.size
        )

        builder = Dialog(context!!)
        builderPrivacy = Dialog(context!!)
        val layoutInflater = LayoutInflater.from(context!!)
        dialog = layoutInflater.inflate(R.layout.login_popup, null)
        dialogPrivacy = layoutInflater.inflate(R.layout.privacy_popup, null)
        btnDiscover = dialog.findViewById(R.id.btn_discover)
        btnCertificate = dialog.findViewById(R.id.btn_certificate)
        btn_privacy_policy = dialogPrivacy.findViewById(R.id.btn_privacy_policy)
        txt_privacy_policy_link = dialogPrivacy.findViewById(R.id.txt_privacy_policy_link)
        chckbox_privacy = dialogPrivacy.findViewById(R.id.chckbox_privacy)

        txt_privacy_policy_link.text = Html.fromHtml(resources.getString(R.string.register_terms))


        if (Preference().getPrivacyPolicy(context!!)) {

            builderPrivacy.setCancelable(false)
            builderPrivacy.setContentView(dialogPrivacy)
            builderPrivacy.show()

        }





        if (Preference().getUserData(context!!)!!.discover_lesson_status == "0" &&
            Preference().getUserData(context!!)!!.certificate_status == "0"
        ) {
            builder.setCancelable(false)
            builder.setContentView(dialog)
            builder.show()
        }

        activity!!.menu.visibility = View.VISIBLE
        activity!!.bottom_navigation.visibility = View.VISIBLE
        activity!!.back.visibility = View.GONE
    }

    internal class MyTimerHeader(
        delayMs1: Long,
        periodMs1: Long,
        pager: ViewPager,
        length: Int
    ) {
        init {
            val handler = Handler()
            val update = Runnable {
                var currentPage = pager.currentItem
                currentPage = if (currentPage >= length - 1) {
                    0
                } else {
                    currentPage + 1
                }
                pager.currentItem = currentPage
            }
            val timer = Timer() // This will create a new Thread
            timer.schedule(object : TimerTask() {
                // task to be scheduled
                override fun run() {
                    handler.post(update)
                }
            }, delayMs1, periodMs1)
        }
    }

}
