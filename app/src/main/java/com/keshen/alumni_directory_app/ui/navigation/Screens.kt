package com.keshen.alumni_directory_app.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable object Main: Screen()
    @Serializable object Home: Screen()
    @Serializable object Profile : Screen()
    @Serializable object Settings : Screen()
    @Serializable object Admin : Screen()
    @Serializable object SignIn: Screen()
    @Serializable object SignUp: Screen()
    @Serializable object RegistrationForm: Screen()
}