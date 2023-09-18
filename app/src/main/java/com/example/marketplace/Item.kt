package com.example.marketplace

import android.net.Uri

data class Item(
    var name: String? = null,
    var description: String? = null,
    var location: String? = null,
    var price: Float? = null,
    //var images: List<String>? = null,
    var sellerId: String? = null
)
