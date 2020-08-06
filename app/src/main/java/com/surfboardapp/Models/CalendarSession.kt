package com.surfboardapp.Models

import java.io.Serializable

class CalendarSession(
    var sessionType: String,
    var sessionDate: String,
    var sessionCost: String,
    var sessionLocation: String,
    var sessionId: String,
    var start_time: String
) : Serializable