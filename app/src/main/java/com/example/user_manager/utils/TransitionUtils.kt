package com.example.user_manager.utils

import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.user_manager.R

object TransitionUtils {
    fun Fragment.beginEmptyStateTransition(sceneRoot: ViewGroup) {
        val transition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.empty_state_transition)
        TransitionManager.beginDelayedTransition(sceneRoot, transition)
    }

    fun Fragment.beginListStateTransition(sceneRoot: ViewGroup) {
        val transition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.fade)
        TransitionManager.beginDelayedTransition(sceneRoot, transition)
    }

    fun Fragment.beginLoadingStateTransition(sceneRoot: ViewGroup) {
        val transition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.fade)
        transition.duration = 150
        TransitionManager.beginDelayedTransition(sceneRoot, transition)
    }
}
