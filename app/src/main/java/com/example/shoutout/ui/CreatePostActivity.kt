package com.example.shoutout.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.shoutout.R
import com.example.shoutout.viewmodels.PostViewModel

class CreatePostActivity : AppCompatActivity() {

    private lateinit var postInput: EditText
    private lateinit var postButton: Button
    private val postViewModel: PostViewModel by viewModels()
    private var postId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        postInput = findViewById(R.id.postInput)
        postButton = findViewById(R.id.postButton)

        postId = intent.getStringExtra("postId")
        val postText = intent.getStringExtra("postText")

        // If editing existing post
        if (postId != null) {
            postInput.setText(postText)
            postButton.text = "Update Post"
        } else {
            postButton.text = "Create Post"
        }

        postButton.setOnClickListener {
            val text = postInput.text.toString().trim()
            if (text.isEmpty()) {
                Toast.makeText(this, "Post cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (postId == null) {
                postViewModel.addPost(text)
                Toast.makeText(this, "Post created", Toast.LENGTH_SHORT).show()
            } else {
                postViewModel.updatePost(postId!!, text)
                Toast.makeText(this, "Post updated", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }
}
