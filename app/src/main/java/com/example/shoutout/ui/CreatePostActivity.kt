package com.example.shoutout.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.shoutout.R
import com.example.shoutout.viewmodels.PostViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CreatePostActivity : AppCompatActivity() {

    private lateinit var postButton: Button
    private lateinit var postInput: EditText
    private val auth = Firebase.auth

    // Use ViewModel instead of directly calling DAO
    private val postViewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        val currentUserId = auth.currentUser!!.uid
        postButton = findViewById(R.id.postButton)
        postInput = findViewById(R.id.postInput)

        postButton.setOnClickListener {
            val input = postInput.text.toString().trim()
            if (input.isNotEmpty()) {
                postViewModel.addPost(input, currentUserId) // ✅ call via ViewModel
                finish()
            }
        }
    }
}
