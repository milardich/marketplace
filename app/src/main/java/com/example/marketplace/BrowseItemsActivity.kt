package com.example.marketplace

import android.content.Intent
import android.graphics.Paint.Align
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.marketplace.ui.theme.MarketplaceTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class BrowseItemsActivity : AppCompatActivity() {

    val database = Firebase.database.reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse_items)

        setContent {
            MarketplaceTheme {

                var itemList: List<Item> by remember { mutableStateOf(emptyList()) }

                var minimumPrice by remember { mutableStateOf("") }
                var maximumPrice by remember { mutableStateOf("") }
                var test1 by remember { mutableStateOf("") }
                var test2 by remember { mutableStateOf("") }
                // var itemFilter: ItemFilter = ItemFilter(99.99, 112.2)
                var itemFilter by remember { mutableStateOf<ItemFilter>(ItemFilter(0.0, 0.0)) }



                Column {
                    Column (modifier = Modifier.padding(10.dp)){
                        OutlinedTextField(
                            value = minimumPrice,
                            onValueChange = { minimumPrice = it },
                            label = { androidx.compose.material.Text("Minimum price") },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = maximumPrice,
                            onValueChange = { maximumPrice = it },
                            label = { androidx.compose.material.Text("Maximum price") },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Button(onClick = {
                            if(minimumPrice.isEmpty()) {
                                minimumPrice = "0.1"
                            }
                            if(maximumPrice.isEmpty()) {
                                maximumPrice = "0.1"
                            }
                            itemFilter = ItemFilter(minimumPrice.toDouble(), maximumPrice.toDouble())
                        }, modifier = Modifier.fillMaxWidth()) {
                            Text(text = "Filter items")
                        }
                        // Text(text = "${itemFilter.minimumPrice} - ${itemFilter.maximumPrice}")
                    }

                    GetAllItems ({ items -> itemList = items }, itemFilter = itemFilter)

                    if(!itemList.isEmpty()){
                        DisplayItems(itemList)
                    }
                    else {
                        Text(text = "No items posted yet", textAlign = TextAlign.Center)
                    }
                }


                NavigationButtons(context = this)
            }
        }
    }

    @Composable
    fun GetAllItems(onItemsLoaded: (List<Item>) -> Unit, itemFilter: ItemFilter) {
        val itemList: MutableList<Item> = mutableListOf()

        database.child("items").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (itemSnapshot in dataSnapshot.children) {
                    val itemData = itemSnapshot.getValue(Item::class.java)
                    itemData?.let {
                        itemList.add(it)
                    }
                }

//                Toast.makeText(
//                    baseContext,
//                    "Loading items...",
//                    Toast.LENGTH_SHORT,
//                ).show()

                // Notify the caller that items have been loaded
                if(itemFilter.minimumPrice.isNaN() || itemFilter.maximumPrice.isNaN() || itemFilter.minimumPrice <= 0.1 || itemFilter.maximumPrice < itemFilter.minimumPrice) {
                    onItemsLoaded(itemList)
                }
                else {
                    val filteredItemList: MutableList<Item> = mutableListOf()
                    itemList.forEach { item ->
                        if(item.price >= itemFilter.minimumPrice && item.price <= itemFilter.maximumPrice) {
                            filteredItemList.add(item)
                        }
                    }
                    onItemsLoaded(filteredItemList)
                }
//                onItemsLoaded(itemList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DisplayItems(itemList: List<Item>) {

        LazyColumn {
            items(itemList) { item ->

                var mutableImageUris = item.images.split(",").toMutableList()
                mutableImageUris.remove("")
                val imageUris = mutableImageUris.toList()

                Card (
                    modifier = Modifier.padding(10.dp),
                    onClick = {
                        openItem(item.itemUuid)
                    }
                ){
                    Column (
                        modifier = Modifier.padding(15.dp)
                    ){
                        AsyncImage(
                            model = imageUris[0],
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                        )
                        Text(text = item.name, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                        Text(text = "€ " + item.price.toString(), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)

                    }
                }
            }
        }
    }

    fun openItem(itemUuid: String) {
        var openItemIntent: Intent
        openItemIntent = Intent(this, ItemActivity::class.java)
        openItemIntent.putExtra("itemUuid", itemUuid.toString())
        startActivity(openItemIntent)
    }
}