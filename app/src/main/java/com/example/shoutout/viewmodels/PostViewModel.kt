package com.example.shoutout.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoutout.daos.PostDao
import com.example.shoutout.models.Post
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {

    private val postDao = PostDao()
    private var listenerRegistration: ListenerRegistration? = null

    private val _allPosts = MutableLiveData<List<Post>>()
    val allPosts: LiveData<List<Post>> get() = _allPosts

    init {
        listenToPosts()
    }

    private fun listenToPosts() {
        listenerRegistration = postDao.getAllPostsQuery()
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("PostViewModel", "Listen failed.", e)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val posts = snapshot.toObjects(Post::class.java)
                    // Set Firestore docId in postId
                    posts.forEachIndexed { index, post ->
                        post.postId = snapshot.documents[index].id
                    }
                    _allPosts.value = posts
                }
            }
    }

    fun addPost(text: String) {
        viewModelScope.launch {
            try {
                postDao.addPost(text)
            } catch (e: Exception) {
                Log.e("PostViewModel", "Error adding post", e)
            }
        }
    }

    fun updatePost(postId: String, text: String) {
        viewModelScope.launch {
            try {
                postDao.updatePost(postId, text)
            } catch (e: Exception) {
                Log.e("PostViewModel", "Error updating post", e)
            }
        }
    }

    fun deletePost(postId: String) {
        viewModelScope.launch {
            try {
                postDao.deletePost(postId)
            } catch (e: Exception) {
                Log.e("PostViewModel", "Error deleting post", e)
            }
        }
    }

    fun updateLikes(postId: String, currentUserId: String) {
        viewModelScope.launch {
            try {
                postDao.updateLikes(postId, currentUserId)
            } catch (e: Exception) {
                Log.e("PostViewModel", "Error updating likes", e)
            }
        }
    }



    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}
