package com.example.user_manager.utils

import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.user_manager.R
import com.google.android.material.snackbar.Snackbar

object SnackbarUtils {
    sealed class SnackbarState(@ColorRes val backgroundColor: Int) {
        object Success : SnackbarState(R.color.success)
        object Error : SnackbarState(R.color.error)
        object Warning : SnackbarState(R.color.warning)
        object Info : SnackbarState(R.color.info)
    }

    fun Fragment.showSnackbar(
        message: String,
        duration: Int = Snackbar.LENGTH_SHORT,
        state: SnackbarState = SnackbarState.Info,
        anchor: View? = null,
        actionText: String? = null,
        action: (() -> Unit)? = null
    ) {
        view?.let { view ->
            Snackbar.make(view, message, duration).apply {
                setBackgroundTint(ContextCompat.getColor(requireContext(), state.backgroundColor))
                setTextColor(ContextCompat.getColor(requireContext(), R.color.text_white))

                if (actionText != null && action != null) {
                    setActionTextColor(ContextCompat.getColor(requireContext(), R.color.text_white))
                    setAction(actionText) { action() }
                }

                anchor?.let { setAnchorView(it) }
            }.show()
        }
    }

    fun Fragment.showSuccessSnackbar(
        @StringRes messageRes: Int,
        duration: Int = Snackbar.LENGTH_SHORT,
        anchor: View? = null
    ) {
        showSnackbar(
            message = getString(messageRes),
            duration = duration,
            state = SnackbarState.Success,
            anchor = anchor
        )
    }

    fun Fragment.showErrorSnackbar(
        @StringRes messageRes: Int,
        duration: Int = Snackbar.LENGTH_LONG,
        anchor: View? = null,
        actionText: String? = null,
        action: (() -> Unit)? = null
    ) {
        showSnackbar(
            message = getString(messageRes),
            duration = duration,
            state = SnackbarState.Error,
            anchor = anchor,
            actionText = actionText,
            action = action
        )
    }

    fun Fragment.showUndoSnackbar(
        message: String,
        onUndo: () -> Unit,
        anchor: View? = null
    ) {
        showSnackbar(
            message = message,
            duration = Snackbar.LENGTH_LONG,
            state = SnackbarState.Info,
            anchor = anchor,
            actionText = getString(R.string.action_undo),
            action = onUndo
        )
    }
}
