package com.example.mindspace.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun register(name: String, email: String, password: String): Boolean {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        // Optionally update display name
        result.user?.updateProfile(com.google.firebase.auth.UserProfileChangeRequest.Builder().setDisplayName(name).build())?.await()
        return result.user != null
    }

    suspend fun login(email: String, password: String): Boolean {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return result.user != null
    }
}