package com.keshen.alumni_directory_app.data.model

data class User(
    val uid: String = "",
    val fullName: String = "",
    val email: String = "",
    val graduationYear: Int = 0,
    val department: String = "",
    val jobTitle: String = "",
    val company: String = "",
    val techStack: String = "",
    val city: String = "",
    val country: String = "",
    val contact: String = "",
    val bio: String = "",
    val profilePicture: String = "",
    val completed: Boolean = true,
    val isAdmin: Boolean = false,
    val status: Status = Status.PENDING
)

enum class Status {
    PENDING,REJECTED,APPROVED
}
