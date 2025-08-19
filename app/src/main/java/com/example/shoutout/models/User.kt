package com.example.shoutout.models

/**
 * Represents a user in Firestore.
 */
data class User(
    val uid: String = "",
    val displayName: String? = "",
    val imageUrl: String = ""
)
