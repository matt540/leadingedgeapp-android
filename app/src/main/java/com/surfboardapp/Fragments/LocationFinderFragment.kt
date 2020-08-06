package com.surfboardapp.Fragments

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.Nullable
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.JsonObject
import com.surfboardapp.Activity.MainActivity
import com.surfboardapp.CustomClasses.CustomInfoWindowGoogleMap
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


class LocationFinderFragment : Fragment(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    com.google.android.gms.location.LocationListener, GoogleMap.OnInfoWindowClickListener {
    private var mMap: GoogleMap? = null
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var mLastLocation: Location
    private lateinit var mCurrLocationMarker: Marker
    private val MY_PERMISSIONS_REQUEST_LOCATION = 99
    private lateinit var mapView: MapView
    private lateinit var address: List<Address>
    private lateinit var etSearchPlace: EditText
    private lateinit var edt_mail: EditText
    private lateinit var btn_request_more_info: Button
    private lateinit var btn_cancel: Button
    private lateinit var spinnerRangeForMap: Spinner
    private var lat: Double = 0.0
    private var long: Double = 0.0
    private var id: String = ""
    private var telephone: String = ""
    private var image: String = ""
    private var name: String = ""
    private var description: String = ""
    private var foils: String = ""
    private var instructor_count: String = ""
    private var favorite_location: String = ""
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var tvScreenTitle: TextView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var builder: Dialog
    lateinit var dialog1: View
    var md: ArrayList<MapData>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { // Inflate the layout for this fragment
        return inflater.inflate(R.layout.location_finder_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setDrawerSelection()
        setBottomSelection()
        onClick()
        getRangFromSpinner()
    }

    private fun setDrawerSelection() {
        drawerLayout = activity!!.findViewById(R.id.drawer_layout)
        val home = drawerLayout.findViewById<LinearLayout>(R.id.nav_location_ll)
        val homeImg = drawerLayout.findViewById<ImageView>(R.id.nav_location_img)
        val hometxt = drawerLayout.findViewById<TextView>(R.id.nav_location_txt)

        home?.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorPrimaryDark))
        homeImg?.setColorFilter(ContextCompat.getColor(context!!, R.color.white))
        hometxt?.setTextColor(ContextCompat.getColor(context!!, R.color.white))
    }

    private fun setBottomSelection() {
        val dashboardImage = activity!!.findViewById<ImageView>(R.id.image_location)
        val dashboardText = activity!!.findViewById<TextView>(R.id.text_location)
        dashboardText?.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        dashboardImage?.setColorFilter(ContextCompat.getColor(context!!, R.color.black))
    }

    private fun init() {
        tvScreenTitle = activity!!.findViewById(R.id.tv_screenTitle)
        tvScreenTitle.text = context!!.resources.getString(R.string.location_finder)
        etSearchPlace = view!!.findViewById(R.id.et_search_place)
        spinnerRangeForMap = view!!.findViewById(R.id.spinner_range_for_map)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)

        activity!!.bottom_navigation.visibility = View.VISIBLE
        activity!!.back.visibility = View.GONE
        activity!!.menu.visibility = View.VISIBLE
        md = ArrayList()
        builder = Dialog(context!!)
        dialog1 = layoutInflater.inflate(R.layout.custom_mail, null)

        edt_mail = dialog1.findViewById(R.id.edt_mail)
        btn_cancel = dialog1.findViewById(R.id.btn_cancel)
        btn_request_more_info = dialog1.findViewById(R.id.btn_request_more_info)

        //for clear marker if edittext is empty or text of edittext is changing

        etSearchPlace.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (etSearchPlace.text.toString() == "") {
                    mMap!!.clear()
                } else {
                    val latLng = LatLng(lat, long)

                    val markerOptions = MarkerOptions()
                    markerOptions.position(latLng)
                    val d: Drawable? =
                        getDrawable(activity!!, R.drawable.locationicon)
                    val bitmap = (d as BitmapDrawable).bitmap
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                    mCurrLocationMarker = mMap!!.addMarker(markerOptions)
                    mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11f))
                    mCurrLocationMarker = mMap!!.addMarker(markerOptions)
                    val mp = MapData(false, "", "You are here", "", "", "", "", "", "")
                    mCurrLocationMarker.tag = mp
                    mCurrLocationMarker.showInfoWindow()
                    mMap!!.setOnMarkerClickListener {
                        val customInfoWindowCurrent =
                            CustomInfoWindowGoogleMap(context!!)
                        mMap!!.setInfoWindowAdapter(customInfoWindowCurrent)
                        false
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

    }

    private fun apiCall() {

        val dialog: Dialog = CustomLoader().loader(context)
        val api: Api = Connection().getCon(context!!)
        api.searchLocation(
            "Bearer " + Preference().getUserData(context!!)?.token,
            lat,
            long,
            spinnerRangeForMap.selectedItem.toString()
        ).enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("error", "" + t)
                CustomLoader().stopLoader(dialog)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                try {
                    Log.e("locationSearch", "" + response.body())
                    CustomLoader().stopLoader(dialog)
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            Log.e("macro", "resp->" + response.body())


                            val data = response.body()!!["data"].asJsonArray
                            if (data.size() > 0) {
                                for (i in 0 until data.size()) {
                                    val first: JsonObject = data.get(i).asJsonObject
                                    val latitude = first.get("latitude").asString
                                    val longitude = first.get("longitude").asString

                                    Preference().setNewLat(context!!, latitude)
                                    Preference().setNewLong(context!!, longitude)
                                    Preference().setRange(
                                        context!!,
                                        spinnerRangeForMap.selectedItem.toString()
                                    )

                                    val markerOptions = MarkerOptions()
                                    val latLng = LatLng(latitude.toDouble(), longitude.toDouble())
                                    markerOptions.position(latLng)
                                    val d: Drawable? =
                                        getDrawable(activity!!, R.drawable.locationicon)
                                    val bitmap = (d as BitmapDrawable).bitmap
                                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap))


                                    val m = mMap!!.addMarker(markerOptions)
                                    mMap!!.setOnMarkerClickListener {
                                        val customInfoWindow =
                                            CustomInfoWindowGoogleMap(context!!)
                                        mMap!!.setInfoWindowAdapter(customInfoWindow)
                                        false
                                    }
                                    if (first.get("id").isJsonNull) {
                                        id = ""
                                    } else {
                                        id = first.get("id").asString
                                    }
                                    if (first.get("name").isJsonNull) {
                                        name = ""
                                    } else {
                                        name = first.get("name").asString
                                    }
                                    if (first.get("description").isJsonNull) {
                                        description = ""
                                    } else {
                                        description = first.get("description").asString
                                    }
                                    if (first.get("instructor_count").isJsonNull) {
                                        instructor_count = ""
                                    } else {
                                        instructor_count = first.get("instructor_count").asString
                                    }
                                    if (first.get("favorite_location").isJsonNull) {
                                        favorite_location = ""
                                    } else {
                                        favorite_location = first.get("favorite_location").asString
                                    }
                                    if (first.get("foils").isJsonNull) {
                                        foils = "0"
                                    } else {
                                        foils = first.get("foils").asString
                                    }
                                    if (first.get("telephone").isJsonNull) {
                                        telephone = ""
                                    } else {
                                        telephone = first.get("telephone").asString
                                    }
                                    if (first.get("image").isJsonNull) {
                                        image = ""
                                    } else {
                                        image = first.get("image").asString
                                    }


                                    val mp = MapData(
                                        false,
                                        id,
                                        name,
                                        description,
                                        foils,
                                        instructor_count,
                                        telephone
                                        , "", image
                                    )
//                                    mMap!!.moveCamera(
//                                        CameraUpdateFactory.newLatLngZoom(
//                                            latLng,
//                                            11f
//                                        )
//                                    )
                                    m.tag = mp
                                    m.showInfoWindow()
                                    md!!.add(mp)


                                    val instructor = first.get("instructor").asJsonArray
                                    if (instructor.size() > 0) {
                                        for (j in 0 until instructor.size()) {
                                            val subObject: JsonObject =
                                                instructor.get(j).asJsonObject
//                                            val id = subObject.get("id").asString
//                                            val name1 = subObject.get("name").asString
//                                            val email = subObject.get("email").asString
//                                            val img = subObject.get("image").asString

                                            val pivot: JsonObject =
                                                subObject.get("pivot").asJsonObject

//                                            val locationId = pivot.get("location_id").asString
//                                            val instructorId =
                                            pivot.get("instructor_id").asString
                                        }
                                    }

                                }

                            }
                        }
                    }
                    if (response.code() == 401) {

                        CustomMethods().logOutUser(context!!)

                    }
                } catch (e: Exception) {
                    Log.e("Macro", "" + e)
                }
            }
        })

        mMap!!.setOnInfoWindowClickListener(this)
    }

    private fun onClick() {
        etSearchPlace.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val geo = searchPlace(etSearchPlace.text.toString())
                etSearchPlace.hideKeyboard()

                try {

                    lat = geo?.lat!!
                    long = geo.long

                } catch (e: java.lang.Exception) {
                }
                mMap!!.clear()
                val latLng = LatLng(lat, long)
                val markerOptions = MarkerOptions()
                markerOptions.position(latLng)
                val d: Drawable? =
                    getDrawable(activity!!, R.drawable.locationicon)
                val bitmap = (d as BitmapDrawable).bitmap
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11f))
                mCurrLocationMarker = mMap!!.addMarker(markerOptions)
                val mp = MapData(false, "", "You are here", "", "", "", "", "", "")
                mCurrLocationMarker.tag = mp
                mCurrLocationMarker.showInfoWindow()
                mMap!!.setOnMarkerClickListener {
                    val customInfoWindowCurrent =
                        CustomInfoWindowGoogleMap(context!!)
                    mMap!!.setInfoWindowAdapter(customInfoWindowCurrent)
                    false
                }
            }
            return@setOnEditorActionListener true
        }


        spinnerRangeForMap.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position != 0) {
                        //search location
                        apiCall()
                    }

                }

            }

    }

    private fun EditText.hideKeyboard() {
        val imm =
            context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.windowToken, 0)
    }

    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mapView = view!!.findViewById(R.id.map)
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                buildGoogleApiClient()
                mMap!!.isMyLocationEnabled = true
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
        } else {
            buildGoogleApiClient()
            mMap!!.isMyLocationEnabled = true
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
                if (location != null) {

                    val latLng = LatLng(location.latitude, location.longitude)
                    val markerOptions = MarkerOptions()
                    markerOptions.position(latLng)
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    mCurrLocationMarker = mMap!!.addMarker(markerOptions)


                    mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11f))
                    mMap!!.animateCamera(CameraUpdateFactory.zoomTo(10f))
                    val mp = MapData(false, "", "You are here", "", "", "", "", "", "")
                    mCurrLocationMarker.tag = mp
                    mCurrLocationMarker.showInfoWindow()
                    mMap!!.setOnMarkerClickListener {
                        val customInfoWindowCurrent =
                            CustomInfoWindowGoogleMap(context!!)
                        mMap!!.setInfoWindowAdapter(customInfoWindowCurrent)
                        false
                    }

                    apiCallCurrent(location.latitude, location.longitude, "100")

                }
            }


    }

    override fun onConnected(p0: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 1000
        mLocationRequest.fastestInterval = 1000
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                mLocationRequest,
                this
            )
        }
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onLocationChanged(location: Location?) {
        mLastLocation = location!!

    }


    override fun onInfoWindowClick(p0: Marker) {
        val map: MapData = p0.tag as MapData
        val dialog: Dialog = CustomLoader().loader(context)
        val api: Api = Connection().getCon(context!!)
        api.bookingStatus(
            "Bearer " + Preference().getUserData(context!!)?.token,
            map.placeId
        ).enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                dialog.dismiss()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                dialog.dismiss()
                try {


                    if (response.isSuccessful) {
                        if (response.body() != null) {

                            Log.e("respanse", "" + response.body())
                            var data = response.body()!!.get("data").asJsonObject
                            var allowed = data.get("allowed").asString



                            if (allowed.equals("true")) {


                                if (!map.placeName.equals("You are here", true)) {

                                    CustomMethods().openPage(
                                        activity!!,
                                        MainActivity().locationFinderInner,
                                        map
                                    )
                                } else {
                                    Toast.makeText(
                                        context,
                                        "You are here!!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            } else {
//                                if (data.has("message")) {
//
//                                    var messagess = data.get("message").asString
//                                    Toast.makeText(context!!, "" + messagess, Toast.LENGTH_LONG)
//                                        .show()
//
//
//                                }
                                builder.setCancelable(false)
                                builder.setContentView(dialog1)
                                builder.show()

                                btn_cancel.setOnClickListener {

                                    builder.dismiss()
                                }
                                btn_request_more_info.setOnClickListener {

                                    if (edt_mail.text.toString().trim().equals("")) {
                                        Toast.makeText(
                                            context,
                                            "Please Enter Mail Address First",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } else {

                                        val api1: Api = Connection().getCon(context!!)
                                        api1.request_info(
                                            "Bearer " + Preference().getUserData(context!!)?.token,
                                            map.placeName, edt_mail.text.toString().trim()
                                        ).enqueue(object : retrofit2.Callback<JsonObject> {
                                            override fun onFailure(
                                                call: Call<JsonObject>,
                                                t: Throwable
                                            ) {

                                                Toast.makeText(
                                                    context,
                                                    "Something Went Wrong",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                builder.dismiss()

                                            }

                                            override fun onResponse(
                                                call: Call<JsonObject>,
                                                response: Response<JsonObject>
                                            ) {


                                                Log.e("r", "" + response.body())
                                                builder.dismiss()
                                                Toast.makeText(
                                                    context,
                                                    "Request Submitted Successfully ",
                                                    Toast.LENGTH_LONG
                                                ).show()


                                            }
                                        })


                                    }

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

        })


    }


    private fun buildGoogleApiClient() {

        mGoogleApiClient = GoogleApiClient.Builder(context!!)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        mGoogleApiClient.connect()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {

                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(
                            context!!,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {


                        buildGoogleApiClient()
                        mMap!!.isMyLocationEnabled = true

                        fusedLocationClient.lastLocation
                            .addOnSuccessListener { location: Location? ->
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {

                                    val latLng = LatLng(location.latitude, location.longitude)
                                    val markerOptions = MarkerOptions()
                                    markerOptions.position(latLng)
                                    markerOptions.icon(
                                        BitmapDescriptorFactory.defaultMarker(
                                            BitmapDescriptorFactory.HUE_RED
                                        )
                                    )
                                    mCurrLocationMarker = mMap!!.addMarker(markerOptions)


                                    mMap!!.moveCamera(
                                        CameraUpdateFactory.newLatLngZoom(
                                            latLng,
                                            11f
                                        )
                                    )
                                    mMap!!.animateCamera(CameraUpdateFactory.zoomTo(10f))
                                    val mp =
                                        MapData(false, "", "You are here", "", "", "", "", "", "")
                                    mCurrLocationMarker.tag = mp
                                    mCurrLocationMarker.showInfoWindow()
                                    mMap!!.setOnMarkerClickListener {
                                        val customInfoWindowCurrent =
                                            CustomInfoWindowGoogleMap(context!!)
                                        mMap!!.setInfoWindowAdapter(customInfoWindowCurrent)
                                        false
                                    }
                                    apiCallCurrent(location.latitude, location.longitude, "100")
                                }
                            }
                    }

                } else {
                    Toast.makeText(context, "permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    private fun apiCallCurrent(latitude: Double, longitude: Double, i: String) {
        val dialog: Dialog = CustomLoader().loader(context)
        val api: Api = Connection().getCon(context!!)
        api.searchLocation(
            "Bearer " + Preference().getUserData(context!!)?.token,
            latitude,
            longitude,
            i
        ).enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("error", "" + t)
                CustomLoader().stopLoader(dialog)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                try {
                    Log.e("locationSearch", "" + response.body())
                    CustomLoader().stopLoader(dialog)
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            Log.e("macro", "resp->" + response.body())


                            val data = response.body()!!["data"].asJsonArray
                            if (data.size() > 0) {
                                for (i in 0 until data.size()) {
                                    val first: JsonObject = data.get(i).asJsonObject
                                    val latitude = first.get("latitude").asString
                                    val longitude = first.get("longitude").asString

                                    Preference().setNewLat(context!!, latitude)
                                    Preference().setNewLong(context!!, longitude)
                                    Preference().setRange(
                                        context!!,
                                        spinnerRangeForMap.selectedItem.toString()
                                    )

                                    val markerOptions = MarkerOptions()
                                    val latLng = LatLng(latitude.toDouble(), longitude.toDouble())
                                    markerOptions.position(latLng)
                                    val d: Drawable? =
                                        getDrawable(activity!!, R.drawable.locationicon)
                                    val bitmap = (d as BitmapDrawable).bitmap
                                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap))


                                    val m = mMap!!.addMarker(markerOptions)
                                    mMap!!.setOnMarkerClickListener {
                                        val customInfoWindow =
                                            CustomInfoWindowGoogleMap(context!!)
                                        mMap!!.setInfoWindowAdapter(customInfoWindow)
                                        false
                                    }
                                    if (first.get("id").isJsonNull) {
                                        id = ""
                                    } else {
                                        id = first.get("id").asString
                                    }
                                    if (first.get("name").isJsonNull) {
                                        name = ""
                                    } else {
                                        name = first.get("name").asString
                                    }
                                    if (first.get("description").isJsonNull) {
                                        description = ""
                                    } else {
                                        description = first.get("description").asString
                                    }
                                    if (first.get("instructor_count").isJsonNull) {
                                        instructor_count = ""
                                    } else {
                                        instructor_count = first.get("instructor_count").asString
                                    }
                                    if (first.get("favorite_location").isJsonNull) {
                                        favorite_location = ""
                                    } else {
                                        favorite_location = first.get("favorite_location").asString
                                    }
                                    if (first.get("foils").isJsonNull) {
                                        foils = "0"
                                    } else {
                                        foils = first.get("foils").asString
                                    }
                                    if (first.get("telephone").isJsonNull) {
                                        telephone = ""
                                    } else {
                                        telephone = first.get("telephone").asString
                                    }
                                    if (first.get("image").isJsonNull) {
                                        image = ""
                                    } else {
                                        image = first.get("image").asString
                                    }


                                    val mp = MapData(
                                        false,
                                        id,
                                        name,
                                        description,
                                        foils,
                                        instructor_count,
                                        telephone
                                        , "",
                                        image
                                    )
//                                    mMap!!.moveCamera(
//                                        CameraUpdateFactory.newLatLngZoom(
//                                            latLng,
//                                            11f
//                                        )
//                                    )
                                    m.tag = mp
                                    m.showInfoWindow()
                                    md!!.add(mp)


                                    val instructor = first.get("instructor").asJsonArray
                                    if (instructor.size() > 0) {
                                        for (j in 0 until instructor.size()) {
                                            val subObject: JsonObject =
                                                instructor.get(j).asJsonObject
//                                            val id = subObject.get("id").asString
//                                            val name1 = subObject.get("name").asString
//                                            val email = subObject.get("email").asString
//                                            val img = subObject.get("image").asString

                                            val pivot: JsonObject =
                                                subObject.get("pivot").asJsonObject

//                                            val locationId = pivot.get("location_id").asString
//                                            val instructorId =
                                            pivot.get("instructor_id").asString
                                        }
                                    }

                                }

                            }
                        }
                    }
                    if (response.code() == 401) {

                        CustomMethods().logOutUser(context!!)

                    }
                } catch (e: Exception) {
                    Log.e("Macro", "" + e)
                }
            }
        })

        mMap!!.setOnInfoWindowClickListener(this)

    }

    private fun searchPlace(strAdd: String): GeoPoint? {
        try {

            val coder = Geocoder(activity!!.applicationContext)
            address = coder.getFromLocationName(strAdd, 5)
            val location: Address = address[0]
            location.latitude
            location.longitude

            return GeoPoint(
                location.latitude,
                location.longitude
            )


        } catch (e: Exception) {
            Log.e("macro", "" + e)

            return null
        }
    }

    class GeoPoint(
        val lat: Double, val long: Double
    )


    private fun getRangFromSpinner() {
        ArrayAdapter.createFromResource(
            context!!,
            R.array.range_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerRangeForMap.adapter = adapter
        }
    }

}