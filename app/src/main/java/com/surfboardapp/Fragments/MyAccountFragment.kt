package com.surfboardapp.Fragments

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
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import com.surfboardapp.Activity.MainActivity
import com.surfboardapp.Adapter.CardListAdapter
import com.surfboardapp.CustomClasses.CustomLoader
import com.surfboardapp.CustomClasses.CustomMethods
import com.surfboardapp.Models.CardListModel
import com.surfboardapp.Models.UserData
import com.surfboardapp.Preference.Preference
import com.surfboardapp.R
import com.surfboardapp.Retrofit.Api
import com.surfboardapp.Retrofit.Connection
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.io.File
import javax.security.auth.callback.Callback

class MyAccountFragment : Fragment() {

    private lateinit var tvScreenTitle: TextView
    private lateinit var imgProfileEdit: ImageView
    private lateinit var btnEditProfile: Button
    private lateinit var etMyactName: EditText
    private lateinit var etMyactEmail: EditText
    private lateinit var etAddress: EditText
    private lateinit var etCity: EditText
    private lateinit var spState: Spinner
    private lateinit var spinner_country: Spinner
    private var state_code: String = ""
    private var set_country: String = ""
    private var set_state_code: String = ""
    private var set_state_name: String = ""
    private lateinit var etZipCode: EditText
    private lateinit var btnResetPwd: Button
    private lateinit var btnEdit: Button
    private lateinit var rvCardDetails: RecyclerView
    private lateinit var images: MultipartBody.Part
    lateinit var adapter: CardListAdapter
    lateinit var cardList: ArrayList<CardListModel>
    lateinit var state_list_array: ArrayList<kotlin.String>
    private val NewPickImage = 1
    private lateinit var file: File
    private val states: JSONArray = JSONArray(
        "[     {\"label\": \"Alabama\", \"value\": \"AL\"},\n" +
                "      {\"label\": \"Alaska\", \"value\": \"AK\"},\n" +
                "      {\"label\": \"Arizona\", \"value\": \"AZ\"},\n" +
                "      {\"label\": \"Arkansas\", \"value\": \"AR\"},\n" +
                "      {\"label\": \"California\", \"value\": \"CA\"},\n" +
                "      {\"label\": \"Colorado\", \"value\": \"CO\"},\n" +
                "      {\"label\": \"Connecticut\", \"value\": \"CT\"},\n" +
                "      {\"label\": \"Delaware\", \"value\": \"DE\"},\n" +
                "      {\"label\": \"District Of Columbia\", \"value\": \"DC\"},\n" +
                "      {\"label\": \"Florida\", \"value\": \"FL\"},\n" +
                "      {\"label\": \"Georgia\", \"value\": \"GA\"},\n" +
                "      {\"label\": \"Hawaii\", \"value\": \"HI\"},\n" +
                "      {\"label\": \"Idaho\", \"value\": \"ID\"},\n" +
                "      {\"label\": \"Illinois\", \"value\": \"IL\"},\n" +
                "      {\"label\": \"Indiana\", \"value\": \"IN\"},\n" +
                "      {\"label\": \"Iowa\", \"value\": \"IA\"},\n" +
                "      {\"label\": \"Kansas\", \"value\": \"KS\"},\n" +
                "      {\"label\": \"Kentucky\", \"value\": \"KY\"},\n" +
                "      {\"label\": \"Louisiana\", \"value\": \"LA\"},\n" +
                "      {\"label\": \"Maine\", \"value\": \"ME\"},\n" +
                "      {\"label\": \"Maryland\", \"value\": \"MD\"},\n" +
                "      {\"label\": \"Massachusetts\", \"value\": \"MA\"},\n" +
                "      {\"label\": \"Michigan\", \"value\": \"MI\"},\n" +
                "      {\"label\": \"Minnesota\", \"value\": \"MN\"},\n" +
                "      {\"label\": \"Mississippi\", \"value\": \"MS\"},\n" +
                "      {\"label\": \"Missouri\", \"value\": \"MO\"},\n" +
                "      {\"label\": \"Montana\", \"value\": \"MT\"},\n" +
                "      {\"label\": \"Nebraska\", \"value\": \"NE\"},\n" +
                "      {\"label\": \"Nevada\", \"value\": \"NV\"},\n" +
                "      {\"label\": \"New Hampshire\", \"value\": \"NH\"},\n" +
                "      {\"label\": \"New Jersey\", \"value\": \"NJ\"},\n" +
                "      {\"label\": \"New Mexico\", \"value\": \"NM\"},\n" +
                "      {\"label\": \"New York\", \"value\": \"NY\"},\n" +
                "      {\"label\": \"North Carolina\", \"value\": \"NC\"},\n" +
                "      {\"label\": \"North Dakota\", \"value\": \"ND\"},\n" +
                "      {\"label\": \"Ohio\", \"value\": \"OH\"},\n" +
                "      {\"label\": \"Oklahoma\", \"value\": \"OK\"},\n" +
                "      {\"label\": \"Oregon\", \"value\": \"OR\"},\n" +
                "      {\"label\": \"Pennsylvania\", \"value\": \"PA\"},\n" +
                "      {\"label\": \"Rhode Island\", \"value\": \"RI\"},\n" +
                "      {\"label\": \"South Carolina\", \"value\": \"SC\"},\n" +
                "      {\"label\": \"South Dakota\", \"value\": \"SD\"},\n" +
                "      {\"label\": \"Tennessee\", \"value\": \"TN\"},\n" +
                "      {\"label\": \"Texas\", \"value\": \"TX\"},\n" +
                "      {\"label\": \"Utah\", \"value\": \"UT\"},\n" +
                "      {\"label\": \"Vermont\", \"value\": \"VT\"},\n" +
                "      {\"label\": \"Virginia\", \"value\": \"VA\"},\n" +
                "      {\"label\": \"Washington\", \"value\": \"WA\"},\n" +
                "      {\"label\": \"West Virginia\", \"value\": \"WV\"},\n" +
                "      {\"label\": \"Wisconsin\", \"value\": \"WI\"},\n" +
                "      {\"label\": \"Wyoming\", \"value\": \"WY\"}]\"\""
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_account_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        clicks()
        setData()
        loadData()
        getCardList()
        setBottomSelection()
    }

    private fun setBottomSelection() {

        val dashboardImage = activity!!.findViewById<ImageView>(R.id.image_myAccount)
        val dashboardText = activity!!.findViewById<TextView>(R.id.text_myAccount)
        dashboardText?.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        dashboardImage?.setColorFilter(ContextCompat.getColor(context!!, R.color.black))
    }


    private fun loadData() {

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
                                val image = user.get("image").asString
                                val zipcode = user.get("zipcode").asString
                                val token = Preference().getUserToken(context!!)
                                val role = user.get("role").asString
                                val address = user.get("address").asString
                                val city = user.get("city").asString
                                val state = user.get("state").asString
                                Log.e("state", "" + state)
                                val country = user.get("country").asString
                                Log.e("country", "" + country)
                                val certificateStatus = user.get("certificate_status").asString
                                val progressionModelStatus =
                                    user.get("progression_model_status").asString
                                val discoverLessonStatus =
                                    user.get("discover_lesson_status").asString
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
                                setData()
                            }
                        } else {
                            val jsonObject = JSONObject(response.errorBody()!!.string())
                            val message = jsonObject.getString("message")
                            Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        Log.e("Macro", "" + e)
                    }
                }
            })
    }

    private fun setData() {
        etMyactName.setText(Preference().getUserData(context!!)!!.name)
        etMyactEmail.setText(Preference().getUserData(context!!)!!.email)
        etAddress.setText(Preference().getUserData(context!!)!!.address)
        etCity.setText(Preference().getUserData(context!!)!!.city)
        etZipCode.setText(Preference().getUserData(context!!)!!.zipcode)

        if (Preference().getUserData(context!!)!!.image == "") {
            Picasso.get().load(R.drawable.account).placeholder(
                R.drawable.account
            )
                .into(imgProfileEdit)
        } else {
            Picasso.get().load(Preference().getUserData(context!!)!!.image)
                .placeholder(R.drawable.account).into(imgProfileEdit)
        }

        val jsonFile =
            context!!.resources.assets.open("countrieslist.json").bufferedReader()
                .use { it.readText() }


        var jobject: JSONArray = JSONArray(jsonFile)

        var country_list_array = ArrayList<kotlin.String>()
        Log.e("PreferenceC", "" + Preference().getUserData(context!!)!!.country)
        for (i in 0 until jobject.length()) {

            var array_obj = jobject.getJSONObject(i)
            var country_name = array_obj.getString("name")
            country_list_array.add(country_name)



            for (j in 0 until country_list_array.size) {

                if (country_list_array.contains(Preference().getUserData(context!!)!!.country)) {
                    set_country = Preference().getUserData(context!!)!!.country

                    state_list_array = ArrayList<kotlin.String>()

                    var state_list = jobject.getJSONObject(i).getJSONArray("states")

                    if (state_list.length() > 0) {

                        for (k in 0 until state_list.length()) {
                            state_list_array.add(state_list.getJSONObject(k).getString("code").toString())

                            if ((country_list_array.contains(Preference().getUserData(context!!)!!.country)) &&
                                (state_list_array.contains(Preference().getUserData(context!!)!!.state))
                            ) {

                                set_state_code = Preference().getUserData(context!!)!!.state
                                set_state_name =
                                    state_list.getJSONObject(k).getString("name").toString()
                                Log.e("asdasd", "" + state_list_array.indexOf(set_state_code))
                                spState.setSelection(state_list_array.indexOf(set_state_code))
                                break
                            }

                        }
                    } else {
                        state_list_array.add("")
                    }
                    break
                }
            }
        }
        Log.e("set_country", "" + set_country)
        Log.e("set_state_code", "" + set_state_code)

        spinner_country.setSelection(country_list_array.indexOf(set_country))

    }

    private fun clicks() {

        btnResetPwd.setOnClickListener {
            CustomMethods().openPage(
                activity!!,
                MainActivity().resetPassword
            )
            (activity as MainActivity).clearSelection()
        }
        btnEdit.setOnClickListener {
            //update user or edit user details
            updateUserApi()
        }
        btnEditProfile.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity!!,
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

    private fun uploadImage() {
        val dialog: Dialog = CustomLoader().loader(context)
        val api = Connection().getCon(context!!)
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
        api.uploadImage(
            "Bearer " + Preference().getUserData(context!!)!!.token,
            images
        ).enqueue(object : Callback, retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("macro", "" + t.toString())

                CustomLoader().stopLoader(dialog)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                CustomLoader().stopLoader(dialog)
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        Log.e("Macro", "upload->" + response.body())
                        val data = response.body()!!.get("data").asJsonObject

                        val id = data.get("id").asString
                        val name = data.get("name").asString
                        val email = data.get("email").asString
                        val image = data.get("image").asString
                        val zipcode = data.get("zipcode").asString
                        val token = Preference().getUserToken(context!!)
                        val role = Preference().getUserData(context!!)!!.role
                        val address = data.get("address").asString
                        val city = data.get("city").asString
                        val state = data.get("state").asString
                        val country = data.get("country").asString
                        val certificateStatus = data.get("certificate_status").asString
                        val progressionModelStatus =
                            data.get("progression_model_status").asString
                        val discoverLessonStatus =
                            data.get("discover_lesson_status").asString
                        val customerId = data.get("customer_id").asString
                        val wavier_accepted = data.get("wavier_accepted").asString
//                        val wavier_accepted = data.get("wavier_accepted").asString
//                        val gender = data.get("gender").asString
//                        val age = data.get("age").asString
//                        val weight = data.get("weight").asString
//                        val previous_experience = data.get("previous_experience").asString
//                        val experience_description =
//                            data.get("experience_description").asString

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
                                discoverLessonStatus,
                                customerId,
                                wavier_accepted
                            )
                        )
                        setData()
                        Toast.makeText(
                            context,
                            "Your profile is successfully edited.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        })

    }

    @SuppressLint("ObsoleteSdkInt")
    private fun getRealPathFromUri(uri: Uri): String? { // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(
                context,
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
                return getDataColumn(context!!, contentUri, "", null)
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
                return getDataColumn(context!!, contentUri!!, selection, selectionArgs)
            }
        } else if ("content".equals(
                uri.scheme,
                ignoreCase = true
            )
        ) { // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                context!!,
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

        if (requestCode == NewPickImage) {
            try {
                uri1 = null
                if (data != null) {
                    uri1 = data.data
                }
                var bitmap: Bitmap? = null
                bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver, uri1)
                imgProfileEdit.setImageBitmap(bitmap)
                saveProfile(uri1!!)

                //upload profile image
                uploadImage()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun updateUserApi() {
        val dialog: Dialog = CustomLoader().loader(context)
        val api = Connection().getCon(context!!)
        api.updateUser(
            "Bearer " + Preference().getUserData(context!!)?.token,
            etMyactName.text.toString(),
            etMyactEmail.text.toString(),
            etZipCode.text.toString(),
            etAddress.text.toString(),
            etCity.text.toString(),
            "" + state_code,
            "" + spinner_country.selectedItem.toString()

        )
            .enqueue(object : Callback,
                retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    CustomLoader().stopLoader(dialog)
                    Log.e("error", "" + t)
                }

                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    try {
                        Log.e("state_code", "" + state_code)
                        CustomLoader().stopLoader(dialog)
                        if (response.isSuccessful) {
                            if (response.body() != null) {

                                Log.e("Macro", "update->" + response.body())
                                val data = response.body()!!.get("data").asJsonObject

                                val id = data.get("id").asString
                                val name = data.get("name").asString
                                val email = data.get("email").asString
                                val image = data.get("image").asString
                                val zipcode = data.get("zipcode").asString
                                val token = Preference().getUserToken(context!!)
                                val role = Preference().getUserData(context!!)!!.role
                                val address = data.get("address").asString
                                val city = data.get("city").asString
                                val state = data.get("state").asString
                                val country = data.get("country").asString
                                val certificateStatus = data.get("certificate_status").asString
                                val progressionModelStatus =
                                    data.get("progression_model_status").asString
                                val discoverLessonStatus =
                                    data.get("discover_lesson_status").asString
                                val customerId = data.get("customer_id").asString
                                val wavier_accepted = data.get("wavier_accepted").asString
//                                val wavier_accepted = data.get("wavier_accepted").asString
//                                val gender = data.get("gender").asString
//                                val age = data.get("age").asString
//                                val weight = data.get("weight").asString
//                                val previous_experience = data.get("previous_experience").asString
//                                val experience_description =
//                                    data.get("experience_description").asString

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
                                        discoverLessonStatus,
                                        customerId,
                                        wavier_accepted
                                    )
                                )
                                setData()
                                Toast.makeText(
                                    context,
                                    "Your details are successfully edited.",
                                    Toast.LENGTH_LONG
                                ).show()
                                CustomMethods().hideKeyboard(activity!!)
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


    private fun init(view: View) {

        tvScreenTitle = activity!!.findViewById(R.id.tv_screenTitle)
        tvScreenTitle.text = context!!.resources.getString(R.string.myAccount)
        activity!!.bottom_navigation.visibility = View.VISIBLE
        activity!!.back.visibility = View.GONE
        activity!!.menu.visibility = View.VISIBLE
        imgProfileEdit = view.findViewById(R.id.img_profileEdit)
        btnEditProfile = view.findViewById(R.id.btn_edit_profile)
        etMyactName = view.findViewById(R.id.et_myAct_name)
        etMyactEmail = view.findViewById(R.id.et_myAct_email)
        etAddress = view.findViewById(R.id.et_address)
        etCity = view.findViewById(R.id.et_city)
        spState = view.findViewById(R.id.sp_state)
        spinner_country = view.findViewById(R.id.spinner_country)
        etZipCode = view.findViewById(R.id.et_zip_code)
        btnResetPwd = view.findViewById(R.id.btn_reset_pwd)
        btnEdit = view.findViewById(R.id.btn_edit)
        rvCardDetails = view.findViewById(R.id.rv_cardDetails)
        cardList = ArrayList()

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
        spinner_country.adapter = arrayAdapter


        spinner_country.onItemSelectedListener =
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
                    spState.adapter = arrayAdapter1

                }

            }
        spState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                var state_list = jobject.getJSONObject(spinner_country.selectedItemPosition)
                    .getJSONArray("states")
                if (state_list.length() > 0) {
                    state_code =
                        state_list.getJSONObject(spState.selectedItemPosition).getString("code")
                } else {
                    Toast.makeText(context, "No State Available", Toast.LENGTH_LONG).show()
                }


            }
        }

    }

    private fun getCardList() {
        val dialog: Dialog = CustomLoader().loader(context)
        val api: Api = Connection().getConApi(context!!)
        //list of card[stripe]
        api.listCard(
            Preference().getUserData(context!!)!!.customer_id,
            "Bearer " + Preference().getStringStripeKey(context!!)

        ).enqueue(object : Callback, retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                CustomLoader().stopLoader(dialog)
            }

            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
                CustomLoader().stopLoader(dialog)
                try {

                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            Log.e("responsecard", "" + response.body())
                            CustomLoader().stopLoader(dialog)
                            val data = response.body()!!.get("data").asJsonArray
                            if (data.size() > 0) {

                                cardList.clear()
                                for (i in 0 until data.size()) {


                                    val jobj: JsonObject = data.get(i).asJsonObject
                                    val id = jobj.get("id").asString
                                    val brand = jobj.get("brand").asString
                                    val last4 = jobj.get("last4").asString
                                    Log.e("macro", "id--->$id")

                                    val cardmodel = CardListModel(id, brand, last4)

                                    var isIn = false

                                    for (j in cardList) {

                                        if (j.brand == cardmodel.brand && j.last4 == cardmodel.last4) {

                                            isIn = true
                                        }
                                    }
                                    if (!isIn) {
                                        cardList.add(cardmodel)
                                    }

                                }

                                adapter = CardListAdapter(
                                    context as MainActivity,
                                    cardList
                                )
                                rvCardDetails.adapter = adapter
                                rvCardDetails.layoutManager =
                                    LinearLayoutManager(context)

                                adapter.notifyDataSetChanged()

                            }
                        }
                    }
                } catch (e: Exception) {
//                    Toast.makeText(context!!, "" + e, Toast.LENGTH_LONG).show()
                }

            }

        })
    }

}
