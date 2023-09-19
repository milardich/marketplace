package com.example.marketplace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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

                GetAllItems { items ->
                    itemList = items
                }

                // Display the items using DisplayItems Composable
                DisplayItems(itemList)
            }
        }
    }

    @Composable
    fun GetAllItems(onItemsLoaded: (List<Item>) -> Unit) {
        val itemList: MutableList<Item> = mutableListOf()

        database.child("items").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (itemSnapshot in dataSnapshot.children) {
                    val itemData = itemSnapshot.getValue(Item::class.java)
                    itemData?.let {
                        itemList.add(it)
                    }
                }

                // Notify the caller that items have been loaded
                onItemsLoaded(itemList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DisplayItems(itemList: List<Item>) {
        // You can loop through the itemList and display each item
        LazyColumn {
            items(itemList) { item ->
                // Display item data here
//                Text(text = "Name: ${item.name}")
//                Text(text = "Description: ${item.description}")
//                Text(text = "Price: ${item.price}")
                // Add more Composables to display other item details
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
                            modifier = Modifier.fillMaxWidth().height(180.dp)
                        )
                        Text(text = item.name, modifier = Modifier.fillMaxWidth())
                        Text(text = item.price.toString(), modifier = Modifier.fillMaxWidth())
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