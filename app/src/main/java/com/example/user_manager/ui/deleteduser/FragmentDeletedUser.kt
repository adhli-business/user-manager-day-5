package com.example.user_manager.ui.deleteduser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.user_manager.R
import kotlinx.coroutines.launch

class FragmentDeletedUser : Fragment() {
    private val viewModel: DeletedUserViewModel by viewModels()
    private lateinit var adapter: DeletedUserAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_deleted_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = DeletedUserAdapter { user ->
            viewModel.restoreUser(user)
        }

        view?.findViewById<RecyclerView>(R.id.rvDeletedUsers)?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@FragmentDeletedUser.adapter
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.deletedUsers.collect { users ->
                adapter.submitList(users)
                view?.findViewById<TextView>(R.id.tvEmptyState)?.visibility =
                    if (users.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }
}
