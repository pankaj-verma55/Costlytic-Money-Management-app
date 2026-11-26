package com.example.costlyticmoneymanagement.model.firebaseRepo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

sealed class AuthResult {
    data object Success : AuthResult()
    data class EmailError(val message: String) : AuthResult()
    data class PasswordError(val message: String) : AuthResult()
    data class GeneralError(val message: String) : AuthResult()
}

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseDatabase = FirebaseDatabase.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
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
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    if (uid == null) {
                        onResult(AuthResult.GeneralError("Failed to get user ID"))
                        return@addOnCompleteListener
                    }

                    val userProfile = hashMapOf(
                        "name" to name,
                        "phone" to phone,
                        "email" to email
                    )

                    // Save to Realtime Database ("table_User")
                    db.getReference("table_User").child(uid).setValue(userProfile)

                    // Save to Firestore ("users")
                    firestore.collection("users").document(uid)
                        .set(userProfile)
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
                } else {
                    when (val exception = task.exception) {
                        is FirebaseAuthUserCollisionException -> {
                            onResult(AuthResult.EmailError("Email is already registered"))
                        }
                        else -> {
                            onResult(
                                AuthResult.GeneralError(
                                    exception?.localizedMessage ?: "Sign up failed"
                                )
                            )
                        }
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