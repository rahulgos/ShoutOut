package com.example.shoutout.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoutout.R
import com.example.shoutout.adapters.PostAdapter
import com.example.shoutout.models.Post
import com.example.shoutout.viewmodels.PostViewModel
import com.google.android.material.appbar.MaterialToolbar
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

        // Toolbar setup
        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "ShoutOut"

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
            if (posts.isNotEmpty()) recyclerView.scrollToPosition(0)
        }
    }

    override fun onLikeClicked(postId: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            Toast.makeText(this, "Please login to like posts", Toast.LENGTH_SHORT).show()
            return
        }

        val currentUserId = currentUser.uid
        val posts = adapter.getPosts().toMutableList()
        val index = posts.indexOfFirst { it.postId == postId }
        if (index != -1) {
            val post = posts[index]
            if (post.likedBy.contains(currentUserId)) post.likedBy.remove(currentUserId)
            else post.likedBy.add(currentUserId)
            adapter.updateData(posts)
        }

        postViewModel.updateLikes(postId, currentUserId)
    }

    override fun onEditClicked(post: Post) {
        val intent = Intent(this, CreatePostActivity::class.java).apply {
            putExtra("postId", post.postId)
            putExtra("postText", post.text)
        }
        startActivity(intent)
    }

    override fun onDeleteClicked(post: Post) {
        AlertDialog.Builder(this)
            .setTitle("Delete Post")
            .setMessage("Are you sure you want to delete this post?")
            .setPositiveButton("Delete") { _, _ ->
                postViewModel.deletePost(post.postId)
                Toast.makeText(this, "Post deleted successfully", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // Inflate toolbar menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // Handle toolbar menu clicks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                showLogoutDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to sign out?")
            .setPositiveButton("Yes") { _, _ ->
                FirebaseAuth.getInstance().signOut()
                Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, SignInActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
