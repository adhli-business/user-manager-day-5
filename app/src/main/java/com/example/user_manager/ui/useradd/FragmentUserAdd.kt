package com.example.user_manager.ui.useradd

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.user_manager.R
import com.example.user_manager.data.models.UserRequest
import kotlinx.coroutines.launch

class FragmentUserAdd : Fragment() {
    private val viewModel: UserAddViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpinner(view)
        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        view?.findViewById<View>(R.id.btnSave)?.setOnClickListener {
            saveUser()
        }
    }

    private fun setupSpinner(view: View) {
        val spinner = view.findViewById<Spinner>(R.id.spGender)
        val genderOptions = listOf("Male", "Female", "Other")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, genderOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun saveUser() {
        val firstName = view?.findViewById<EditText>(R.id.etFirstName)?.text.toString()
        val lastName = view?.findViewById<EditText>(R.id.etLastName)?.text.toString()
        val email = view?.findViewById<EditText>(R.id.etEmail)?.text.toString()
        val gender = view?.findViewById<Spinner>(R.id.spGender)?.selectedItem.toString()
        val age = view?.findViewById<EditText>(R.id.etAge)?.text.toString() // keep as String
        val phone = view?.findViewById<EditText>(R.id.etPhone)?.text.toString()

        if (firstName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty()) {
            val userRequest = UserRequest(
                firstName = firstName,
                lastName = lastName,
                email = email,
                gender = gender,
                age = age, // as String?
                phone = phone
            )
            viewModel.addUser(userRequest)
        } else {
            Toast.makeText(context, "Please fill required fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.addSuccess.collect { success ->
                if (success) {
                    Toast.makeText(context, "User added successfully", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        }
    }
}
