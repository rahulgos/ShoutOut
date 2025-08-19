package com.example.shoutout.daos

import com.example.shoutout.models.Post
import com.example.shoutout.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class PostDao {

    private val db = FirebaseFirestore.getInstance()
    private val postCollection = db.collection("posts")

    // Returns query for real-time posts listener
    fun getAllPostsQuery(): Query = postCollection.orderBy("createdAt", Query.Direction.DESCENDING)

    // Add a new post
    fun addPost(text: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return
        val post = Post(
            text = text,
            createdBy = User(
                uid = currentUser.uid,
                displayName = currentUser.displayName ?: "",
                imageUrl = currentUser.photoUrl?.toString() ?: ""
            ),
            createdAt = System.currentTimeMillis()
        )
        postCollection.add(post)
    }

    // Update post text
    fun updatePost(postId: String, newText: String) {
        postCollection.document(postId).update("text", newText)
    }

    // Delete post
    fun deletePost(postId: String) {
        postCollection.document(postId).delete()
    }

    // Toggle like for current user
    fun updateLikes(postId: String, currentUserId: String) {
        val postRef = postCollection.document(postId)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(postRef)
            val post = snapshot.toObject(Post::class.java) ?: return@runTransaction
            val updatedLikes = if (post.likedBy.contains(currentUserId)) {
                post.likedBy - currentUserId
            } else {
                post.likedBy + currentUserId
            }
            transaction.update(postRef, "likedBy", updatedLikes)
        }
    }
}
