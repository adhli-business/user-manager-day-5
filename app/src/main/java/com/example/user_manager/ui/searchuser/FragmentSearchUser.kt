package com.example.user_manager.ui.searchuser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.user_manager.R
import com.example.user_manager.ui.userlist.UserListAdapter
import kotlinx.coroutines.launch

class FragmentSearchUser : Fragment() {
    private val viewModel: SearchUserViewModel by viewModels()
    private lateinit var adapter: UserListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearch()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = UserListAdapter { user ->
            val bundle = Bundle().apply {
                putInt("userId", user.id)
            }
            findNavController().navigate(R.id.action_searchUser_to_userDetail, bundle)
        }

        view?.findViewById<RecyclerView>(R.id.rvSearchResults)?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@FragmentSearchUser.adapter
        }
    }

    private fun setupSearch() {
        view?.findViewById<EditText>(R.id.etSearch)?.doAfterTextChanged { text ->
            val query = text.toString().trim()
            if (query.isNotEmpty()) {
                viewModel.searchUsers(query)
            } else {
                viewModel.clearSearch()
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.searchResults.collect { users ->
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