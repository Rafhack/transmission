package app.web.transmission_sama.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import app.web.transmission_sama.R

class SiteView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    @ColorRes
    var siteColor: Int = R.color.colorPrimary
        set(value) {
            field = value
            updatePaint()
        }

    var size: Int = 100
        set(value) {
            field = value
            invalidate()
        }

    private var paint = Paint()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawRect(0f, 0f, size.toFloat(), size.toFloat(), paint)
    }

    private fun updatePaint() {
        this.paint = Paint().apply {
            color = ContextCompat.getColor(context, siteColor)
            invalidate()
        }
    }

}