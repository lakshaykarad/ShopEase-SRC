package com.example.shopease.data.model

data class AuthState(

    // email password fullName mobile isLoading errorMessage isUserLoggedin isRegistered isFirstTimeUser

    val email : String = "",
    val password : String = "",
    val fullName : String = "",
    val mobile : String = "",
    val isLoading : Boolean = false,
    val errorMessage : String? = null,
    val isUserLoggedIn : Boolean = false,
    val isRegister : Boolean = false,
    val isFirstTimeUser : Boolean? = null

)