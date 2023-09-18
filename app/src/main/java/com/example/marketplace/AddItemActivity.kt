package com.example.marketplace

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.marketplace.ui.theme.MarketplaceTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.util.UUID


val database = Firebase.database.reference

class AddItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        val auth = Firebase.auth
        val storage = Firebase.storage.reference


        setContent {
            MarketplaceTheme {
                val context = LocalContext.current
                val bitmap = remember { mutableStateOf<Bitmap?>(null) }

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
                        label = { Text("Item location") }
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
                        // Add item to db
                        var item = Item()
                        val itemUuid = UUID.randomUUID()

                        item.name = itemName
                        item.description = itemDescription
                        item.location = itemLocation
                        item.price = itemPrice.toDouble()
                        item.sellerId = auth.currentUser?.uid.toString()

                        addNewItem(itemUuid, item)

                        // create folder on firebase storage with that uuid and store images there
                        var index: Int = 0
                        selectedImageUris.forEach { selectedImageUri ->
                            var storageDirectoryPath = itemUuid.toString() + "/" + index
                            var storageDirectoryReference = storage.child(storageDirectoryPath)

                            val source = ImageDecoder.createSource(context.contentResolver, selectedImageUri)
                            bitmap.value = ImageDecoder.decodeBitmap(source)
                            val baos = ByteArrayOutputStream()
                            bitmap.value!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                            val data = baos.toByteArray()
                            var uploadTask = storageDirectoryReference.putBytes(data)
                            index += 1
                        }

                        openCreatedItem(itemUuid)

                    }) {
                        Text(text = "Add new item")
                    }
                }
            }
        }
    }

    fun addNewItem(itemUuid: UUID, item: Item?) {
        database.child("items").child(itemUuid.toString()).setValue(item)
    }

    fun openCreatedItem(itemUuid: UUID) {
        var openItemIntent: Intent
        openItemIntent = Intent(this, ItemActivity::class.java)
        openItemIntent.putExtra("itemUuid", itemUuid.toString())
        startActivity(openItemIntent)
    }
}