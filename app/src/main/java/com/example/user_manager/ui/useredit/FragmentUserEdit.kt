package com.example.user_manager.ui.useredit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.user_manager.R
import com.example.user_manager.data.models.User
import com.example.user_manager.data.models.UserRequest
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class FragmentUserEdit : Fragment() {
    private val viewModel: UserEditViewModel by viewModels()
    private var userId: Int = -1

    private lateinit var etName: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPhone: TextInputEditText
    private lateinit var etAge: TextInputEditText
    private lateinit var etBirthDate: TextInputEditText
    private lateinit var etAddress: TextInputEditText
    private lateinit var rbMale: RadioButton
    private lateinit var rbFemale: RadioButton
    private lateinit var rgGender: RadioGroup
    private lateinit var btnUpdate: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = arguments?.getInt("userId") ?: -1

        initViews(view)
        observeViewModel()

        btnUpdate.setOnClickListener {
            saveUser()
        }

        if (userId != -1) {
            viewModel.loadUser(userId)
        }
    }

    private fun initViews(view: View) {
        etName = view.findViewById(R.id.etName)
        etEmail = view.findViewById(R.id.etEmail)
        etPhone = view.findViewById(R.id.etPhone)
        etAge = view.findViewById(R.id.etAge)
        etBirthDate = view.findViewById(R.id.etBirthDate)
        etAddress = view.findViewById(R.id.etAddress)
        rgGender = view.findViewById(R.id.rgGender)
        rbMale = view.findViewById(R.id.rbMale)
        rbFemale = view.findViewById(R.id.rbFemale)
        btnUpdate = view.findViewById(R.id.btnUpdate)
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
        val fullName = "${user.firstName} ${user.lastName}".trim()
        etName.setText(fullName)
        etEmail.setText(user.email)
        etPhone.setText(user.phone)
        etAge.setText(user.age.toString())
        etBirthDate.setText(user.birthDate ?: "")
        etAddress.setText(user.address?.address ?: "")
        // HANYA pakai .address jika itu bertipe nested object

        when (user.gender?.lowercase()) {
            "male" -> rbMale.isChecked = true
            "female" -> rbFemale.isChecked = true
        }
    }

    private fun saveUser() {
        val fullName = etName.text.toString().trim()
        val nameParts = fullName.split(" ", limit = 2)
        val firstName = nameParts.getOrNull(0) ?: ""
        val lastName = nameParts.getOrNull(1) ?: ""

        val email = etEmail.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val age = etAge.text.toString().trim()
        val birthDate = etBirthDate.text.toString().trim()
        val address = etAddress.text.toString().trim()

        val gender = when {
            rbMale.isChecked -> "male"
            rbFemale.isChecked -> "female"
            else -> ""
        }

        if (firstName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty()) {
            val userRequest = UserRequest(
                firstName = firstName,
                lastName = lastName,
                email = email,
                gender = gender,
                age = age,
                phone = phone,
                birthDate = birthDate,
                address = address
            )
            viewModel.updateUser(userId, userRequest)
        } else {
            Toast.makeText(context, "Please fill required fields", Toast.LENGTH_SHORT).show()
        }
    }
}
