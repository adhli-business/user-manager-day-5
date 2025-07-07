package com.example.user_manager.ui.base

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.user_manager.di.DependencyProvider
import com.example.user_manager.ui.deleted.DeletedUserViewModel
import com.example.user_manager.ui.profile.UserProfileViewModel
import com.example.user_manager.ui.search.SearchUserViewModel
import com.example.user_manager.ui.statistics.UserStatisticsViewModel
import com.example.user_manager.ui.useradd.UserAddViewModel
import com.example.user_manager.ui.userdetail.UserDetailViewModel
import com.example.user_manager.ui.useredit.UserEditViewModel
import com.example.user_manager.ui.userlist.UserListViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(UserListViewModel::class.java) ->
                UserListViewModel(DependencyProvider.provideUserRepository()) as T

            modelClass.isAssignableFrom(UserDetailViewModel::class.java) ->
                UserDetailViewModel(DependencyProvider.provideUserRepository()) as T

            modelClass.isAssignableFrom(UserAddViewModel::class.java) ->
                UserAddViewModel(DependencyProvider.provideUserRepository()) as T

            modelClass.isAssignableFrom(UserEditViewModel::class.java) ->
                UserEditViewModel(DependencyProvider.provideUserRepository()) as T

            modelClass.isAssignableFrom(SearchUserViewModel::class.java) ->
                SearchUserViewModel(DependencyProvider.provideUserRepository()) as T

            modelClass.isAssignableFrom(UserStatisticsViewModel::class.java) ->
                UserStatisticsViewModel(DependencyProvider.provideUserRepository()) as T

            modelClass.isAssignableFrom(UserProfileViewModel::class.java) ->
                UserProfileViewModel(
                    DependencyProvider.provideUserRepository(),
                    DependencyProvider.provideSessionManager(context)
                ) as T

            modelClass.isAssignableFrom(DeletedUserViewModel::class.java) ->
                DeletedUserViewModel() as T

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
