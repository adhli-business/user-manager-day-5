package com.example.user_manager.ui.userlist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.user_manager.R
import com.example.user_manager.ui.auth.LoginActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class FragmentUserList : Fragment() {
    private val viewModel: UserListViewModel by viewModels()
    private lateinit var adapter: UserListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView(view)
        observeViewModel()
        setupMenu()

        view.findViewById<FloatingActionButton>(R.id.fabAddUser).setOnClickListener {
            findNavController().navigate(R.id.action_userList_to_userAdd)
        }
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.top_app_bar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_logout -> {
                        performLogout()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun performLogout() {
        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()

        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    private fun setupRecyclerView(view: View) {
        adapter = UserListAdapter { user ->
            val bundle = Bundle().apply {
                putInt("userId", user.id)
            }
            findNavController().navigate(R.id.action_userList_to_userDetail, bundle)
        }

        view.findViewById<RecyclerView>(R.id.rvUsers).apply {
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
