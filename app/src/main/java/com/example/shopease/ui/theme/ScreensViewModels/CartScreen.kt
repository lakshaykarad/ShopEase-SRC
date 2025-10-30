package com.example.shopease.ui.theme.ScreensViewModels

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.shopease.data.model.CartItem
import com.google.firebase.ai.type.content

@Composable
fun CartScreen(cartViewModel: CartViewModel, navController : NavController) {

    val cartItems by remember { mutableStateOf(cartViewModel.cartItems) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF5F5F5))
            .navigationBarsPadding()
            .imePadding()
    ) {
        Text(
            text = "My Cart",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier.padding(16.dp)
        )
        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Your cart is empty",
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(cartItems) { item ->
                    CartItemRow(item = item,
                        onAdd = { cartViewModel.addToCart(item.product, item.size) },
                        onRemove = { cartViewModel.decreaseQuantity(item.product, item.size) },
                        onDelete = { cartViewModel.removeItem(item.product, item.size) },
                        onClick = {navController.navigate("productDetails/${item.product.id}")}
                    )

                }
            }
            Divider(color = Color.LightGray, thickness = 1.dp)

            Row (
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = "Total Price",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$${cartViewModel.totalPrice}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )
            }
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Checkout")
            }
        }
    }
}

@Composable
fun CartItemRow(
    item: CartItem,
    onAdd: () -> Unit,
    onRemove: () -> Unit,
    onDelete: () -> Unit,
    onClick : () -> Unit
) {

   Row (
       modifier = Modifier.fillMaxWidth()
           .padding(8.dp)
           .background(Color.White, RoundedCornerShape(8.dp))
           .padding(8.dp)
           .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically,
   ){
       AsyncImage(
           model = item.product.images.firstOrNull(),
           contentDescription = item.product.title,
           modifier = Modifier.size(80.dp)
       )

       Spacer(modifier = Modifier.width(8.dp))

       Column (modifier = Modifier.weight(1f)){
           Text("${item.product.title}", fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
           Text("${item.size}", fontSize = 14.sp, color = Color.Gray)
           Text("${item.product.price}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
       }

       Row (
           verticalAlignment = Alignment.CenterVertically
       ){
           IconButton(
               onClick = onRemove
           ) {
               Text(
                   text = "-",
                   color = Color.Gray,
                   fontSize = 20.sp
               )
           }

           Text(
               text = "${item.quantity}",
               fontSize = 16.sp,
               modifier = Modifier.padding(horizontal = 4.dp)
           )

           IconButton(
               onClick = onAdd
           ) {
               Text(
                   text = "+",
                   color = Color.Black,
                   fontSize = 20.sp
               )
           }

       }

       IconButton(onClick = onDelete) {
           Icon(Icons.Default.Delete, contentDescription = "Remove item", tint = Color.Blue)
       }
   }
}