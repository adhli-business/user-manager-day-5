package com.example.user_manager.ui.deleteduser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.user_manager.R
import com.example.user_manager.data.models.User
import com.example.user_manager.ui.userlist.UserListAdapter
import de.hdodenhof.circleimageview.CircleImageView

class DeletedUserAdapter(private val onRestoreClick: (User) -> Unit) :
    ListAdapter<User, DeletedUserAdapter.DeletedUserViewHolder>(UserListAdapter.UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeletedUserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_deleted_user, parent, false)
        return DeletedUserViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeletedUserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DeletedUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
        private val btnRestore: View = itemView.findViewById(R.id.btnRestore)
        private val ivProfile: CircleImageView = itemView.findViewById(R.id.ivProfile)

        fun bind(user: User) {
            tvName.text = "${user.firstName} ${user.lastName}"
            tvEmail.text = user.email

            Glide.with(itemView.context)
                .load(user.image)
                .into(ivProfile)

            btnRestore.setOnClickListener {
                onRestoreClick(user)
            }
        }
    }
}
