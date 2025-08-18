package com.example.shoutout

import androidx.lifecycle.LiveData
import com.example.shoutout.models.Post
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject

/**
 * LiveData wrapper around Firestore Query to provide real-time updates.
 */
class FirestoreLiveData(private val query: Query) : LiveData<List<Post>>() {

    private var listenerRegistration = query.addSnapshotListener { snapshot, _ ->
        if (snapshot != null) {
            val posts = snapshot.documents.mapNotNull { doc ->
                val post = doc.toObject<Post>()
                post?.apply { postId = doc.id }
            }
            value = posts
        }
    }

    override fun onInactive() {
        super.onInactive()
        listenerRegistration.remove()
    }
}
