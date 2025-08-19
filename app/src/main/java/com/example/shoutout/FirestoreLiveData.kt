package com.example.shoutout

import androidx.lifecycle.LiveData
import com.example.shoutout.models.Post
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject

/**
 * LiveData wrapper around a Firestore Query to provide real-time updates of Posts.
 */
class FirestoreLiveData(private val query: Query) : LiveData<List<Post>>() {

    private val listenerRegistration = query.addSnapshotListener { snapshot, _ ->
        if (snapshot != null) {
            val posts = snapshot.documents.mapNotNull { doc ->
                doc.toObject<Post>()?.apply { postId = doc.id }
            }
            value = posts
        }
    }

    override fun onInactive() {
        super.onInactive()
        listenerRegistration.remove()
    }
}
