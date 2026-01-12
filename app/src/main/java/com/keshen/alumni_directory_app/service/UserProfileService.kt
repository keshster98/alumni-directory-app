package com.keshen.alumni_directory_app.service

import com.google.firebase.firestore.FirebaseFirestore
import com.keshen.alumni_directory_app.data.model.Status
import com.keshen.alumni_directory_app.data.model.User
import kotlinx.coroutines.tasks.await

class UserProfileService(
    private val db: FirebaseFirestore
) {
    suspend fun getProfile(uid: String): User {
        val doc = db.collection("users")
            .document(uid)
            .get()
            .await()

        if (!doc.exists()) {
            throw IllegalStateException("Profile not found")
        }

        return doc.toObject(User::class.java)
            ?: throw IllegalStateException("Failed to parse profile")
    }

    suspend fun updateWorkInfo(
        uid: String,
        jobTitle: String,
        company: String,
        techStack: String
    ) {
        db.collection("users").document(uid).update(
            mapOf(
                "jobTitle" to jobTitle,
                "company" to company,
                "primaryTechStack" to techStack
            )
        ).await()
    }

    suspend fun updateLocation(uid: String, city: String, country: String) {
        db.collection("users").document(uid).update(
            mapOf("city" to city, "country" to country)
        ).await()
    }

    suspend fun updateContact(
        uid: String,
        phone: String,
        github: String,
        linkedin: String
    ) {
        db.collection("users").document(uid).update(
            mapOf(
                "phone" to phone,
                "github" to github,
                "linkedIn" to linkedin
            )
        ).await()
    }

    suspend fun updateVisibility(
        uid: String,
        showPhone: Boolean,
        showLinkedIn: Boolean,
        showGithub: Boolean
    ) {
        db.collection("users").document(uid).update(
            mapOf(
                "showPhone" to showPhone,
                "showLinkedIn" to showLinkedIn,
                "showGithub" to showGithub
            )
        ).await()
    }

    suspend fun getUserDisplayName(uid: String, email: String): String {
        val doc = db.collection("users").document(uid).get().await()

        val fullName = doc.getString("fullName")

        return fullName?.takeIf { it.isNotBlank() } ?: email
    }

    suspend fun isProfileCompleted(uid: String): Boolean {
        val doc = db.collection("users").document(uid).get().await()
        return doc.exists() && doc.getBoolean("completed") == true
    }

    suspend fun saveProfile(profile: User) {
        db.collection("users")
            .document(profile.uid)
            .set(profile)
            .await()
    }

    suspend fun getApprovedUsers(): List<User> {
        return db.collection("users")
            .whereEqualTo("status", "APPROVED")
            .get()
            .await()
            .toObjects(User::class.java)
    }

    suspend fun isAdmin(uid: String): Boolean {
        val doc = db.collection("users").document(uid).get().await()
        return doc.getBoolean("admin") == true
    }

    suspend fun getUserStatus(uid: String): Status {
        val doc = db.collection("users").document(uid).get().await()
        val status = doc.getString("status") ?: Status.PENDING.name
        return Status.valueOf(status)
    }

    suspend fun getAllUsers(): List<User> =
        db.collection("users").get().await().toObjects(User::class.java)
}