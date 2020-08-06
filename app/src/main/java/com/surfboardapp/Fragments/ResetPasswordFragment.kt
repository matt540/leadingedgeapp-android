package com.surfboardapp.Fragments


import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.JsonObject
import com.surfboardapp.CustomClasses.CustomLoader
import com.surfboardapp.CustomClasses.CustomMethods
import com.surfboardapp.Preference.Preference
import com.surfboardapp.R
import com.surfboardapp.Retrofit.Api
import com.surfboardapp.Retrofit.Connection
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ResetPasswordFragment : Fragment() {

    private lateinit var edtOldPassword: EditText
    private lateinit var edtNewPassword: EditText
    lateinit var edtConfirmNewPassword: EditText
    private lateinit var btnSubmit: Button
    private lateinit var tvScreenTitle: TextView
    private var validate: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reset_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        click()
    }

    private fun click() {

        btnSubmit.setOnClickListener {
            //reset password

            if (!validate()) {

                val dialog: Dialog = CustomLoader().loader(context)
                val api: Api = Connection().getCon(context!!)

                api.getResetPassword(
                    "Bearer " + Preference().getUserData(context!!)?.token,
                    edtOldPassword.text.toString(),
                    edtNewPassword.text.toString(),
                    edtConfirmNewPassword.text.toString()
                ).enqueue(object : Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        Log.e("error", "" + t)
                        CustomLoader().stopLoader(dialog)
                    }

                    override fun onResponse(
                        call: Call<JsonObject>,
                        response: Response<JsonObject>
                    ) {
                        try {
                            Log.e("resetresponse", "" + response.body())
                            CustomLoader().stopLoader(dialog)
                            if (response.isSuccessful) {
                                if (response.body() != null) {
                                    val jsonObject = JSONObject(response.body().toString())
                                    val message = jsonObject.getString("message")
                                    Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show()

                                    val myAccountFragment = MyAccountFragment()
                                    val transaction = fragmentManager!!.beginTransaction()
                                    transaction.replace(
                                        R.id.nav_host,
                                        myAccountFragment
                                    )
                                    transaction.addToBackStack(null)
                                    transaction.commit()
                                }

                                Preference().setUserPassword(
                                    context!!,
                                    edtConfirmNewPassword.text.toString()
                                )
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
        }
    }

    private fun validate(): Boolean {
        when {
            edtOldPassword.text.toString().isEmpty() -> {
                edtOldPassword.error = getString(R.string.enter_old_password)
                validate = true
            }
            edtNewPassword.text.toString().isEmpty() -> {
                edtNewPassword.error = getString(R.string.enter_new_password)
                validate = true
            }
            edtConfirmNewPassword.text.toString().isEmpty() -> {
                edtConfirmNewPassword.error = getString(R.string.enter_confirm_new_password)
                validate = true
            }
        }
        return validate
    }

    private fun init() {
        tvScreenTitle = activity!!.findViewById(R.id.tv_screenTitle)
        tvScreenTitle.text = context!!.resources.getString(R.string.resetPassword)

        edtOldPassword = view!!.findViewById(R.id.edt_old_password)
        edtNewPassword = view!!.findViewById(R.id.edt_new_password)
        edtConfirmNewPassword = view!!.findViewById(R.id.edt_confirm_new_password)
        btnSubmit = view!!.findViewById(R.id.btn_submit)
    }
}
