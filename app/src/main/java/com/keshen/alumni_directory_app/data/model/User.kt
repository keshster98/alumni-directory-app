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
    val status: Status = Status.PENDING,

    // New profile fields
    val linkedIn: String = "",
    val github: String = "",

    // Visibility controls (owner can toggle in registration form)
    val showEmail: Boolean = true,
    val showLinkedIn: Boolean = true,
    val showGithub: Boolean = true,
    val showPhone: Boolean = true,
) {
    fun displayEmail(viewerIsAdmin: Boolean, viewerUid: String): String =
        if (viewerIsAdmin || viewerUid == uid || showEmail) email else "Hide"

    fun displayLinkedIn(viewerIsAdmin: Boolean, viewerUid: String): String =
        if (viewerIsAdmin || viewerUid == uid || showLinkedIn) linkedIn else "Hide"

    fun displayGithub(viewerIsAdmin: Boolean, viewerUid: String): String =
        if (viewerIsAdmin || viewerUid == uid || showGithub) github else "Hide"

    fun displayPhone(viewerIsAdmin: Boolean, viewerUid: String): String =
        if (viewerIsAdmin || viewerUid == uid || showPhone) contact else "Hide"
}

enum class Status {
    PENDING,REJECTED,APPROVED
}
