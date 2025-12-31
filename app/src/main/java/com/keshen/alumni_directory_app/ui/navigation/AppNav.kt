package com.keshen.alumni_directory_app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.keshen.alumni_directory_app.service.AuthService
import com.keshen.alumni_directory_app.service.UserProfileService
import com.keshen.alumni_directory_app.ui.screens.auth.RegistrationFormScreen
import com.keshen.alumni_directory_app.ui.screens.auth.SignInScreen
import com.keshen.alumni_directory_app.ui.screens.auth.SignUpScreen
import com.keshen.alumni_directory_app.ui.screens.home.HomeScreen
import com.keshen.alumni_directory_app.ui.screens.profile.AlumniProfileScreen
import com.keshen.alumni_directory_app.ui.screens.profile.UserProfileScreen

@Composable
fun AppNav(
    authService: AuthService,
    profileService: UserProfileService
) {
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        if (authService.isLoggedIn()) {
            val completed = profileService.isProfileCompleted(authService.uid())

            navController.navigate(
                if (completed) Screen.Home else Screen.RegistrationForm
            ) {
                popUpTo(0)
            }
        } else {
            navController.navigate(Screen.SignIn) {
                popUpTo(0)
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.SignIn
    ) {

        composable<Screen.SignIn> {
            SignInScreen(
                authService = authService,
                profileService = profileService,
                onSuccess = { completed ->
                    navController.navigate(
                        if (completed) Screen.Home else Screen.RegistrationForm
                    ) {
                        popUpTo(Screen.SignIn) { inclusive = true }
                    }
                },
                onSignUpClick = {
                    navController.navigate(Screen.SignUp)
                }
            )
        }

        composable<Screen.SignUp> {
            SignUpScreen(
                authService = authService,
                onSuccess = {
                    navController.navigate(Screen.RegistrationForm) {
                        popUpTo(Screen.SignUp) { inclusive = true }
                    }
                },
                onSignInClick = {
                    navController.navigate(Screen.SignIn)
                }
            )
        }

        composable<Screen.RegistrationForm> {
            RegistrationFormScreen(
                authService = authService,
                profileService = profileService,
                onCompleted = {
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.RegistrationForm) { inclusive = true }
                    }
                }
            )
        }

        composable<Screen.Home> {
            HomeScreen(navController, authService = authService)
        }

        composable<Screen.AlumniProfile> {
            AlumniProfileScreen()
        }
    }
}