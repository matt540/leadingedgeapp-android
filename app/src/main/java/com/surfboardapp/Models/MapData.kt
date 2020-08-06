package com.surfboardapp.Models

import java.io.Serializable

class MapData(
    val isRechedule: Boolean,
    val placeId: String,
    var placeName: String,
    val placeDesc: String,
    val foils: String,
    val instructor_count: String,
    val telephone: String,
    val response: String,
    val image: String
) : Serializable