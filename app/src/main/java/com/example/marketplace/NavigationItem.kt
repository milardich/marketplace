package com.example.marketplace

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem (var route: String, var icon: ImageVector, var title: String) {
    object Items : NavigationItem("items", Icons.Rounded.ShoppingCart, "Items")
    object AddItem : NavigationItem("addItem", Icons.Rounded.AddCircle, "Add item")
    object Profile : NavigationItem("profile", Icons.Rounded.Person, "Profile")
}
