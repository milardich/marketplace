package com.example.marketplace

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity

@Composable
fun NavigationButtons(context: Context) {
    // Navigation buttons
    Column(
        modifier = Modifier.fillMaxHeight(), // Fill the entire height of the screen
        verticalArrangement = Arrangement.Bottom // Align items to the bottom
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), // Add some padding for spacing
            horizontalArrangement = Arrangement.SpaceEvenly // Align buttons evenly
        ) {
            FloatingActionButton(
                onClick = {
                    openActivity(context, BrowseItemsActivity::class.java)
                },
            ) {
                Icon(Icons.Filled.ShoppingCart, "Browse items.")
            }

            FloatingActionButton(
                onClick = {
                    openActivity(context, AddItemActivity::class.java)
                },
            ) {
                Icon(Icons.Filled.Add, "Add item.")
            }

            FloatingActionButton(
                onClick = {
                    // openActivity(context, ProfileActivity::class.java)
                },
            ) {
                Icon(Icons.Filled.Person, "Profile.")
            }
        }
    }
}

fun openActivity(context: Context, activityClass: Class<*>) {
    var openItemIntent: Intent
    openItemIntent = Intent(context, activityClass)
    context.startActivity(openItemIntent)
}