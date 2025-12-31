package com.keshen.alumni_directory_app.data.model


data class RegistrationForm (
    val id: String,
    val fullName : String,
    val email: String,
    val password: String,
    val graduationYear: Int,
    val department: String,
    val jobTitle: String,
    val company: String,
    val techStack: String,
    val city: String,
    val country: String,
    val contact: Int,
    val bio: String,
    val profilePicture: String
)