package com.example.shopease.data.repository

import com.example.shopease.data.Apis.PlatziApiService

class ProductDetailsRepository (private val apiService: PlatziApiService){
    suspend fun getProductById(id : Int) = apiService.getProductById(id)
}