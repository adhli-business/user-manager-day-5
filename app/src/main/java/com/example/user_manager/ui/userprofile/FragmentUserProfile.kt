package com.example.user_manager.ui.userprofile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.user_manager.R
import com.example.user_manager.data.models.User
import com.example.user_manager.ui.auth.LoginActivity
import com.example.user_manager.utils.PreferencesHelper
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch

class FragmentUserProfile : Fragment() {
    private val viewModel: UserProfileViewModel by viewModels()
    private lateinit var preferencesHelper: PreferencesHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferencesHelper = PreferencesHelper(requireContext())

        setupViews()
        observeViewModel()

        val userId = preferencesHelper.getUserId()
        if (userId != -1) {
            viewModel.loadProfile(userId)
        }
    }

    private fun setupViews() {
        view?.findViewById<View>(R.id.btnLogout)?.setOnClickListener {
            logout()
        }

        view?.findViewById<View>(R.id.btnEditProfile)?.setOnClickListener {
            // Navigate to edit profile
        }
    }

    private fun logout() {
        preferencesHelper.clearPreferences()
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.user.collect { user ->
                user?.let { updateProfileUI(it) }
            }
        }
    }

    private fun updateProfileUI(user: User) {
        view?.apply {
            findViewById<TextView>(R.id.tvName).text = "${user.firstName} ${user.lastName}"
            findViewById<TextView>(R.id.tvEmail).text = user.email
            findViewById<TextView>(R.id.tvGender).text = user.gender
            findViewById<TextView>(R.id.tvAge).text = "${user.age} years old"
            findViewById<TextView>(R.id.tvPhone).text = user.phone

            user.address?.let { address ->
                findViewById<TextView>(R.id.tvAddress).text =
                    "${address.address}, ${address.city}, ${address.state}"
            }

            Glide.with(this@FragmentUserProfile)
                .load(user.image)
                .into(findViewById<CircleImageView>(R.id.ivProfile))
        }
    }
}