package com.example.user_manager.ui.userlist

import com.example.user_manager.utils.ImageUtils.loadUserImage
import com.example.user_manager.utils.ItemAnimationUtils.addClickAnimation
import com.example.user_manager.utils.ItemAnimationUtils.animateAddition
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.user_manager.R
import com.example.user_manager.data.model.User
import com.example.user_manager.databinding.ItemUserBinding

class UserListAdapter(
    private val onUserClick: (User) -> Unit,
    private val onDeleteClick: (User) -> Unit
) : ListAdapter<User, UserListAdapter.UserViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class UserViewHolder(
        private val binding: ItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.addClickAnimation() // Add touch feedback animation
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onUserClick(getItem(position))
                }
            }

            binding.buttonDelete.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onDeleteClick(getItem(position))
                }
            }
        }

        fun bind(user: User) {
            binding.apply {
                root.animateAddition() // Animate item when it appears
                textViewName.text = "${user.firstName} ${user.lastName}"
                textViewEmail.text = user.email
                textViewCompany.text = user.company.name

                // Use our ImageUtils extension function
                imageViewUser.loadUserImage(user.image)
            }
        }
    }

    class UserDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}
