package com.example.user_manager.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.example.user_manager.R

class CircularImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val path = Path()
    private var borderWidth = 0f
    private var borderColor = 0

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircularImageView)
            borderWidth = typedArray.getDimension(R.styleable.CircularImageView_borderWidth, 0f)
            borderColor = typedArray.getColor(R.styleable.CircularImageView_borderColor, 0)
            typedArray.recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        val width = width.toFloat()
        val height = height.toFloat()
        val radius = (width.coerceAtMost(height) / 2f)

        path.reset()
        path.addCircle(width / 2f, height / 2f, radius, Path.Direction.CW)
        canvas.clipPath(path)

        super.onDraw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // Maintain aspect ratio
        if (w != h) {
            val size = w.coerceAtMost(h)
            layoutParams.width = size
            layoutParams.height = size
            requestLayout()
        }
    }
}
