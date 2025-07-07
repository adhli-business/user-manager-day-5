package com.example.user_manager.utils

sealed class UserEvent {
    data class UserDeleted(val user: com.example.user_manager.data.model.User) : UserEvent()
    data class UserRestored(val userId: Int) : UserEvent()
}

object EventBus {
    private val listeners = mutableListOf<(UserEvent) -> Unit>()

    fun subscribe(listener: (UserEvent) -> Unit) {
        listeners.add(listener)
    }

    fun unsubscribe(listener: (UserEvent) -> Unit) {
        listeners.remove(listener)
    }

    fun publish(event: UserEvent) {
        listeners.forEach { it(event) }
    }
}
