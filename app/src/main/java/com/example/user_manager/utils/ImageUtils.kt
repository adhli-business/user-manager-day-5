package com.example.user_manager.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.user_manager.R

object ImageUtils {
    fun ImageView.loadUserImage(
        imageUrl: String?,
        placeholder: Int = R.drawable.ic_person_placeholder,
        error: Int = R.drawable.ic_error_placeholder
    ) {
        Glide.with(this)
            .load(imageUrl)
            .apply(RequestOptions()
                .placeholder(placeholder)
                .error(error)
                .transform(CircleCrop()))
            .into(this)
    }

    fun ImageView.loadImage(
        imageUrl: String?,
        placeholder: Int = R.drawable.ic_image_placeholder,
        error: Int = R.drawable.ic_error_placeholder
    ) {
        Glide.with(this)
            .load(imageUrl)
            .apply(RequestOptions()
                .placeholder(placeholder)
                .error(error))
            .into(this)
    }
}
