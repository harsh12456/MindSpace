package com.example.mindspace.services.Firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.tasks.await

object FirebaseAuthService {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun isUserLoggedIn(): Boolean = auth.currentUser != null

    suspend fun loginWithEmail(email: String, password: String): AuthResult {
        return auth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun registerWithEmail(email: String, password: String): AuthResult {
        return auth.createUserWithEmailAndPassword(email, password).await()
    }

    suspend fun loginWithGoogle(idToken: String): AuthResult {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return auth.signInWithCredential(credential).await()
    }

    fun signOut() {
        auth.signOut()
    }
}