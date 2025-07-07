package com.example.user_manager.ui.userlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.user_manager.R
import kotlinx.coroutines.launch

class FragmentUserList : Fragment() {
    private val viewModel: UserListViewModel by viewModels()
    private lateinit var adapter: UserListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()

        view.findViewById<View>(R.id.fabAddUser).setOnClickListener {
            findNavController().navigate(R.id.action_userList_to_userAdd)
        }
    }

    private fun setupRecyclerView() {
        adapter = UserListAdapter { user ->
            val bundle = Bundle().apply {
                putInt("userId", user.id)
            }
            findNavController().navigate(R.id.action_userList_to_userDetail, bundle)
        }

        view?.findViewById<RecyclerView>(R.id.rvUsers)?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@FragmentUserList.adapter
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.users.collect { users ->
                adapter.submitList(users)
            }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                view?.findViewById<View>(R.id.progressBar)?.visibility =
                    if (isLoading) View.VISIBLE else View.GONE
            }
        }
    }
}
