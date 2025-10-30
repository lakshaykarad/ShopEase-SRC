package com.example.shopease.ui.theme.ScreensViewModels


import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.shopease.data.model.Products

class FavoritesViewModel : ViewModel() {

    val favoriteProducts = mutableStateListOf<Products>()

    fun isFavorite(products: Products) : Boolean{
        return favoriteProducts.any{it.id == products.id}
    }

    /** Add product to favorites if not already there */
    fun addFavorite(products: Products){
        if(!isFavorite(products)){
            favoriteProducts.add(products)
        }
    }

    fun removeFavorite(products: Products){
        favoriteProducts.removeAll { it.id == products.id }
    }

    fun toggleFavorite(products: Products){
        if(isFavorite(products)){
            removeFavorite(products)
        }else{
            addFavorite(products)
        }
    }

}
