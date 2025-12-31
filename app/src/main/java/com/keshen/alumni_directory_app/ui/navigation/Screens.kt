package com.keshen.alumni_directory_app.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable object Home: Screen()
    @Serializable object SignIn: Screen()
    @Serializable object SignUp: Screen()
    @Serializable object RegistrationForm: Screen()
    @Serializable object AlumniProfile: Screen()

    @Serializable object Alumni: Screen()
}