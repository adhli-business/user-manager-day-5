package com.example.user_manager.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

object ItemAnimationUtils {
    fun View.addClickAnimation(
        scaleDown: Float = 0.95f,
        duration: Long = 100
    ) {
        setOnTouchListener { v, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_DOWN -> {
                    animateScale(v, scaleDown, duration)
                }
                android.view.MotionEvent.ACTION_UP, android.view.MotionEvent.ACTION_CANCEL -> {
                    animateScale(v, 1f, duration)
                    if (event.action == android.view.MotionEvent.ACTION_UP) {
                        performClick()
                    }
                }
            }
            true
        }
    }

    private fun animateScale(view: View, scale: Float, duration: Long) {
        val scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, scale)
        val scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, scale)
        AnimatorSet().apply {
            playTogether(scaleX, scaleY)
            this.duration = duration
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }

    fun View.animateAddition() {
        alpha = 0f
        scaleX = 0.8f
        scaleY = 0.8f
        animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(300)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
    }

    fun View.animateRemoval(onEnd: () -> Unit = {}) {
        animate()
            .alpha(0f)
            .scaleX(0.8f)
            .scaleY(0.8f)
            .setDuration(300)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .withEndAction { onEnd() }
            .start()
    }
}
