package app.web.transmission_sama.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
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

    private var paint = Paint()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawRect(160f, 160f, 160f, 160f, paint)
    }

    private fun updatePaint() {
        this.paint = Paint().apply {
            color = siteColor
        }
    }

}