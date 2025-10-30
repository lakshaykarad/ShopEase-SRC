package com.example.shopease.ui.theme.ScreensViewModels

import android.util.Log
import android.util.Size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopease.data.model.Products
import com.example.shopease.data.repository.ProductDetailsRepository
import kotlinx.coroutines.launch

class ProductDetailsViewModel(
    private val repository: ProductDetailsRepository
): ViewModel(){

    var product by mutableStateOf<Products?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set

    var size by mutableStateOf("M")
        private set

    var cartItems by mutableStateOf(0)
        private set

    fun loadProductDetails(productId : Int){
        viewModelScope.launch {
            try {
                isLoading = true
                product = repository.getProductById(productId)
            }catch (e : Exception){
                Log.e("ProductDetailsViewModel", "${e.message}")
            }finally {
                isLoading = false
            }
        }
    }

    fun selectSize(selectSize: String){
        size = selectSize
    }


    fun orderNow(onOrdered: () -> Unit) {
        product?.let {
            println("Ordering ${it.title} (size $size)")
            onOrdered()
        }
    }

}