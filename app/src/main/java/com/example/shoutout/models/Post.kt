package com.example.shoutout.models

/**
 * Represents a single post in Firestore.
 */
data class Post(
    val text: String = "",
    val createdBy: User = User(),
    val createdAt: Long = 0L,
    val likedBy: ArrayList<String> = ArrayList(),
    var postId: String = "" // Firestore document ID
)
