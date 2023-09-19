package com.example.marketplace

import android.net.Uri
import com.google.firebase.database.PropertyName

data class Item(
    @PropertyName("itemUuid")
    var itemUuid: String = "",

    @PropertyName("name")
    var name: String = "",

    @PropertyName("description")
    var description: String = "",

    @PropertyName("location")
    var location: String = "",

    @PropertyName("price")
    var price: Double = 0.0,

    @PropertyName("sellerId")
    var sellerId: String = "",

    @PropertyName("images")
    var images: String = ""
)
