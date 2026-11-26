package com.example.costlyticmoneymanagement.model.firebaseRepo

import android.util.Log
import com.example.costlyticmoneymanagement.model.data.WalletItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class WalletRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseDatabase = FirebaseDatabase.getInstance()
) {
    fun observeWallets(): Flow<List<WalletItem>> = callbackFlow {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val walletsRef = db.getReference("wallets_table").child(uid)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull { child ->
                    child.getValue(WalletItem::class.java)?.copy(
                        id = child.key ?: ""
                    )
                }
                trySend(list)
            }

            override fun onCancelled(error: DatabaseError) {
//                Log()
                trySend(emptyList())
            }
        }

        walletsRef.addValueEventListener(listener)
        awaitClose { walletsRef.removeEventListener(listener) }
    }

    // Streams available banks from Firebase RTDB ("banks" node)
    fun observeBanks(): Flow<List<String>> = callbackFlow {
        val defaultBanks = listOf(
            "Chase", "PayPal", "Citi", "Bank of America",
            "Jago", "Mandiri", "BCA", "See Other"
        )

        val banksRef = db.getReference("banks")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val firebaseBanks = snapshot.children.mapNotNull {
                    it.getValue(String::class.java)
                }
                trySend(firebaseBanks.ifEmpty { defaultBanks })
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(defaultBanks)
            }
        }

        banksRef.addValueEventListener(listener)
        awaitClose { banksRef.removeEventListener(listener) }
    }

    fun addWallet(
        wallet: WalletItem,
        onResult: (Result<Unit>) -> Unit
    ) {
        val uid = auth.currentUser?.uid
            ?: return onResult(Result.failure(Exception("User not logged in")))

        val userWalletsRef = db.getReference("wallets").child(uid)

        userWalletsRef.get()
            .addOnSuccessListener { snapshot ->
                // Find the highest existing integer ID (starts at 0 if none exist)
                val maxId = snapshot.children
                    .mapNotNull { it.key?.toIntOrNull() }
                    .maxOrNull() ?: -1

                val nextId = (maxId + 1).toString() // "0", "1", "2", ...
                val finalWallet = wallet.copy(id = nextId)

                userWalletsRef.child(nextId).setValue(finalWallet)
                    .addOnSuccessListener { onResult(Result.success(Unit)) }
                    .addOnFailureListener { e -> onResult(Result.failure(e)) }
            }
            .addOnFailureListener { e ->
                onResult(Result.failure(e))
            }
    }
}