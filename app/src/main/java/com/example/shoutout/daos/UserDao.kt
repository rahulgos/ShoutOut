package com.example.shoutout.daos

import com.example.shoutout.models.User
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserDao {

    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    // Add a new user to Firestore
    suspend fun addUser(user: User) {
        usersCollection.document(user.uid).set(user).await()
    }

    // Get user details by userId
    suspend fun getUserById(uId: String): DocumentSnapshot {
        return usersCollection.document(uId).get().await()
    }
}
