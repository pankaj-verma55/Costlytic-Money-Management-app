package com.example.costlyticmoneymanagement.model.firebaseRepo

import com.example.costlyticmoneymanagement.model.data.BudgetRecord
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class BudgetRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseDatabase = FirebaseDatabase.getInstance()
) {
    fun observeBudgets(): Flow<List<BudgetRecord>> = callbackFlow {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val ref = db.getReference("budgets").child(uid)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull { child ->
                    child.getValue(BudgetRecord::class.java)?.copy(
                        id = child.key ?: ""
                    )
                }
                trySend(list)
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(emptyList())
            }
        }

        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    fun saveBudget(
        budget: BudgetRecord,
        onResult: (Result<Unit>) -> Unit
    ) {
        val uid = auth.currentUser?.uid
            ?: return onResult(Result.failure(Exception("User not logged in")))

        val ref = db.getReference("budgets").child(uid).push()
        val finalBudget = budget.copy(id = ref.key ?: "")

        ref.setValue(finalBudget)
            .addOnSuccessListener { onResult(Result.success(Unit)) }
            .addOnFailureListener { e -> onResult(Result.failure(e)) }
    }
}