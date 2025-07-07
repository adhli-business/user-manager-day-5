package com.example.user_manager.ui.userdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.user_manager.R
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch
import com.example.user_manager.data.models.User


class FragmentUserDetail : Fragment() {
    private val viewModel: UserDetailViewModel by viewModels()
    private var userId: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_detail, container, false)
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
        view?.findViewById<View>(R.id.btnEdit)?.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("userId", userId)
            }
            findNavController().navigate(R.id.action_userDetail_to_userEdit, bundle)
        }

        view?.findViewById<View>(R.id.btnDelete)?.setOnClickListener {
            viewModel.deleteUser(userId)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.user.collect { user ->
                user?.let { updateUI(it) }
            }
        }

        lifecycleScope.launch {
            viewModel.deleteSuccess.collect { success ->
                if (success) {
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun updateUI(user: User) {
        view?.apply {
            findViewById<TextView>(R.id.tvName).text = "${user.firstName} ${user.lastName}"
            findViewById<TextView>(R.id.tvEmail).text = user.email
            findViewById<TextView>(R.id.tvGender).text = user.gender
            findViewById<TextView>(R.id.tvAge).text = user.age.toString()
            findViewById<TextView>(R.id.tvPhone).text = user.phone
            findViewById<TextView>(R.id.tvBirthDate).text = user.birthDate

            user.address?.let { address ->
                findViewById<TextView>(R.id.tvAddress).text =
                    "${address.address}, ${address.city}, ${address.state}, ${address.country}"
            }

            Glide.with(this@FragmentUserDetail)
                .load(user.image)
                .into(findViewById<CircleImageView>(R.id.ivProfile))
        }
    }
}