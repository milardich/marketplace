package com.example.marketplace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.marketplace.ui.theme.MarketplaceTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ItemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)


        var item = Item()
        item.name = "-"
        item.price = 0.0
        item.location = "-"
        item.description = "-"
        item.sellerId = "-"

        val database = Firebase.database.reference
        val auth = Firebase.auth
        val storage = Firebase.storage.reference

        val openItemIntent: Intent = intent
        var itemUuid: String? = openItemIntent.getStringExtra("itemUuid")

        var showItem: Boolean = false

        setContent {
            MarketplaceTheme {
                Column {
                    
                    ItemImagesScreen(itemUuid = itemUuid.toString(), database = database)

                    BasicTextField(
                        value = "Item uuid: " + itemUuid.toString(),
                        onValueChange = { itemUuid = it }
                    )

                    ItemDetailScreen(itemUuid = itemUuid.toString(), database = database)
                }
            }
        }
    }

    @Composable
    fun ItemDetailScreen(itemUuid: String, database: DatabaseReference) {
        var item by remember { mutableStateOf<Item?>(null) }

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val itemData = snapshot.getValue(Item::class.java)
                    item = itemData
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if data fetch fails
            }
        }

        DisposableEffect(itemUuid) {
            val query = database.child("items").child(itemUuid)
            query.addListenerForSingleValueEvent(listener)

            onDispose {
                // Remove the listener when the composable is disposed
                query.removeEventListener(listener)
            }
        }

        if (item != null) {
            DisplayItemData(item = item!!)
        } else {
            // Display loading or error state here if needed
        }
    }


    @Composable
    fun DisplayItemData(item: Item) {
        Column {
            Text(text = "Name: ${item.name}")
            Text(text = "Description: ${item.description}")
            Text(text = "Location: ${item.location}")
            Text(text = "Price: ${item.price}")
            Text(text = "Seller ID: ${item.sellerId}")
        }
    }

    // images shit
    @Composable
    fun ItemImagesScreen(itemUuid: String, database: DatabaseReference) {
        var item by remember { mutableStateOf<Item?>(null) }

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val itemData = snapshot.getValue(Item::class.java)
                    item = itemData
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if data fetch fails
            }
        }

        DisposableEffect(itemUuid) {
            val query = database.child("items").child(itemUuid)
            query.addListenerForSingleValueEvent(listener)

            onDispose {
                // Remove the listener when the composable is disposed
                query.removeEventListener(listener)
            }
        }

        if (item != null) {
            DisplayItemImages(item = item!!)
        } else {
            // Display loading or error state here if needed
        }
    }

    @Composable
    fun DisplayItemImages(item: Item) {
        // TODO
        val imageUris = listOf<String>()
        var test = ""
        Column() {
//            val images = listOf("https://media.npr.org/assets/img/2021/08/11/gettyimages-1279899488_wide-f3860ceb0ef19643c335cb34df3fa1de166e2761-s1100-c50.jpg",
//                "https://cdn.pixabay.com/photo/2017/02/20/18/03/cat-2083492__480.jpg",
//                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRrfPnodZbEjtJgE-67C-0W9pPXK8G9Ai6_Rw&usqp=CAU",
//                "https://i.ytimg.com/vi/E9iP8jdtYZ0/maxresdefault.jpg",
//                "https://cdn.shopify.com/s/files/1/0535/2738/0144/articles/shutterstock_149121098_360x.jpg")

            ImageSlider(imageUris)
        }
    }

    @Composable
    fun ImageSlider(imageUris: List<Any>) {
        var currentImageIndex by remember { mutableStateOf(0) }
        var isAnimating by remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()

        Column(modifier = Modifier.fillMaxSize()) {

            Box(modifier = Modifier
                .weight(1f)
                .height(100.dp)
                .fillMaxWidth()
                .padding(16.dp)) {
                // Scrollable Row of Cards
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(imageUris) { index, image ->
                        Card(
                            modifier = Modifier
                                .width(300.dp)
                                .height(200.dp)
                                .clickable {
                                    if (index != currentImageIndex && !isAnimating) {
                                        isAnimating = true
                                        coroutineScope.launch {
                                            val delayMillis = 500L
                                            // Wait for the card to change color before animating
                                            delay(delayMillis / 2)
                                            currentImageIndex = index
                                            delay(delayMillis)
                                            isAnimating = false
                                        }
                                    }
                                },
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            AsyncImage(
                                model = image,
                                contentDescription = null,
                            )
                        }
                    }
                }
            }
        }
    }

}