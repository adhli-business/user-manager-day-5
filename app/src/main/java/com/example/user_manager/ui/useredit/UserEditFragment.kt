package com.example.user_manager.ui.useredit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.user_manager.data.model.User
import com.example.user_manager.databinding.FragmentUserFormBinding
import com.google.android.material.snackbar.Snackbar

class UserEditFragment : Fragment() {
    private var _binding: FragmentUserFormBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserEditViewModel by viewModels()
    private val args: UserEditFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpinner()
        setupListeners()
        setupObservers()
        viewModel.loadUser(args.userId)
    }

    private fun setupSpinner() {
        val genders = arrayOf("male", "female", "other")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, genders)
        binding.spinnerGender.setAdapter(adapter)
    }

    private fun setupListeners() {
        binding.buttonSave.setOnClickListener {
            val firstName = binding.editTextFirstName.text.toString()
            val lastName = binding.editTextLastName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val gender = binding.spinnerGender.text.toString()

            if (validateInput(firstName, lastName, email, gender)) {
                viewModel.updateUser(args.userId, firstName, lastName, email, gender)
            }
        }
    }

    private fun setupObservers() {
        viewModel.user.observe(viewLifecycleOwner) { user ->
            populateFields(user)
        }

        viewModel.success.observe(viewLifecycleOwner) { success ->
            if (success) {
                findNavController().navigateUp()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun populateFields(user: User) {
        binding.apply {
            editTextFirstName.setText(user.firstName)
            editTextLastName.setText(user.lastName)
            editTextEmail.setText(user.email)
            editTextPhone.setText(user.phone)
            spinnerGender.setText(user.gender, false)
        }
    }

    private fun validateInput(firstName: String, lastName: String, email: String, gender: String): Boolean {
        if (firstName.isBlank()) {
            binding.firstNameLayout.error = "First name is required"
            return false
        }
        if (lastName.isBlank()) {
            binding.lastNameLayout.error = "Last name is required"
            return false
        }
        if (email.isBlank()) {
            binding.emailLayout.error = "Email is required"
            return false
        }
        if (gender.isBlank()) {
            binding.genderLayout.error = "Gender is required"
            return false
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
