package com.example.shopease.data.model

import com.google.gson.annotations.SerializedName

data class Products(
    @SerializedName("id") val id : Int,
    @SerializedName("title") val title : String,
    @SerializedName("price") val price : Double,
    @SerializedName("description") val description : String,
    @SerializedName("images") val images : List<String>,
    @SerializedName("category") val category : Category,
)

data class Category(
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("image") val image : String
)
