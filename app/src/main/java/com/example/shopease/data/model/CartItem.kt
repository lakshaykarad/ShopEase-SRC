package com.example.shopease.data.model

data class CartItem  (
    val product: Products,
    val size : String,
    val quantity : Int = 1
)

