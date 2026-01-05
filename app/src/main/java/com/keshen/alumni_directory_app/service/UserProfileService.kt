package com.keshen.alumni_directory_app.service

import com.google.firebase.firestore.FirebaseFirestore
import com.keshen.alumni_directory_app.data.model.User
import kotlinx.coroutines.tasks.await

class UserProfileService(
    private val db: FirebaseFirestore
) {
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
            .whereEqualTo("status", "PENDING")
            .get()
            .await()
            .toObjects(User::class.java)
    }
}