package com.example.shopease.ui.theme.ScreensViewModels

import android.R
import android.app.ActionBar
import android.graphics.Paint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.room.util.TableInfo
import androidx.test.espresso.base.Default
import com.example.shopease.data.navigation.BottomNavItem

@Composable
fun ProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = viewModel()
) {

    val userInfo by profileViewModel.userInfo.collectAsState()
    val errorMessage by profileViewModel.errorMessage.collectAsState()
    val isLoading by profileViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        profileViewModel.loadUserData()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
    )
    {
        when{
            isLoading-> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

            errorMessage != null  -> Text(
                text = "Somthing Went Wrong.",
                color = Color.Red,
                modifier = Modifier.align(Alignment.Center)
            )

            userInfo != null->ProfileContent(
                name = userInfo?.fullName ?: "Unknown",
                email = userInfo?.email ?: "Testing@gmail.com",
                navController = navController
            )

            else -> Text("No Data Found", modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun ProfileContent(name: String, email: String, navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp, start = 20.dp, end = 20.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        val firstLetter = name.firstOrNull()?.uppercase()?:"A"
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.Blue),
            contentAlignment = Alignment.Center
        ){
            Text(text = firstLetter, fontSize = 48.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(10.dp))

        Text(text = name, fontWeight = FontWeight.Medium, fontSize = 22.sp )

        Text(text = email, color = Color.Gray, fontSize = 18.sp)

        ActionCard(
            icon = Icons.Default.Home,
            title = "Home",
            description = "Go to Home Screen",
            onClick = { navController.navigate(BottomNavItem.HOME.route) }
        )

        ActionCard(
            icon = Icons.Default.Favorite,
            title = "Favorites",
            description = "Your favorite products",
            onClick = { navController.navigate(BottomNavItem.FAVORITES.route) }
        )

        ActionCard(
            icon = Icons.Default.ShoppingBag,
            title = "Products",
            description = "View product catalog",
            onClick = { navController.navigate(BottomNavItem.CATEGORIES.route) }
        )

        ActionCard(
            icon = Icons.Default.ShoppingCart,
            title = "Cart",
            description = "View your shopping cart",
            onClick = { navController.navigate(BottomNavItem.CART.route) }
        )

        ActionCard(
            icon = Icons.Default.ReceiptLong,
            title = "My Orders",
            description = "Track your orders",
            onClick = { navController.navigate(BottomNavItem.CART.route) }
        )

        ActionCard(
            icon = Icons.Default.ExitToApp,
            title = "Logout",
            description = "Sign out of your account",
            onClick = { navController.navigate(BottomNavItem.PROFILE.route) }
        )
    }

}

@Composable
fun ActionCard(icon: ImageVector, title: String, description: String, onClick: () -> Unit) {

    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ){
        Row (
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color.Blue,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = title, fontWeight = FontWeight.Medium, fontSize = 18.sp)
                Text(text = description, color = Color.Gray, textAlign = TextAlign.Start, fontSize = 16.sp)
            }
        }

    }
    
}


