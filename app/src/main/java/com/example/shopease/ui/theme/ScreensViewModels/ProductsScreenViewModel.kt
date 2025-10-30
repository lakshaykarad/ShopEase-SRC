package com.example.shopease.ui.theme.ScreensViewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.shopease.data.model.Products
import com.example.shopease.data.repository.ProductsRepository
import com.example.shopease.data.util.Resource
import com.example.shopease.ui.theme.AuthViewModel.RegisterScreen
import kotlinx.coroutines.launch

class ProductsScreenViewModel(private val repository: ProductsRepository) : ViewModel(){

    var productState by mutableStateOf<Resource<List<Products>>> (Resource.Loading)
        private set
     fun getProducts(){
        viewModelScope.launch {
            productState = Resource.Loading
            try {
                val productList = repository.getAllProducts() // fetch from repository
                productState = Resource.Success(productList)  // success
            }catch(e: Exception){
                productState = Resource.Error(e.message ?: "Unknows Error")
            }
        }
    }
}

class ProductsViewModelFactory(
    private val repository: ProductsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductsScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductsScreenViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}