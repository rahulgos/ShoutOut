package com.example.shoutout.daos

import com.example.shoutout.models.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.tasks.await

/**
 * DAO (Repository) for User operations in Firestore.
 */
class UserDao {

    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    /** Add a user */
    suspend fun addUser(user: User) {
        user.uid.let { uid ->
            usersCollection.document(uid).set(user).await()
        }
    }

    /** Fetch user by ID */
    suspend fun getUserById(uId: String): DocumentSnapshot {
        return usersCollection.document(uId).get().await()
    }
}
