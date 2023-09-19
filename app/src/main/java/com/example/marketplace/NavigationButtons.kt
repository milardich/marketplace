package com.example.marketplace

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat.startActivity

@Composable
fun NavigationButtons(context: Context) {
    // Navigation buttons
    Row (
        modifier = Modifier.fillMaxWidth(),
    ){
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
                // openActivity(context, BrowseItemsActivity::class.java)
            },
        ) {
            Icon(Icons.Filled.Person, "Profile.")
        }
    }
}

fun openActivity(context: Context, activityClass: Class<*>) {
    var openItemIntent: Intent
    openItemIntent = Intent(context, activityClass)
    context.startActivity(openItemIntent)
}