package com.keshen.alumni_directory_app.data.model

data class User(
    val id: String,
    val email: String,
    val password: String,
    val isAdmin: Boolean = false,
    val status: Status = Status.PENDING
)

enum class Status {
    PENDING,REJECTED,APPROVED
}
