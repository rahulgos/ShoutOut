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

    /** Add User */
    fun addUser(user: User) {
        viewModelScope.launch {
            userDao.addUser(user)
        }
    }

    /** Get User by Id (returns via callback to UI) */
    fun getUserById(uId: String, onResult: (User?) -> Unit) {
        viewModelScope.launch {
            val snapshot = userDao.getUserById(uId)
            val user = snapshot.toObject(User::class.java)
            onResult(user)
        }
    }
}
