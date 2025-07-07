package com.example.user_manager.utils

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.google.android.material.snackbar.Snackbar

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun Fragment.showSnackbar(message: String, duration: Int = Snackbar.LENGTH_LONG) {
    view?.let {
        Snackbar.make(it, message, duration).show()
    }
}

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: (T) -> Unit) {
    observe(lifecycleOwner) { value ->
        observer(value)
        removeObservers(lifecycleOwner)
    }
}

fun String.capitalizeFirst(): String {
    return if (isNotEmpty()) {
        this[0].uppercase() + substring(1).lowercase()
    } else {
        this
    }
}
