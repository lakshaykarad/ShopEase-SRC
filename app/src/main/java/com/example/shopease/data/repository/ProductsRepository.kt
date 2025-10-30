package com.example.shopease.data.repository

import com.example.shopease.data.Apis.PlatziApiService

class ProductsRepository(private val apiService: PlatziApiService) {
    suspend fun getAllProducts() = apiService.getAllProducts()
}

