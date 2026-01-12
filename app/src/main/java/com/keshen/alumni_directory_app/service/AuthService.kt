package com.keshen.alumni_directory_app.service

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthService(
    private val auth: FirebaseAuth
) {
    suspend fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).await()
    }

    suspend fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    fun isLoggedIn(): Boolean = auth.currentUser != null

    fun uid(): String = auth.currentUser!!.uid

    fun email(): String = auth.currentUser!!.email!!

    fun signOut() = auth.signOut()
}