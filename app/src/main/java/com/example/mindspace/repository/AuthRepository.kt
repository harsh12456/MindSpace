package com.example.mindspace.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun register(name: String, email: String, password: String): Boolean {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.let { user ->
                // Update display name
                user.updateProfile(
                    com.google.firebase.auth.UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()
                )?.await()

                // Save user profile to Firestore
                val userProfile = hashMapOf(
                    "name" to name,
                    "email" to email,
                    "uid" to user.uid
                )
                firestore.collection("users").document(user.uid).set(userProfile).await()
                true
            } ?: false
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun login(email: String, password: String): Boolean {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user != null
        } catch (e: Exception) {
            throw e
        }
    }

    fun getCurrentUser() = auth.currentUser

    fun logout() = auth.signOut()
}