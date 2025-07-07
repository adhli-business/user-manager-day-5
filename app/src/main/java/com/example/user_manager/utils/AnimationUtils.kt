package com.example.user_manager.utils

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment

object AnimationUtils {
    fun View.fadeIn(duration: Long = 300) {
        val fadeIn = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.fade_in)
        fadeIn.duration = duration
        startAnimation(fadeIn)
        visibility = View.VISIBLE
    }

    fun View.fadeOut(duration: Long = 300, gone: Boolean = true) {
        val fadeOut = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.fade_out)
        fadeOut.duration = duration
        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                visibility = if (gone) View.GONE else View.INVISIBLE
            }
        })
        startAnimation(fadeOut)
    }

    fun Fragment.crossFade(showView: View, hideView: View, duration: Long = 300) {
        showView.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .setDuration(duration)
                .setListener(null)
        }

        hideView.animate()
            .alpha(0f)
            .setDuration(duration)
            .withEndAction { hideView.visibility = View.GONE }
    }
}
