package com.example.user_manager.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBaseObservers()
    }

    private fun setupBaseObservers() {
        getViewModel()?.let { viewModel ->
            viewModel.error.observe(viewLifecycleOwner) { error ->
                error?.let { showError(it) }
            }

            viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                handleLoading(isLoading)
            }
        }
    }

    protected open fun showError(message: String) {
        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_LONG).show()
        }
    }

    protected open fun handleLoading(isLoading: Boolean) {
        // Override in subclasses to handle loading state
    }

    protected abstract fun getViewModel(): BaseViewModel?
}
