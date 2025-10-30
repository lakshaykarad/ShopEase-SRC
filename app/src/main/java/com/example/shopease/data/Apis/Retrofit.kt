package com.example.shopease.data.Apis

import com.example.shopease.data.model.Category
import com.example.shopease.data.model.Products
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET

interface PlatziApiService {

    @GET("api/v1/products")
    suspend fun getAllProducts() : List<Products>

    @GET("api/v1/categories")
    suspend fun getAllCategories() : List<Category>

    @GET("api/v1/products/{id}")
    suspend fun getProductById(@retrofit2.http.Path("id") id : Int) : Products

}

object RetrofitInstance {

    val api : PlatziApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.escuelajs.co/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlatziApiService::class.java)
    }

}