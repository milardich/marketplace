package com.example.marketplace

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.BuildCompat
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.modernstorage.photopicker.PhotoPicker

var storage = Firebase.storage

@Composable
fun ItemsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "Items screen",
            fontWeight = FontWeight.Bold,
            color = Color.Red,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 25.sp
        )
    }
}


@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class, BuildCompat.PrereleaseSdkCheck::class)
@Composable
fun AddItemScreen() {
    var itemName by rememberSaveable { mutableStateOf("") }
    var itemDescription by rememberSaveable { mutableStateOf("") }
    var itemPrice by rememberSaveable { mutableStateOf("") }
    var itemLocation by rememberSaveable { mutableStateOf("") }
    var locations = listOf("Osijek", "Vinkovci", "Vukovar", "Zagreb", "Split", "Dubrovnik", "Pula")
    var locationsExpanded by rememberSaveable { mutableStateOf(false) }
    var selectedLocation by rememberSaveable { mutableStateOf("") }
    var selectedImages by rememberSaveable { mutableStateOf(listOf<Uri>()) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            //.wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "Add new item",
            fontWeight = FontWeight.Bold,
            color = Color.Red,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 25.sp
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = itemName,
            onValueChange = { itemName = it},
            label = { Text("Item name") },
            textStyle = TextStyle(color = Color.Gray)
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = itemDescription,
            onValueChange = { itemDescription = it },
            label = { Text("Description") },
            textStyle = TextStyle(color = Color.Gray)
        )

        // TODO: add dropdown and use phone location
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = itemLocation,
            onValueChange = { itemLocation = it },
            label = { Text("Location") },
            textStyle = TextStyle(color = Color.Gray, fontWeight = FontWeight.Bold),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )


        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = itemPrice,
            onValueChange = { itemPrice = it },
            label = { Text("Price (€)") },
            textStyle = TextStyle(color = Color.Gray, fontWeight = FontWeight.Bold),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )


        // Register a callback for the Activity Result
        val photoPicker = rememberLauncherForActivityResult(PhotoPicker()) { uris ->
            // uris contain the list of selected images & video
            println(uris)
            selectedImages = uris
        }

        Button(
            onClick = {
                // Launch the picker with 5 max images & video selectable
                photoPicker.launch(PhotoPicker.Args(PhotoPicker.Type.IMAGES_AND_VIDEO, 20))
            },
            modifier = Modifier
                .wrapContentSize()
        ) {
            Text(text = "Choose images from gallery")
        }

        LazyVerticalGrid(GridCells.Fixed(3)) {
            items(selectedImages.size) { uri ->
                    
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "Profile screen",
            fontWeight = FontWeight.Bold,
            color = Color.Red,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 25.sp
        )
    }
}
