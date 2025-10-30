package com.example.shopease.ui.theme.AuthViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopease.data.model.AuthState
import com.example.shopease.data.model.User
import com.example.shopease.data.repository.AuthRepository
import com.example.shopease.data.util.Resource
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: AuthRepository = AuthRepository()) : ViewModel() {
    var state by mutableStateOf(AuthState())
        private set

    fun onFullNameChange(name : String){
        state = state.copy(fullName = name)
    }
    fun onMobileChange(mobile : String){
        state = state.copy(mobile = mobile)
    }
    fun onEmailChange(email : String){
        state = state.copy(email = email)
    }
    fun onPasswordChange(password : String){
        state = state.copy(password = password)
    }

    // ADDED: A helper function for email validation
    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun checkFirstTimeUser(){
        val sanitizedEmail = state.email.trim().lowercase()
        if (!isEmailValid(sanitizedEmail)) return

        viewModelScope.launch {
            state = state.copy(isLoading = true, errorMessage = null)
            val exists = repository.checkUserExists(sanitizedEmail)
            state = state.copy(isLoading = false, isFirstTimeUser = !exists)
        }
    }

    fun registerUser(){
        val fullName = state.fullName.trim()
        val mobile = state.mobile.trim()
        val email = state.email.trim().lowercase()
        val password = state.password


        if (fullName.isEmpty() || mobile.isEmpty() || email.isEmpty() || password.isEmpty()) {
            state = state.copy(errorMessage = "All fields are required.")
            return
        }

        if (!isEmailValid(email)) {
            state = state.copy(errorMessage = "Please enter a valid email address.")
            return
        }

        if (password.length < 6) {
            state = state.copy(errorMessage = "Password must be at least 6 characters long.")
            return
        }

        viewModelScope.launch {
            state = state.copy(isLoading = true, errorMessage = null)
            // Use the sanitized variables
            val user = User(uid = "", fullName = fullName, email = email, mobile = mobile)
            when(val result = repository.registerUser(user, password)){
                is Resource.Success -> state = state.copy(isLoading = false, isRegister = true)
                is Resource.Error -> state = state.copy(isLoading = false, errorMessage = result.message)
                // FIXED: Correctly assign the new state
                else -> state = state.copy(isLoading = false)
            }
        }
    }
}