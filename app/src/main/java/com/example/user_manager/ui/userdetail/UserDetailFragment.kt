package com.example.user_manager.ui.userdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.user_manager.R
import com.example.user_manager.data.model.User
import com.example.user_manager.databinding.FragmentUserDetailBinding
import com.google.android.material.snackbar.Snackbar

class UserDetailFragment : Fragment(), MenuProvider {
    private var _binding: FragmentUserDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserDetailViewModel by viewModels()
    private val args: UserDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        setupObservers()
        viewModel.loadUser(args.userId)
    }

    private fun setupObservers() {
        viewModel.user.observe(viewLifecycleOwner) { user ->
            updateUI(user)
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun updateUI(user: User) {
        binding.apply {
            textViewName.text = "${user.firstName} ${user.lastName}"
            textViewEmail.text = user.email
            textViewPhone.text = user.phone
            textViewGender.text = "Gender: ${user.gender}"
            textViewAge.text = "${user.age} years old"

            textViewCompany.text = """
                Company: ${user.company.name}
                Title: ${user.company.title}
                Department: ${user.company.department}
            """.trimIndent()

            textViewAddress.text = """
                ${user.address.address}
                ${user.address.city}, ${user.address.state} ${user.address.postalCode}
            """.trimIndent()

            Glide.with(imageViewUser)
                .load(user.image)
                .circleCrop()
                .into(imageViewUser)
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_user_detail, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_edit -> {
                findNavController().navigate(
                    UserDetailFragmentDirections.actionUserDetailToUserEdit(args.userId)
                )
                true
            }
            else -> false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
