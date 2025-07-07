package com.example.user_manager.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import com.example.user_manager.R
import com.example.user_manager.databinding.ViewLoadingBinding

class LoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: ViewLoadingBinding

    init {
        binding = ViewLoadingBinding.inflate(LayoutInflater.from(context), this, true)
        visibility = View.GONE
    }

    fun showLoading() {
        if (visibility != View.VISIBLE) {
            visibility = View.VISIBLE
            startAnimation()
        }
    }

    fun hideLoading() {
        if (visibility == View.VISIBLE) {
            visibility = View.GONE
            clearAnimation()
        }
    }

    private fun startAnimation() {
        val rotateAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate_infinite)
        binding.imageViewLoading.startAnimation(rotateAnimation)
    }
}
