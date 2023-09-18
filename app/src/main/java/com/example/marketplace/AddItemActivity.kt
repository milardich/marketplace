package com.example.marketplace

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.marketplace.ui.theme.MarketplaceTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


val database = Firebase.database.reference

class AddItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        val auth = Firebase.auth

        setContent {
            MarketplaceTheme {

                var itemName by remember { mutableStateOf("") }
                var itemDescription by remember { mutableStateOf("") }
                var itemPrice by remember { mutableStateOf("") }
                var itemLocation by remember { mutableStateOf("") }
                var selectedImageUris by remember {
                    mutableStateOf<List<Uri>>(emptyList())
                }
                val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.PickMultipleVisualMedia(),
                    onResult = { uris -> selectedImageUris = uris }
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {

                    OutlinedTextField(
                        value = itemName,
                        onValueChange = { itemName = it },
                        label = { Text("Item name") }
                    )

                    OutlinedTextField(
                        value = itemDescription,
                        onValueChange = { itemDescription = it },
                        label = { Text("Item description") }
                    )

                    // TODO: set location using location service
                    OutlinedTextField(
                        value = itemLocation,
                        onValueChange = { itemLocation = it },
                        label = { Text("Item description") }
                    )

                    OutlinedTextField(
                        value = itemPrice,
                        onValueChange = { itemPrice = it },
                        label = { Text("Item price") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    )

                    Button(onClick = {
                        multiplePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }) {
                        Text(text = "Pick multiple photos")
                    }

                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 128.dp)
                    ) {
                        items(selectedImageUris) { uri ->
                            AsyncImage(
                                model = uri,
                                contentDescription = null,
                                modifier = Modifier.fillMaxWidth(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    Button(onClick = {
                        // TODO: add new item functionality
                        var item = Item()
                        item.name = itemName
                        item.description = itemDescription
                        item.location = itemLocation
                        item.price = itemPrice.toFloat()
                        item.sellerId = auth.currentUser?.uid

                        addNewItem(item)

                    }) {
                        Text(text = "Add new item")
                    }
                }
            }
        }
    }

    fun addNewItem(item: Item?) {
        database.child("items").push().setValue(item)
    }
}