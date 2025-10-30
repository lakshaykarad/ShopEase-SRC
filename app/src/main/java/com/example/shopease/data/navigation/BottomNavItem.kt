package com.example.shopease.data.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route : String, val title : String, val icon : ImageVector){
    object HOME : BottomNavItem(route = "home", title = "Home", icon = Icons.Default.Home)
    object CATEGORIES : BottomNavItem(route = "products", title = "Products", icon = Icons.Default.List)
    object CART : BottomNavItem(route = "cart", title = "Cart", icon = Icons.Default.ShoppingCart)
    object FAVORITES : BottomNavItem(route = "favorites", title = "Favorites", icon = Icons.Default.Favorite)
    object PROFILE : BottomNavItem(route = "profile", title = "Profile", icon = Icons.Default.Person)

}