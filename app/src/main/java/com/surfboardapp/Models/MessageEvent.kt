package com.surfboardapp.Models

class MessageEvent {
    var mMessage: String = ""
    fun MessageEvent(message: String) {
        mMessage = message
    }

    fun getMessage(): String? {
        return mMessage
    }
}
