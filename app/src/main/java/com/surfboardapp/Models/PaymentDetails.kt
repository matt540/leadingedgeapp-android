package com.surfboardapp.Models

import java.io.Serializable

class PaymentDetails(
    val type: String,
    val cost: String,
    val date: String,
    val timeSlotId: String,
    val id: String,
    val description: String,
    val isFromProgression: String,
    val session_id: String,
    val start_time: String,
    val end_time: String,
    val instructor: String?,
    val board: String

) : Serializable