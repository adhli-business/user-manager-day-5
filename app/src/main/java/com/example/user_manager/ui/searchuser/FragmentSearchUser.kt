package com.example.user_manager.ui.searchuser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.user_manager.R
import com.example.user_manager.ui.auth.LoginActivity
import com.example.user_manager.ui.userlist.UserListAdapter
import kotlinx.coroutines.launch

class FragmentSearchUser : Fragment() {
    private val viewModel: SearchUserViewModel by viewModels()
    private lateinit var adapter: UserListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Penting untuk menampilkan menu (logout)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(view)
        setupSearch(view)
        observeViewModel()
    }

    private fun setupRecyclerView(view: View) {
        adapter = UserListAdapter { user ->
            val bundle = Bundle().apply {
                putInt("userId", user.id)
            }
            findNavController().navigate(R.id.action_searchUser_to_userDetail, bundle)
        }

        view.findViewById<RecyclerView>(R.id.rvSearchResults).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@FragmentSearchUser.adapter
        }
    }

    private fun setupSearch(view: View) {
        view.findViewById<EditText>(R.id.etSearch).doAfterTextChanged { text ->
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

    // Menampilkan menu logout di AppBar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_app_bar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    // Aksi saat menu dipilih
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                performLogout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Fungsi logout: hapus sesi & pindah ke LoginActivity
    private fun performLogout() {
        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()

        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish() // penting agar MainActivity tidak tersisa
    }
}
