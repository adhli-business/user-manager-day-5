package com.example.user_manager.ui.useredit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.user_manager.R
import com.example.user_manager.data.models.User
import com.example.user_manager.data.models.UserRequest
import kotlinx.coroutines.launch

class FragmentUserEdit : Fragment() {
    private val viewModel: UserEditViewModel by viewModels()
    private var userId: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = arguments?.getInt("userId") ?: -1

        setupViews()
        observeViewModel()

        if (userId != -1) {
            viewModel.loadUser(userId)
        }
    }

    private fun setupViews() {
        view?.findViewById<View>(R.id.btnSave)?.setOnClickListener {
            saveUser()
        }
    }

    private fun saveUser() {
        val firstName = view?.findViewById<EditText>(R.id.etFirstName)?.text.toString()
        val lastName = view?.findViewById<EditText>(R.id.etLastName)?.text.toString()
        val email = view?.findViewById<EditText>(R.id.etEmail)?.text.toString()
        val gender = view?.findViewById<Spinner>(R.id.spGender)?.selectedItem.toString()
        val age = view?.findViewById<EditText>(R.id.etAge)?.text.toString()  // sudah string
        val phone = view?.findViewById<EditText>(R.id.etPhone)?.text.toString()

        if (firstName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty()) {
            val userRequest = UserRequest(firstName, lastName, email, gender, age, phone)
            viewModel.updateUser(userId, userRequest)
        } else {
            Toast.makeText(context, "Please fill required fields", Toast.LENGTH_SHORT).show()
        }
    }


    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.user.collect { user ->
                user?.let { populateFields(it) }
            }
        }

        lifecycleScope.launch {
            viewModel.updateSuccess.collect { success ->
                if (success) {
                    Toast.makeText(context, "User updated successfully", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun populateFields(user: User) {
        view?.apply {
            findViewById<EditText>(R.id.etFirstName).setText(user.firstName)
            findViewById<EditText>(R.id.etLastName).setText(user.lastName)
            findViewById<EditText>(R.id.etEmail).setText(user.email)
            findViewById<EditText>(R.id.etAge).setText(user.age.toString())
            findViewById<EditText>(R.id.etPhone).setText(user.phone)
        }
    }
}