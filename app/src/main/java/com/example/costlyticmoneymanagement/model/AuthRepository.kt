package com.example.costlyticmoneymanagement.model

import com.example.costlyticmoneymanagement.model.sealed.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.FirebaseDatabase

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseDatabase = FirebaseDatabase.getInstance()
) {
    fun getCurrentUserId(): String? = auth.currentUser?.uid

    fun getCurrentUserEmail(): String? = auth.currentUser?.email

    fun loginUser(
        email: String,
        password: String,
        onResult: (AuthResult) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                onResult(AuthResult.Success)
            }
            .addOnFailureListener { exception ->
                when (exception) {
                    is FirebaseAuthInvalidUserException -> {
                        onResult(AuthResult.EmailError("Email is not registered"))
                    }
                    is FirebaseAuthInvalidCredentialsException -> {
                        onResult(AuthResult.PasswordError("Incorrect password"))
                    }
                    else -> {
                        onResult(
                            AuthResult.PasswordError(
                                exception.localizedMessage ?: "Authentication failed"
                            )
                        )
                    }
                }
            }
    }

    fun signUpUser(
        name: String,
        email: String,
        phone: String,
        password: String,
        onResult: (AuthResult) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val uid = authResult.user?.uid
                if (uid == null) {
                    onResult(AuthResult.GeneralError("Failed to create user ID"))
                    return@addOnSuccessListener
                }

                val userMap = mapOf(
                    "uid" to uid,
                    "name" to name,
                    "email" to email,
                    "phone" to phone,
                    "currency" to "₹"
                )

                db.getReference("users").child(uid).setValue(userMap)
                    .addOnSuccessListener {
                        onResult(AuthResult.Success)
                    }
                    .addOnFailureListener { e ->
                        onResult(
                            AuthResult.GeneralError(
                                e.localizedMessage ?: "Failed to save profile"
                            )
                        )
                    }
            }
            .addOnFailureListener { exception ->
                when (exception) {
                    is FirebaseAuthUserCollisionException -> {
                        onResult(AuthResult.EmailError("Email is already registered"))
                    }
                    else -> {
                        onResult(
                            AuthResult.GeneralError(
                                exception.localizedMessage ?: "Sign up failed"
                            )
                        )
                    }
                }
            }
    }

    fun sendPasswordReset(email: String, onResult: (Result<Unit>) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener { onResult(Result.success(Unit)) }
            .addOnFailureListener { e -> onResult(Result.failure(e)) }
    }

    fun logout() {
        auth.signOut()
    }
}