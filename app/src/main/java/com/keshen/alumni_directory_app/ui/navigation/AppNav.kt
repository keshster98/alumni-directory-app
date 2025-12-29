package com.keshen.alumni_directory_app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.keshen.alumni_directory_app.ui.screens.auth.RegistrationFormScreen
import com.keshen.alumni_directory_app.ui.screens.auth.SignInScreen
import com.keshen.alumni_directory_app.ui.screens.auth.SignUpScreen
import com.keshen.alumni_directory_app.ui.screens.home.HomeScreen
import com.keshen.alumni_directory_app.ui.screens.profile.ProfileScreen

@Composable
fun AppNav() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.SignIn,

    ) {
        composable<Screen.Home> {
            HomeScreen(navController)
        }
        composable<Screen.SignIn> {
            SignInScreen(navController)
        }

        composable<Screen.SignUp> {
            SignUpScreen(navController)
        }

        //test
        composable<Screen.RegistrationForm> {
            RegistrationFormScreen()
        }

        composable<Screen.Profile> {
            ProfileScreen()
        }
    }
}