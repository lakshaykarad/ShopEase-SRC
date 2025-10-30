package com.example.shopease.ui.theme.ScreensViewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import coil.size.Size
import com.example.shopease.data.model.CartItem
import com.example.shopease.data.model.Products

class CartViewModel : ViewModel() {

    private val _cartItems = mutableStateListOf<CartItem>()
    val cartItems : List<CartItem> get()  = _cartItems

    fun addToCart(products : Products, size: String){
        val existingItem = _cartItems.find{it.product.id == products.id && it.size == size}
        if(existingItem != null){
            val index = _cartItems.indexOf(existingItem)
            _cartItems[index] = existingItem.copy(quantity = existingItem.quantity + 1)
        }else{
            _cartItems.add(CartItem(product = products, size = size))
        }
    }

    fun decreaseQuantity(products: Products, size : String){
        val existingItem = _cartItems.find { it.product.id == products.id && it.size == size }
        if(existingItem != null){
            val index = _cartItems.indexOf(existingItem)
            val newQty = (existingItem.quantity - 1).coerceAtLeast(0)
            _cartItems[index] = existingItem.copy(quantity = newQty)
        }
    }

    fun removeItem(products: Products, size: String){
        _cartItems.removeAll { it.product.id == products.id && size == size }
    }

    fun clearCart(){
        _cartItems.clear()
    }

    val totalPrice : Double
        get() = _cartItems.sumOf { it.product.price * it.quantity }
}
