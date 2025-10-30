package com.example.shopease.data.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.Navigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.room.util.TableInfo
import com.example.shopease.data.model.Routes
import com.example.shopease.ui.theme.ScreensViewModels.CartScreen
import com.example.shopease.ui.theme.ScreensViewModels.CartViewModel
import com.example.shopease.ui.theme.ScreensViewModels.FavoritesScreen
import com.example.shopease.ui.theme.ScreensViewModels.FavoritesViewModel
import com.example.shopease.ui.theme.ScreensViewModels.HomeScreen
import com.example.shopease.ui.theme.ScreensViewModels.ProductDetailsScreen
import com.example.shopease.ui.theme.ScreensViewModels.ProductsScreen
import com.example.shopease.ui.theme.ScreensViewModels.ProfileScreen
import com.example.shopease.ui.theme.ScreensViewModels.ProfileViewModel
@Composable
fun NavHostContainer(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val favoritesViewModel: FavoritesViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = BottomNavItem.HOME.route,
        modifier = modifier
    ) {
        composable(BottomNavItem.HOME.route) { HomeScreen(navController) }
        composable(BottomNavItem.CATEGORIES.route) { ProductsScreen(navController) }
        composable(BottomNavItem.CART.route) { CartScreen(cartViewModel, navController) }
        composable(BottomNavItem.PROFILE.route) {
            ProfileScreen(profileViewModel = profileViewModel, navController = navController)
        }
        composable(BottomNavItem.FAVORITES.route) {
            FavoritesScreen(navController = navController, favoritesViewModel = favoritesViewModel)
        }
        composable(
            route = "productDetails/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: 0
            ProductDetailsScreen(
                productId = productId,
                navController = navController,
                favoritesViewModel = favoritesViewModel,
                cartViewModel = cartViewModel
            )
        }
    }
}
