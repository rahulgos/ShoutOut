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

/**
 * RecyclerView Adapter to bind Post data to item_post layout.
 */
class PostAdapter(
    private var postList: List<Post>,
    private val listener: IPostAdapter
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    interface IPostAdapter {
        fun onLikeClicked(postId: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun getItemCount() = postList.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        holder.postText.text = post.text
        holder.userText.text = post.createdBy.displayName
        holder.createdAt.text = Utils.getTimeAgo(post.createdAt)
        holder.likeCount.text = post.likedBy.size.toString()

        Glide.with(holder.userImage.context)
            .load(post.createdBy.imageUrl)
            .circleCrop()
            .into(holder.userImage)

        holder.likeButton.setImageResource(
            if (userId != null && post.likedBy.contains(userId))
                R.drawable.liked else R.drawable.unliked
        )

        holder.likeButton.setOnClickListener {
            listener.onLikeClicked(post.postId)
        }
    }

    fun updateData(newList: List<Post>) {
        postList = newList
        notifyDataSetChanged()
    }

    fun getPosts() = postList

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postText: TextView = itemView.findViewById(R.id.postTitle)
        val userText: TextView = itemView.findViewById(R.id.userName)
        val createdAt: TextView = itemView.findViewById(R.id.createdAt)
        val likeCount: TextView = itemView.findViewById(R.id.likeCount)
        val userImage: ImageView = itemView.findViewById(R.id.userImage)
        val likeButton: ImageView = itemView.findViewById(R.id.likeButton)
    }
}

