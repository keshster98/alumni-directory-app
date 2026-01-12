package com.keshen.alumni_directory_app.core.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object Utils {

    /**
     * Checks if a user is currently logged in
     * @return true if user is authenticated, false otherwise
     */
    fun isUserLoggedIn(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    /**
     * Gets the current user ID
     * @return user ID if logged in, null otherwise
     */
    fun getCurrentUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    /**
     * Checks if the current user has admin privileges
     * @return true if user is admin, false otherwise
     */
    suspend fun isAdmin(): Boolean {
        val userId = getCurrentUserId() ?: return false

        return try {
            val document = FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .get()
                .await()

            document.getBoolean("isAdmin") ?: false
        } catch (e: Exception) {
            false
        }
    }
}
