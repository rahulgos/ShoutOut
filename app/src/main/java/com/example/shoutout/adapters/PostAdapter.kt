package com.example.shoutout.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shoutout.R
import com.example.shoutout.Utils
import com.example.shoutout.models.Post
import com.google.firebase.auth.FirebaseAuth

class PostAdapter(
    private var postList: List<Post>,
    private val listener: IPostAdapter
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    interface IPostAdapter {
        fun onLikeClicked(postId: String)
        fun onEditClicked(post: Post)
        fun onDeleteClicked(post: Post)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun getItemCount() = postList.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]

        // Text bindings
        holder.postText.text = post.text
        holder.userText.text = post.createdBy.displayName
        holder.createdAt.text = Utils.getTimeAgo(post.createdAt)
        holder.likeCount.text = post.likedBy.size.toString()

        // Load profile image
        Glide.with(holder.userImage.context)
            .load(post.createdBy.imageUrl)
            .placeholder(R.drawable.ic_person)
            .error(R.drawable.ic_person)
            .circleCrop()
            .into(holder.userImage)

        // Like state
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        holder.likeButton.setImageResource(
            if (userId != null && post.likedBy.contains(userId))
                R.drawable.liked
            else
                R.drawable.unliked
        )

        holder.likeButton.setOnClickListener { listener.onLikeClicked(post.postId) }

        // Owner-only visibility for Edit/Delete
        val isOwner = post.createdBy.uid == userId
        holder.editButton.visibility = if (isOwner) View.VISIBLE else View.GONE
        holder.deleteButton.visibility = if (isOwner) View.VISIBLE else View.GONE

        holder.editButton.setOnClickListener { listener.onEditClicked(post) }
        holder.deleteButton.setOnClickListener { listener.onDeleteClicked(post) }
    }

    fun updateData(newList: List<Post>) {
        postList = newList
        notifyDataSetChanged()
    }

    fun getPosts(): List<Post> = postList

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postText: TextView = itemView.findViewById(R.id.postTitle)
        val userText: TextView = itemView.findViewById(R.id.userName)
        val createdAt: TextView = itemView.findViewById(R.id.createdAt)
        val likeCount: TextView = itemView.findViewById(R.id.likeCount)
        val userImage: ImageView = itemView.findViewById(R.id.userImage)
        val likeButton: ImageView = itemView.findViewById(R.id.likeButton)
        val editButton: ImageView = itemView.findViewById(R.id.editButton)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }
}
