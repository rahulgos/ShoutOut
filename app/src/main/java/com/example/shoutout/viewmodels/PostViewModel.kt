package com.example.shoutout.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoutout.daos.PostDao
import com.example.shoutout.models.Post
import kotlinx.coroutines.launch

class PostViewModel(
    private val postDao: PostDao = PostDao()
) : ViewModel() {

    val allPosts: LiveData<List<Post>> = postDao.listenToPosts()

    fun addPost(text: String, currentUserId: String) {
        viewModelScope.launch {
            postDao.addPost(text, currentUserId)
        }
    }

    fun updateLikes(postId: String) {
        viewModelScope.launch {
            postDao.updateLikes(postId)
        }
    }
}
