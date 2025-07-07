package com.example.user_manager.ui.deleted

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.user_manager.data.model.User
import com.example.user_manager.ui.base.BaseViewModel
import com.example.user_manager.utils.EventBus
import com.example.user_manager.utils.UserEvent

class DeletedUserViewModel(application: Application) : BaseViewModel(application) {
    private val _deletedUsers = MutableLiveData<List<User>>()
    val deletedUsers: LiveData<List<User>> = _deletedUsers

    private val deletedUsersList = mutableListOf<User>()

    private val eventListener: (UserEvent) -> Unit = { event ->
        when (event) {
            is UserEvent.UserDeleted -> {
                deletedUsersList.add(event.user)
                _deletedUsers.value = deletedUsersList.toList()
            }
            is UserEvent.UserRestored -> {
                deletedUsersList.removeAll { it.id == event.userId }
                _deletedUsers.value = deletedUsersList.toList()
            }
        }
    }

    init {
        EventBus.subscribe(eventListener)
    }

    fun restoreUser(userId: Int) {
        try {
            val userToRestore = deletedUsersList.find { it.id == userId }
            if (userToRestore != null) {
                deletedUsersList.remove(userToRestore)
                _deletedUsers.value = deletedUsersList.toList()
                // Broadcast restoration event
                EventBus.publish(UserEvent.UserRestored(userId))
            } else {
                showError("User not found")
            }
        } catch (e: Exception) {
            showError(e)
        }
    }

    fun clearDeletedUsers() {
        deletedUsersList.clear()
        _deletedUsers.value = emptyList()
    }

    override fun onCleared() {
        super.onCleared()
        EventBus.unsubscribe(eventListener)
    }
}
