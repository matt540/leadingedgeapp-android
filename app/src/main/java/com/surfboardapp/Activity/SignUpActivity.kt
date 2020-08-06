package com.surfboardapp.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.surfboardapp.CustomClasses.CustomLoader
import com.surfboardapp.CustomClasses.CustomMethods
import com.surfboardapp.Language.AppUtils
import com.surfboardapp.Models.UserData
import com.surfboardapp.Preference.Preference
import com.surfboardapp.R
import com.surfboardapp.Retrofit.Api
import com.surfboardapp.Retrofit.Connection
import kotlinx.android.synthetic.main.activity_sign_up.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*

class SignUpActivity : AppCompatActivity() {

    lateinit var call: Call<JsonObject>
    private var validate: Boolean = false
    private val NewPickImage = 1
    private lateinit var file: File
    private lateinit var images: MultipartBody.Part


    private lateinit var btnRegisterNow: Button
    private lateinit var btnRegisterNowProfile: Button
    private lateinit var tvAlreadyHaveAnAct: TextView
    var pressedTwice = false
    lateinit var view: View
    lateinit var roleName: String
    lateinit var idCheckCustomerApi: String
    lateinit var id: String
    lateinit var name: String
    lateinit var email: String
    lateinit var zipcodeCheckCustomerApi: String
    lateinit var image: String
    lateinit var address: String
    lateinit var cityCheckCustomerApi: String
    lateinit var state: String
    lateinit var country: String
    lateinit var discoverLessonStatusCheckCustomerApi: String
    lateinit var progressionModelStatus: String
    lateinit var certificateStatusStatusCheckCustomerApi: String
    lateinit var customerId1: String
    lateinit var role: JsonArray
    lateinit var wavier_accepted: String
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Preference().getSelectedLanguage(this)
        AppUtils().setLocale(Locale(Preference().getSelectedLanguage(this)))
        AppUtils().setConfigChange(this)
        setContentView(R.layout.activity_sign_up)

        firebaseAnalytics = Firebase.analytics


        init()
        onClick()
        object : CountDownTimer(500, 500) {
            override fun onFinish() {}
            override fun onTick(millisUntilFinished: Long) {
                CustomMethods().hideKeyboard(this@SignUpActivity)
            }
        }.start()

    }


    private fun init() {

        btnRegisterNow = findViewById(R.id.btn_register_now)
        btnRegisterNowProfile = findViewById(R.id.btn_register_now_profile)
        tvAlreadyHaveAnAct = findViewById(R.id.tv_already_have_an_act)
        view = findViewById(android.R.id.content)

    }

    private fun onClick() {

        btnRegisterNow.setOnClickListener {
            if (!validation()) {
                //register or signup new user
                getRegister()
            }
        }

        tvAlreadyHaveAnAct.setOnClickListener {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }

        btnRegisterNowProfile.setOnClickListener {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    22
                )
            }

            val mimeTypes = arrayOf("image/*")
            val intent = Intent()
            var mimeTypesStr = ""

            for (mimeType in mimeTypes) {
                mimeTypesStr += "$mimeType|"
            }

            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            intent.type = mimeTypesStr.substring(0, mimeTypesStr.length - 1)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                NewPickImage
            )
        }
    }

    private fun validation(): Boolean {

        when {
            et_your_name.text.toString().isEmpty() -> {
                et_your_name.error = getString(R.string.enterName)
                validate = true
            }
            et_emailRegister.text.toString().isEmpty() -> {
                et_emailRegister.error = getString(R.string.enterEmail)
                validate = true
            }
            et_passwordRegister.text.toString().isEmpty() -> {
                et_passwordRegister.error = getString(R.string.enterPassword)
                validate = true
            }
//            et_zip_code.text.toString().isEmpty() -> {
//                et_zip_code.error = getString(R.string.enterZipCode)
//                validate = true
//            }
        }
        return validate
    }


    private fun getRegister() {
        val dialog: Dialog = CustomLoader().loader(this)
        val api: Api = Connection().getCon(this as Context)

        images = try {

            val requestBody: RequestBody = if (file.name != "") {
                RequestBody.create(MediaType.parse("multipart/form-data"), file)
            } else {
                RequestBody.create(MediaType.parse("multipart/form-data"), "")
            }
            MultipartBody.Part.createFormData("image", file.name, requestBody)

        } catch (e: Exception) {

            val requestBody: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), "")
            MultipartBody.Part.createFormData("image", "", requestBody)
        }

        val etYourNameVar: RequestBody = RequestBody.create(
            MediaType.parse("multipart/form-data"),
            et_your_name.text.toString()
        )
        val etEmailregisterVar: RequestBody = RequestBody.create(
            MediaType.parse("multipart/form-data"),
            et_emailRegister.text.toString()
        )
        val etPasswordregisterVar: RequestBody = RequestBody.create(
            MediaType.parse("multipart/form-data"),
            et_passwordRegister.text.toString()
        )
        val etZipCodeVar: RequestBody = RequestBody.create(
            MediaType.parse("multipart/form-data"),
            et_zip_code.text.toString()
        )

        call = api.getRegister(
            etYourNameVar,
            etEmailregisterVar,
            etPasswordregisterVar,
            images,
            etZipCodeVar
        )
        call.enqueue(object : javax.security.auth.callback.Callback, Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("error", "" + t)
                Log.e("error", "" + call)
                CustomLoader().stopLoader(dialog)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                CustomLoader().stopLoader(dialog)
                Log.e("responsere", "" + response.body())
                try {
                    if (response.isSuccessful) {

                        if (response.body() != null) {
                            val data = response.body()!!.get("data").asJsonObject
                            val user = data!!.get("user").asJsonObject
                            val id = user.get("id").asString

                            val name = user.get("name").asString
                            val email = user.get("email").asString
                            val image = user.get("image").asString
                            val zipcode = user.get("zipcode").asString
                            val token = data.get("token").asString
                            val role = user.get("role").asString
                            val address = user.get("address").asString
                            val city = user.get("city").asString
                            val state = user.get("state").asString
                            val country = user.get("country").asString
                            val certificateStatus = user.get("certificate_status").asString
                            val progressionModelStatus =
                                user.get("progression_model_status").asString
                            val discoverLessonStatus = user.get("discover_lesson_status").asString
                            val customerId = user.get("customer_id").asString
                            val wavier_accepted = user.get("wavier_accepted").asString

                            Preference().setLoginFlag(this@SignUpActivity, false)
                            Preference().setUserToken(this@SignUpActivity, token)
                            Preference().setUserPassword(
                                this@SignUpActivity,
                                et_passwordRegister.text.toString()
                            )

                            Preference().setUserData(
                                this@SignUpActivity,
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

                            if (customerId == "") {
                                createStripeCustomer()
                            }
                            startActivity(
                                Intent(
                                    applicationContext,
                                    MainActivity::class.java
                                ).putExtra("canceled_session", "")
                            )
                            finish()
                        }

                    } else {

                        val jsonObject = JSONObject(response.errorBody()!!.string())
                        val message = jsonObject.getString("message")
                        Toast.makeText(this@SignUpActivity, "" + message, Toast.LENGTH_LONG).show()
                        Log.e("message", "" + message)

                    }
                } catch (e: Exception) {
                    Log.e("Macro", "" + e)
                }

            }
        })
    }

    private fun createStripeCustomer() {
//        val dialog: Dialog = CustomLoader().loader(this)
        val api: Api = Connection().getCon(this)
        call = api.checkCustomerId("Bearer " + Preference().getUserData(this)?.token)
        Log.e("macro", "asd->" + Preference().getUserData(this)?.token)

        call.enqueue(object : javax.security.auth.callback.Callback,
            Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                try {
                    if (response.isSuccessful) {
                        if (response.body() != null) {


                            val data = response.body()!!.get("data").asJsonObject

                            if (data.has("id")) {
                                if (!data.get("id").isJsonNull) {

                                    idCheckCustomerApi = data.get("id").asString
                                }
                            }
                            if (data.has("name")) {
                                if (!data.get("name").isJsonNull) {

                                    name = data.get("name").asString
                                }
                            }
                            if (data.has("email")) {
                                if (!data.get("email").isJsonNull) {

                                    email = data.get("email").asString
                                }
                            }
                            if (data.has("zipcode")) {
                                if (!data.get("zipcode").isJsonNull) {

                                    zipcodeCheckCustomerApi = data.get("zipcode").asString
                                }
                            }
                            if (data.has("image")) {
                                if (!data.get("image").isJsonNull) {

                                    image = data.get("image").asString
                                }
                            }
                            if (data.has("address")) {
                                if (!data.get("address").isJsonNull) {

                                    address = data.get("address").asString
                                }
                            }

                            if (data.has("city")) {
                                if (!data.get("city").isJsonNull) {

                                    cityCheckCustomerApi = data.get("city").asString

                                }
                            }
                            if (data.has("state")) {
                                if (!data.get("state").isJsonNull) {

                                    state = data.get("state").asString
                                }
                            }
                            if (data.has("country")) {
                                if (!data.get("country").isJsonNull) {

                                    country = data.get("country").asString
                                }
                            }

                            if (data.has("discover_lesson_status")) {
                                if (!data.get("discover_lesson_status").isJsonNull) {

                                    discoverLessonStatusCheckCustomerApi =
                                        data.get("discover_lesson_status").asString
                                }
                            }
                            if (data.has("progression_model_status")) {
                                if (!data.get("progression_model_status").isJsonNull) {
                                    progressionModelStatus =
                                        data.get("progression_model_status").asString
                                }
                            }
                            if (data.has("certificate_status")) {
                                if (!data.get("certificate_status").isJsonNull) {
                                    certificateStatusStatusCheckCustomerApi =
                                        data.get("certificate_status").asString

                                }
                            }
                            if (data.has("customer_id")) {
                                if (!data.get("customer_id").isJsonNull) {

                                    customerId1 = data.get("customer_id").asString
                                }
                            }

                            val token = Preference().getUserToken(this@SignUpActivity)
                            if (data.has("roles")) {
                                if (!data.get("roles").isJsonNull) {
                                    role = data.get("roles").asJsonArray

                                }
                            }
                            if (data.has("wavier_accepted")) {
                                if (!data.get("wavier_accepted").isJsonNull) {
                                    wavier_accepted = data.get("wavier_accepted").asString

                                }
                            }

                            for (i in 0 until data.size()) {
                                val jsonObj = role.get(i).asJsonObject
//                                val modelId = jsonObj.get("model_id").asString
                                roleName = jsonObj.get("name").asString

//                                val pivot = role.get(i).asJsonObject
//                                val modelIdp = pivot.get("model_id").asString
//                                val roleId = pivot.get("role_id").asString
//                                val modelType = pivot.get("model_type").asString


                            }

                            Log.e("macro", "inside->")

                            Preference().setLoginFlag(this@SignUpActivity, false)
                            Preference().setUserPassword(
                                this@SignUpActivity,
                                et_passwordRegister.text.toString()
                            )
                            Preference().setUserData(
                                this@SignUpActivity,
                                UserData(
                                    idCheckCustomerApi,
                                    name,
                                    email,
                                    zipcodeCheckCustomerApi,
                                    image,
                                    address,
                                    cityCheckCustomerApi,
                                    state,
                                    country,
                                    discoverLessonStatusCheckCustomerApi,
                                    progressionModelStatus,
                                    certificateStatusStatusCheckCustomerApi,
                                    customerId1,
                                    roleName,
                                    token,
                                    wavier_accepted
                                )
                            )

                        }
                    }
                    if (response.code() == 401) {

                        CustomMethods().logOutUser(this@SignUpActivity)

                    }

                } catch (e: Exception) {
                }
            }
        })
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun getRealPathFromUri(uri: Uri): String? { // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(
                this,
                uri
            )
        ) { // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id)
                )
                return getDataColumn(this, contentUri, "", null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                when (type) {
                    "image" -> {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    }
                    "video" -> {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    }
                    "audio" -> {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(
                    split[1]
                )
                return getDataColumn(this, contentUri!!, selection, selectionArgs)
            }
        } else if ("content".equals(
                uri.scheme,
                ignoreCase = true
            )
        ) { // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                this,
                uri,
                "",
                null
            )
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    private fun getDataColumn(
        context: Context, uri: Uri, selection: String,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
            column
        )
        try {
            cursor = context.contentResolver.query(
                uri, projection, selection, selectionArgs,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    private fun saveProfile(
        uri1: Uri
    ) {
        val filePath: String = getRealPathFromUri(uri1)!!
        file = if (filePath.isNotEmpty()) {
            File(filePath)
        } else {
            File("")
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var uri1: Uri?


        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (requestCode == NewPickImage) {
                try {
                    uri1 = null
                    if (data != null) {
                        uri1 = data.data
                    }
                    var bitmap: Bitmap? = null
                    bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri1)
                    img_profileRegister.setImageBitmap(bitmap)
                    saveProfile(uri1!!)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            Toast.makeText(
                this,
                "" + resources.getString(R.string.permission_denied),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onBackPressed() {
        object : CountDownTimer(2000, 2000) {
            override fun onFinish() {
                pressedTwice = false
            }

            override fun onTick(millisUntilFinished: Long) {

            }
        }.start()
        if (pressedTwice) {
            finish()
        } else {
            Snackbar.make(view, "Click Again to exit", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            pressedTwice = true
        }
    }
}
