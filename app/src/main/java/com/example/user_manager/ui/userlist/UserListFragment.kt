package com.example.user_manager.ui.userlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.user_manager.databinding.FragmentUserListBinding
import com.example.user_manager.ui.base.BaseFragment
import com.example.user_manager.ui.base.BaseViewModel
import com.example.user_manager.ui.base.LoadingStateAdapter
import com.example.user_manager.ui.base.PaginationScrollListener
import com.example.user_manager.ui.base.ViewModelFactory
import com.example.user_manager.ui.common.SwipeToDeleteCallback
import com.example.user_manager.utils.DialogUtils.showConfirmationDialog
import com.example.user_manager.utils.DialogUtils.showUserActionsBottomSheet
import com.example.user_manager.utils.DialogUtils.UserAction
import com.example.user_manager.utils.TransitionUtils.beginEmptyStateTransition
import com.example.user_manager.utils.TransitionUtils.beginLoadingStateTransition
import com.example.user_manager.utils.hide
import com.example.user_manager.utils.setupAnimations
import com.example.user_manager.utils.show
import com.example.user_manager.utils.showErrorSnackbar
import com.example.user_manager.utils.showSuccessSnackbar
import com.example.user_manager.utils.showUndoSnackbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class UserListFragment : BaseFragment() {
    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserListViewModel by viewModels {
        ViewModelFactory(requireContext())
    }

    private lateinit var adapter: UserListAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupFab()
        setupObservers()
        setupSwipeRefresh()
    }

    private fun setupRecyclerView() {
        adapter = UserListAdapter(
            onUserClick = { user ->
                showUserActions(user)
            },
            onDeleteClick = { user ->
                showDeleteConfirmation(user.id)
            }
        )

        layoutManager = LinearLayoutManager(requireContext())

        binding.recyclerView.apply {
            this.layoutManager = this@UserListFragment.layoutManager
            adapter = this@UserListFragment.adapter.withLoadStateFooter(
                footer = LoadingStateAdapter { viewModel.loadUsers() }
            )
            setupAnimations() // Apply our custom animations
            addOnScrollListener(object : PaginationScrollListener(this@UserListFragment.layoutManager) {
                override fun loadMoreItems() {
                    if (!binding.swipeRefreshLayout.isRefreshing) {
                        viewModel.loadUsers()
                    }
                }

                override fun isLastPage(): Boolean =
                    viewModel.isLastPage.value ?: false

                override fun isLoading(): Boolean =
                    viewModel.isLoading()
            })

            // Add swipe-to-delete functionality
            val swipeHandler = SwipeToDeleteCallback(requireContext()) { position ->
                val user = adapter.currentList[position]
                showDeleteConfirmation(user.id)
            }
            ItemTouchHelper(swipeHandler).attachToRecyclerView(this)
        }
    }

    private fun showUserActions(user: User) {
        val actions = listOf(
            UserAction.Edit,
            UserAction.Delete
        )

        showUserActionsBottomSheet(
            title = "${user.firstName} ${user.lastName}",
            actions = actions
        ) { action ->
            when (action) {
                UserAction.Edit -> {
                    findNavController().navigate(
                        UserListFragmentDirections.actionUserListToUserDetail(user.id)
                    )
                }
                UserAction.Delete -> showDeleteConfirmation(user.id)
                else -> Unit
            }
        }
    }

    private fun showDeleteConfirmation(userId: Int) {
        showConfirmationDialog(
            title = getString(R.string.dialog_delete_title),
            message = getString(R.string.dialog_delete_message),
            onConfirm = { viewModel.deleteUser(userId) }
        )
    }

    private fun setupObservers() {
        viewModel.users.observe(viewLifecycleOwner) { users ->
            beginEmptyStateTransition(binding.root as ViewGroup)
            adapter.submitList(users)
            binding.swipeRefreshLayout.isRefreshing = false
            binding.textViewEmpty.visibility = if (users.isEmpty()) View.VISIBLE else View.GONE
            binding.recyclerView.visibility = if (users.isEmpty()) View.GONE else View.VISIBLE
        }

        viewModel.isLastPage.observe(viewLifecycleOwner) { isLastPage ->
            if (isLastPage && adapter.currentList.isNotEmpty()) {
                showSuccessSnackbar(R.string.message_all_users_loaded)
            }
        }

        viewModel.deletedUser.observe(viewLifecycleOwner) { user ->
            user?.let {
                showUndoSnackbar(
                    message = getString(R.string.message_user_deleted, user.firstName),
                    onUndo = { viewModel.undoDelete(user) },
                    anchor = binding.fabAddUser
                )
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                showErrorSnackbar(
                    messageRes = R.string.error_loading_users,
                    actionText = getString(R.string.action_retry),
                    action = { viewModel.loadUsers() }
                )
            }
        }
    }

    private fun setupFab() {
        binding.fab.setOnClickListener {
            findNavController().navigate(UserListFragmentDirections.actionUserListToUserDetail(0))
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshUsers()
        }
    }

    override fun handleLoading(isLoading: Boolean) {
        beginLoadingStateTransition(binding.root as ViewGroup)
        if (isLoading) {
            binding.loadingView.showLoading()
        } else {
            binding.loadingView.hideLoading()
        }
    }

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
