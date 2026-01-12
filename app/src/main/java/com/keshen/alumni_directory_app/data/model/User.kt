package com.keshen.alumni_directory_app.data.model

data class User(
    val uid: String = "",

    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val showPhone: Boolean = true,

    val graduationYear: Int = 0,
    val department: String = "",

    val jobTitle: String = "",
    val company: String = "",
    val primaryTechStack: String = "",

    val city: String = "",
    val country: String = "",

    val linkedIn: String = "",
    val showLinkedIn: Boolean = true,
    val github: String = "",
    val showGithub: Boolean = true,

    val completed: Boolean = true,
    val isAdmin: Boolean = false,
    val status: Status = Status.PENDING,
)
enum class Status {
    PENDING,REJECTED,APPROVED,INACTIVE
}
