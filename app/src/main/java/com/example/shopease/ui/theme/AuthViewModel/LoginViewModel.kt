package com.example.shopease.ui.theme.AuthViewModel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopease.data.model.AuthState
import com.example.shopease.data.repository.AuthRepository
import com.example.shopease.data.util.Resource
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AuthRepository = AuthRepository()) : ViewModel(){

    var state by mutableStateOf(AuthState())
        private set

    fun onEmailChange(email : String){
        state = state.copy(email = email)
    }
    fun onPasswordChange(password : String){
        state = state.copy(password = password)
    }

    fun login(){
        if(!isInputValid()) return
        viewModelScope.launch {
            state = state.copy(isLoading = true, errorMessage = null)
            when (val result = repository.login(email = state.email, password = state.password)) {
                is Resource.Success -> {
                    // Access the user object if needed
                    val user = result.data
                    state = state.copy(
                        isLoading = false,
                        isUserLoggedIn = true,
                        errorMessage = null
                    )
                }
                is Resource.Error -> {
                    state = state.copy(
                        isLoading = false,
                        errorMessage = result.message ?: "Login failed"
                    )
                }
                else -> {
                    state = state.copy(isLoading = false)
                }
            }
        }

    }

    private fun isInputValid(): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
            state = state.copy(errorMessage = "Please enter a valid email address.")
            return false
        }
        if (state.password.length < 6) {
            state = state.copy(errorMessage = "Password must be at least 6 characters long.")
            return false
        }
        return true
    }
}