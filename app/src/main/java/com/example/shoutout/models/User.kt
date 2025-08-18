package com.example.shoutout.models

/**
 * User Model - represents an app user in Firestore.
 */
data class User(
    val uid: String = "",
    val displayName: String? = "",
    val imageUrl: String = ""
)
