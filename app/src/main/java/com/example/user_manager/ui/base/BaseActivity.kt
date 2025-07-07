package com.example.user_manager.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.user_manager.UserManagerApplication
import com.example.user_manager.R
import com.google.android.material.snackbar.Snackbar

abstract class BaseActivity : AppCompatActivity() {
    private var networkSnackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeNetworkState()
    }

    private fun observeNetworkState() {
        (application as UserManagerApplication).networkStateHandler.isNetworkAvailable
            .observe(this) { isAvailable ->
                if (!isAvailable) {
                    showNoNetworkWarning()
                } else {
                    hideNoNetworkWarning()
                }
            }
    }

    private fun showNoNetworkWarning() {
        if (networkSnackbar == null) {
            networkSnackbar = Snackbar.make(
                findViewById(android.R.id.content),
                R.string.error_no_internet,
                Snackbar.LENGTH_INDEFINITE
            )
        }
        networkSnackbar?.show()
    }

    private fun hideNoNetworkWarning() {
        networkSnackbar?.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        networkSnackbar = null
    }
}
