package com.example.shoutout.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoutout.daos.UserDao
import com.example.shoutout.models.User
import kotlinx.coroutines.launch

/**
 * ViewModel for User operations.
 */
class UserViewModel(
    private val userDao: UserDao = UserDao()
) : ViewModel() {

    /** Add a new user to Firestore */
    fun addUser(user: User) = viewModelScope.launch {
        userDao.addUser(user)
    }

    /** Get a user by UID and return via callback */
    fun getUserById(uId: String, onResult: (User?) -> Unit) = viewModelScope.launch {
        val snapshot = userDao.getUserById(uId)
        val user = snapshot.toObject(User::class.java)
        onResult(user)
    }
}
