package com.surfboardapp.Models

import java.io.Serializable

class CardListModelMembership(
    var id: String,
    var brand: String,
    var last4: String,
    var isSame: String
) : Serializable