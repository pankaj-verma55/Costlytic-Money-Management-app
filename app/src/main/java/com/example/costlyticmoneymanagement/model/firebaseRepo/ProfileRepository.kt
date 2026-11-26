package com.example.costlyticmoneymanagement.model.firebaseRepo

import com.example.costlyticmoneymanagement.model.data.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ProfileRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseDatabase = FirebaseDatabase.getInstance()
) {
    fun observeUserProfile(): Flow<UserProfile> = callbackFlow {
        val currentUser = auth.currentUser
        val uid = currentUser?.uid

        if (uid == null) {
            trySend(UserProfile())
            close()
            return@callbackFlow
        }

        val userRef = db.getReference("table_User").child(uid)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.child("name").getValue(String::class.java) ?: ""
                val email = snapshot.child("email").getValue(String::class.java)
                    ?: currentUser.email.orEmpty()
                val phone = snapshot.child("phone").getValue(String::class.java) ?: ""

                trySend(
                    UserProfile(
                        uid = uid,
                        name = name,
                        email = email,
                        phone = phone
                    )
                )
            }

            override fun onCancelled(error: DatabaseError) {
                // Keep fallback Auth email if DB read fails
                trySend(
                    UserProfile(
                        uid = uid,
                        email = currentUser.email.orEmpty()
                    )
                )
            }
        }

        userRef.addValueEventListener(listener)
        awaitClose { userRef.removeEventListener(listener) }
    }

    fun logout() {
        auth.signOut()
    }
}