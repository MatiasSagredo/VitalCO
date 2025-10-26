package com.example.vitalco

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vitalco.navigation.BottomBar
import com.example.vitalco.navigation.BottomNavItem
import com.example.vitalco.navigation.Routes
import com.example.vitalco.ui.screens.Home
import com.example.vitalco.ui.screens.LoginScreen
import com.example.vitalco.ui.screens.ProductDetailScreen
import com.example.vitalco.ui.screens.ProfileScreen
import com.example.vitalco.ui.screens.RegisterScreen
import com.example.vitalco.ui.theme.VitalCOTheme
import com.example.vitalco.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VitalCOTheme { App() }
        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    val bottomItems = listOf(BottomNavItem.Home, BottomNavItem.Profile)
    val mainViewModel: MainViewModel = viewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        NavHost(
            navController = navController,
            startDestination = Routes.LOGIN,
            modifier = Modifier.weight(1f)
        ) {
            composable(
                Routes.LOGIN,
                enterTransition = { fadeIn() },
                exitTransition = { fadeOut() }
            ) {
                Scaffold { innerPadding ->
                    LoginScreen(
                        modifier = Modifier.padding(innerPadding),
                        mainViewModel = mainViewModel,
                        onLoginSuccess = {
                            navController.navigate(Routes.HOME) {
                                popUpTo(Routes.LOGIN) { inclusive = true }
                            }
                        },
                        onNavigateToRegister = {
                            navController.navigate(Routes.REGISTER)
                        }
                    )
                }
            }
            composable(
                Routes.REGISTER,
                enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { it }) + fadeOut() }
            ) {
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.REGISTER) { inclusive = true }
                        }
                    },
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
            composable(
                Routes.HOME,
                enterTransition = { slideInHorizontally(initialOffsetX = { -it }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }
            ) {
                Scaffold(
                    bottomBar = { BottomBar(navController, bottomItems) }
                ) { innerPadding ->
                    Home(
                        modifier = Modifier.padding(innerPadding),
                        onNavigateToDetail = { product ->
                            mainViewModel.setSelectedProduct(product)
                            navController.navigate(Routes.PRODUCT_DETAIL)
                        }
                    )
                }
            }
            composable(Routes.PRODUCT_DETAIL) {
                val selectedProduct = mainViewModel.selectedProduct
                if (selectedProduct != null) {
                    ProductDetailScreen(
                        product = selectedProduct,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
            composable(
                Routes.PROFILE,
                enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
                exitTransition = { slideOutHorizontally(targetOffsetX = { it }) + fadeOut() }
            ) {
                Scaffold(
                    bottomBar = { BottomBar(navController, bottomItems) }
                ) { innerPadding ->
                    ProfileScreen(
                        modifier = Modifier.padding(innerPadding),
                        mainViewModel = mainViewModel,
                        onLogout = {
                            navController.navigate(Routes.LOGIN) {
                                popUpTo(Routes.HOME) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}
