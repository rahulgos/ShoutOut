package com.example.shoutout.daos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoutout.models.Post
import com.example.shoutout.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class PostDao {

    private val db = FirebaseFirestore.getInstance()
    private val postCollections = db.collection("posts")
    private val auth = Firebase.auth
    private var postsListener: ListenerRegistration? = null

    /** Add new post */
    suspend fun addPost(text: String, currentUserId: String) {
        val userDao = UserDao()
        val snapshot = userDao.getUserById(currentUserId)
        val user = snapshot.toObject(User::class.java) ?: return
        val currentTime = System.currentTimeMillis()
        val postId = postCollections.document().id
        val post = Post(postId = postId, text = text, createdBy = user, createdAt = currentTime)
        postCollections.document(postId).set(post).await()
    }

    /** Live updates for posts */
    fun listenToPosts(): LiveData<List<Post>> {
        val liveData = MutableLiveData<List<Post>>()
        postsListener?.remove() // avoid duplicate listeners

        postsListener = postCollections
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) {
                    liveData.postValue(emptyList())
                    return@addSnapshotListener
                }
                val posts = snapshot.toObjects(Post::class.java)
                liveData.postValue(posts)
            }

        return liveData
    }

    /** Update Likes for a post */
    suspend fun updateLikes(postId: String) {
        val currentUserId = auth.currentUser?.uid ?: return
        val post = postCollections.document(postId).get().await().toObject(Post::class.java) ?: return

        if (post.likedBy.contains(currentUserId)) {
            post.likedBy.remove(currentUserId)
        } else {
            post.likedBy.add(currentUserId)
        }
        postCollections.document(postId).set(post).await()
    }
}
