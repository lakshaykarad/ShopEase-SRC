package com.example.shopease

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.shopease.data.navigation.AppNavigation
import com.example.shopease.data.navigation.BottomNavigationBar
import com.example.shopease.data.navigation.NavHostContainer
import com.example.shopease.ui.theme.AuthViewModel.LoginScreen
import com.example.shopease.ui.theme.AuthViewModel.LoginViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
//            val loginViewModel: LoginViewModel = viewModel()
//            val state = loginViewModel.state
//
//            if (state.isUserLoggedIn) {
//                EcommerceApp()
//            } else {
//                AppNavigation(loginViewModel)
//            }
            EcommerceApp()
        }

    }
}
