package com.example.marketplace

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.os.HandlerCompat.postDelayed
import coil.compose.AsyncImage
import com.example.marketplace.ui.theme.MarketplaceTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.lang.StringBuilder
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
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    OutlinedTextField(
                        value = itemName,
                        onValueChange = { itemName = it },
                        label = { Text("Item name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = itemDescription,
                        onValueChange = { itemDescription = it },
                        label = { Text("Item description") },
                        modifier = Modifier.fillMaxWidth()

                    )

                    // TODO: set location using location service
                    OutlinedTextField(
                        value = itemLocation,
                        onValueChange = { itemLocation = it },
                        label = { Text("Item location") },
                        modifier = Modifier.fillMaxWidth()

                    )

                    OutlinedTextField(
                        value = itemPrice,
                        onValueChange = { itemPrice = it },
                        label = { Text("Item price") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(onClick = {
                        multiplePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Pick multiple photos")
                    }

                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 100.dp)
                    ) {
                        items(selectedImageUris) { uri ->
                            AsyncImage(
                                model = uri,
                                contentDescription = null,
                                //modifier = Modifier.fillMaxWidth(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }


                    Button(onClick = {
                        // Add item to db
                        var item = Item()
                        val itemUuid = UUID.randomUUID()

                        item.itemUuid = itemUuid.toString()
                        item.name = itemName
                        item.description = itemDescription
                        item.location = itemLocation
                        item.price = itemPrice.toDouble()
                        item.sellerId = auth.currentUser?.uid.toString()

                        var itemUrisStringBuilder = StringBuilder()

                        // create folder on firebase storage with that uuid and store images there
                        Toast.makeText(
                            this@AddItemActivity,
                            "Uploading images...",
                            Toast.LENGTH_SHORT
                        ).show()

                        var index: Int = 0
                        selectedImageUris.forEach { selectedImageUri ->
                            var storageDirectoryPath = itemUuid.toString() + "/" + index
                            var storageDirectoryReference = storage.child(storageDirectoryPath)

                            var uploadTask = storageDirectoryReference.putFile(selectedImageUri).addOnSuccessListener {
                                taskSnapshot ->
                                run {
                                    storageDirectoryReference.downloadUrl.addOnSuccessListener { uri ->
                                        itemUrisStringBuilder.append(uri.toString())
                                        database.child("items").child(itemUuid.toString()).child("images").setValue(itemUrisStringBuilder.append(",").toString())

//                                        Toast.makeText(
//                                            this@AddItemActivity,
//                                            uri.toString(),
//                                            Toast.LENGTH_SHORT
//                                        ).show()

                                    }
                                }
                            }

                            index += 1
                        }

                        addNewItem(itemUuid, item)

                        Handler(Looper.getMainLooper()).postDelayed({
                            openCreatedItem(itemUuid)
                        }, 3000)


                    }, modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Add new item")
                    }
                }
                
                Spacer(modifier = Modifier.padding(50.dp))
                
                NavigationButtons(context = this)
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