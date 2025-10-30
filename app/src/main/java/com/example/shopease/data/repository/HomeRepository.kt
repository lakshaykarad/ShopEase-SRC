package com.example.shopease.data.repository

import com.example.shopease.data.Apis.PlatziApiService

class HomeRepository(private val apiService : PlatziApiService) {

    suspend fun getAllProduct() = apiService.getAllProducts()
    suspend fun getAllCategory() = apiService.getAllCategories()

}