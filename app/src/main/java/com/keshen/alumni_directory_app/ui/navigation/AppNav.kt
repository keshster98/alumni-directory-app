package com.keshen.alumni_directory_app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.keshen.alumni_directory_app.service.AuthService
import com.keshen.alumni_directory_app.service.UserProfileService
import com.keshen.alumni_directory_app.ui.screens.auth.*
import com.keshen.alumni_directory_app.ui.screens.home.HomeScreen
import com.keshen.alumni_directory_app.ui.screens.profile.UserProfileScreen

@Composable
fun AppNav(
    authService: AuthService,
    profileService: UserProfileService
) {
    val rootNavController = rememberNavController()
    val isAdmin = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (authService.isLoggedIn()) {
            val completed = profileService.isProfileCompleted(authService.uid())
            isAdmin.value = profileService.isAdmin(authService.uid())
            rootNavController.navigate(
                if (completed) Screen.Main else Screen.RegistrationForm
            ) {
                popUpTo(0)
            }
        } else {
            rootNavController.navigate(Screen.SignIn) {
                popUpTo(0)
            }
        }
    }

    NavHost(
        navController = rootNavController,
        startDestination = Screen.SignIn
    ) {

        composable<Screen.SignIn> {
            SignInScreen(
                onSuccess = { completed ->
                    rootNavController.navigate(
                        if (completed) Screen.Main else Screen.RegistrationForm
                    ) {
                        popUpTo(Screen.SignIn) { inclusive = true }
                    }
                },
                onSignUpClick = {
                    rootNavController.navigate(Screen.SignUp)
                }
            )
        }

        composable<Screen.SignUp> {
            SignUpScreen(
                onSuccess = {
                    rootNavController.navigate(Screen.RegistrationForm) {
                        popUpTo(Screen.SignUp) { inclusive = true }
                    }
                },
                onSignInClick = {
                    rootNavController.navigate(Screen.SignIn)
                }
            )
        }

        composable<Screen.RegistrationForm> {
            RegistrationFormScreen(
                authService = authService,
                onCompleted = {
                    rootNavController.navigate(Screen.Main) {
                        popUpTo(Screen.RegistrationForm) { inclusive = true }
                    }
                }
            )
        }

        composable<Screen.Main> {
            val bottomNavController = rememberNavController()

            Scaffold(
                bottomBar = {
                    NavigationBar {
                        val backStack by bottomNavController.currentBackStackEntryAsState()
                        val currentRoute = backStack?.destination?.route

                        NavigationBarItem(
                            selected = currentRoute == Screen.Home::class.qualifiedName,
                            onClick = {
                                bottomNavController.navigate(Screen.Home) {
                                    launchSingleTop = true
                                }
                            },
                            icon = { Icon(Icons.Filled.Home, null) },
                            label = { Text("Home") }
                        )

                        NavigationBarItem(
                            selected = currentRoute == Screen.Profile::class.qualifiedName,
                            onClick = {
                                bottomNavController.navigate(Screen.Profile) {
                                    launchSingleTop = true
                                }
                            },
                            icon = { Icon(Icons.Filled.Person, null) },
                            label = { Text("Profile") }
                        )

                        NavigationBarItem(
                            selected = currentRoute == Screen.Settings::class.qualifiedName,
                            onClick = {
                                bottomNavController.navigate(Screen.Settings) {
                                    launchSingleTop = true
                                }
                            },
                            icon = { Icon(Icons.Filled.Settings, null) },
                            label = { Text("Settings") }
                        )

                        if (isAdmin.value) {
                            NavigationBarItem(
                                selected = currentRoute == Screen.Admin::class.qualifiedName,
                                onClick = {
                                    bottomNavController.navigate(Screen.Admin) {
                                        launchSingleTop = true
                                    }
                                },
                                icon = { Icon(Icons.Filled.AdminPanelSettings, null) },
                                label = { Text("Admin") }
                            )
                        }
                    }
                }
            ) { padding ->
                NavHost(
                    navController = bottomNavController,
                    startDestination = Screen.Home,
                    modifier = Modifier.padding(padding)
                ) {
                    composable<Screen.Home> {
                        HomeScreen(bottomNavController)
                    }
                    composable<Screen.Profile> {
                        UserProfileScreen()
                    }
                    composable<Screen.Settings> {
                        Text("Settings Screen")
                    }
                    composable<Screen.Admin> {
                        Text("Admin Dashboard")
                    }
                }
            }
        }
    }
}
