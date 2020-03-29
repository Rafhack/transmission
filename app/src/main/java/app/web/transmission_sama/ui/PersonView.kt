package app.web.transmission_sama.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import app.web.transmission_sama.R

class PersonView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    @ColorRes
    var personColor: Int = R.color.colorPrimary
        set(value) {
            field = value
            updatePersonPaint()
        }

    @ColorRes
    var infectionColor: Int = R.color.colorPrimary
        set(value) {
            field = value
            updateInfectionPaint()
        }

    @ColorRes
    var infectionBorderColor: Int = R.color.colorPrimary
        set(value) {
            field = value
            updateInfectionPaint()
        }

    var infectionRatio: Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    private var personPaint = Paint()
    private var infectionPaint = Paint()
    private var infectionBorderPaint = Paint()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawCircle(width / 2f, height / 2f, 10f, personPaint)
        canvas?.drawCircle(width / 2f, height / 2f, infectionRatio, infectionPaint)
        canvas?.drawCircle(width / 2f, height / 2f, infectionRatio, infectionBorderPaint)
    }

    private fun updatePersonPaint() {
        this.personPaint = Paint().apply {
            color = ContextCompat.getColor(context, personColor)
            invalidate()
        }
    }

    private fun updateInfectionPaint() {
        this.infectionPaint = Paint().apply {
            color = ContextCompat.getColor(context, infectionColor)
            invalidate()
        }
        this.infectionBorderPaint = Paint().apply {
            color = ContextCompat.getColor(context, infectionBorderColor)
            strokeWidth = resources.getDimensionPixelSize(R.dimen.infection_stroke_width).toFloat()
            style = Paint.Style.STROKE
            invalidate()
        }
    }

}