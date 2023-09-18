package com.example.marketplace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.marketplace.ui.theme.MarketplaceTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        val database = Firebase.database.reference
        val auth = Firebase.auth
        val storage = Firebase.storage.reference

        val openItemIntent: Intent = intent
        val itemUuid: String? = openItemIntent.getStringExtra("itemUuid")

        setContent {
            MarketplaceTheme {
                
                Button(onClick = {
                    displayItemData(
                        itemUuid = "d5d23cb1-d4a7-41af-b9d7-b7ffc3c2cf9c",
                        database = database
                    )
                }) {
                    Text(text = "Display item")
                }

            }
        }
    }


    fun displayItemData(itemUuid: String, database: DatabaseReference) {
        var item: Item = Item()

        database.child("items").child(itemUuid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val itemData = snapshot.getValue(Item::class.java)

                    // Now you have the itemData object containing the fetched data
                    if (itemData != null) {
                        // Do something with the data
                        item.name = itemData.name
                        item.description = itemData.description
                        item.location = itemData.location
                        item.price = itemData.price
                        item.sellerId = itemData.sellerId

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if data fetch fails
            }
        })

        Log.i("firebase", "Got value ${item.toString()}")
        // Display data
//        Column {
//            item.name?.let { Text(text = it, color = Color.Red) }
//            item.description?.let { Text(text = it) }
//            item.location?.let { Text(text = it) }
//            item.price?.let { Text(text = it.toString()) }
//            item.sellerId?.let { Text(text = it) }
//        }
    }
}