package com.example.shopease.data.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shopease.data.model.Routes
import com.example.shopease.ui.theme.AuthViewModel.LoginScreen
import com.example.shopease.ui.theme.AuthViewModel.LoginViewModel
import com.example.shopease.ui.theme.AuthViewModel.RegisterScreen
import com.example.shopease.ui.theme.AuthViewModel.RegisterViewModel
import com.example.shopease.ui.theme.ScreensViewModels.HomeScreen
@Composable
fun AppNavigation(loginViewModel: LoginViewModel) {
    val navController = rememberNavController()
    val registerViewModel : RegisterViewModel = viewModel()
    NavHost(navController, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) {
            LoginScreen(navController, loginViewModel)
        }
        composable(Routes.REGISTER) {
            RegisterScreen(registerViewModel,navController)
        }
    }
}
