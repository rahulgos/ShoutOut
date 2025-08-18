package com.example.shoutout.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoutout.R
import com.example.shoutout.adapters.PostAdapter
import com.example.shoutout.models.Post
import com.example.shoutout.viewmodels.PostViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), PostAdapter.IPostAdapter {

    private lateinit var adapter: PostAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var addPostButton: FloatingActionButton

    private val postViewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = PostAdapter(emptyList(), this)
        recyclerView.adapter = adapter

        addPostButton = findViewById(R.id.addPostButton)
        addPostButton.setOnClickListener {
            startActivity(Intent(this, CreatePostActivity::class.java))
        }

        observePosts()
    }

    private fun observePosts() {
        postViewModel.allPosts.observe(this) { posts ->
            adapter.updateData(posts)
            if (posts.isNotEmpty()) {
                recyclerView.scrollToPosition(0) // safe scroll
            }
        }
    }

    override fun onLikeClicked(postId: String) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        // Optimistic UI update
        val updatedList = adapter.getPosts().map { post ->
            if (post.postId == postId) {
                if (post.likedBy.contains(currentUserId)) {
                    post.likedBy.remove(currentUserId)
                } else {
                    post.likedBy.add(currentUserId)
                }
            }
            post
        }
        adapter.updateData(updatedList)

        // Update Firestore
        postViewModel.updateLikes(postId)
    }
}
