package com.example.user_manager.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.example.user_manager.R

class ItemAnimator : DefaultItemAnimator() {
    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
        holder.itemView.alpha = 0f
        holder.itemView.translationY = holder.itemView.height.toFloat() / 4
        holder.itemView.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(300)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
        return true
    }

    override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {
        holder.itemView.animate()
            .alpha(0f)
            .translationX(holder.itemView.width.toFloat())
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    holder.itemView.alpha = 1f
                    holder.itemView.translationX = 0f
                }
            })
            .start()
        return true
    }
}

fun RecyclerView.setupAnimations() {
    layoutAnimation = AnimationUtils.loadLayoutAnimation(
        context,
        R.anim.layout_animation_fall_down
    )
    itemAnimator = ItemAnimator()
}

fun View.animateHeight(targetHeight: Int, duration: Long = 300) {
    val prevHeight = height
    val valueAnimator = android.animation.ValueAnimator.ofInt(prevHeight, targetHeight)
    valueAnimator.addUpdateListener { animation ->
        layoutParams.height = animation.animatedValue as Int
        requestLayout()
    }
    valueAnimator.duration = duration
    valueAnimator.start()
}
