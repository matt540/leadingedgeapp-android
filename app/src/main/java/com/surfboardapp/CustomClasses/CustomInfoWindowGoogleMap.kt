package com.surfboardapp.CustomClasses

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.Marker
import com.surfboardapp.Models.MapData
import com.surfboardapp.R

class CustomInfoWindowGoogleMap(var context: Context) :
    InfoWindowAdapter {
    private lateinit var tvLocationTitle: TextView
    private lateinit var tvLocationDesc: TextView


    @SuppressLint("InflateParams")
    override fun getInfoWindow(marker: Marker): View? {
        val view: View?
        val map: MapData = marker.tag as MapData
        val textView: TextView


        if (!map.placeName.equals("You are here", true)) {
            view = LayoutInflater.from(context).inflate(R.layout.map_layout, null)
            tvLocationTitle = view!!.findViewById(R.id.tv_location_title)
            tvLocationDesc = view.findViewById(R.id.tv_location_desc)

            tvLocationTitle.text = map.placeName
            tvLocationDesc.text = map.placeDesc
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.map_pin_current, null)
            textView = view!!.findViewById(R.id.place)
            textView.text = map.placeName
        }
        return view
    }

    override fun getInfoContents(marker: Marker): View? {
        return null
    }

}
