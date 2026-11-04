package com.example.shroom.ui.map

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.shroom.R
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class CompassView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private var headingDegrees: Float = 0f

    private val parchment = ContextCompat.getColor(context, R.color.parchment)
    private val wood = ContextCompat.getColor(context, R.color.wood)
    private val brass = ContextCompat.getColor(context, R.color.brass)
    private val stem = ContextCompat.getColor(context, R.color.stem_beige)
    private val ink = ContextCompat.getColor(context, R.color.ink_brown)
    private val capRed = ContextCompat.getColor(context, R.color.cap_red)

    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.STROKE }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ink
        textAlign = Paint.Align.CENTER
        typeface = ResourcesCompat.getFont(context, R.font.medievalsharp)
            ?: Typeface.create(Typeface.SERIF, Typeface.NORMAL)
    }
    private val northPath = Path()
    private val southPath = Path()

    fun setHeading(degrees: Float) {
        if (headingDegrees != degrees) {
            headingDegrees = degrees
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cx = width / 2f
        val cy = height / 2f
        val radius = min(cx, cy)

        fillPaint.color = parchment
        canvas.drawCircle(cx, cy, radius, fillPaint)

        strokePaint.color = wood
        strokePaint.strokeWidth = dp(4f)
        canvas.drawCircle(cx, cy, radius - dp(1f), strokePaint)

        strokePaint.color = brass
        strokePaint.strokeWidth = dp(2f)
        canvas.drawCircle(cx, cy, radius - dp(6f), strokePaint)

        fillPaint.color = stem
        fillPaint.alpha = 140
        canvas.drawCircle(cx, cy, radius - dp(12f), fillPaint)
        fillPaint.alpha = 255

        canvas.save()
        canvas.rotate(-headingDegrees, cx, cy)

        val tickOuter = radius - dp(14f)
        for (i in 0 until 36) {
            val angle = Math.toRadians((i * 10).toDouble())
            val major = i % 3 == 0
            val inner = if (major) tickOuter - dp(8f) else tickOuter - dp(4f)
            strokePaint.color = if (major) ink else wood
            strokePaint.strokeWidth = if (major) dp(2f) else dp(1f)
            canvas.drawLine(
                cx + cos(angle).toFloat() * inner,
                cy + sin(angle).toFloat() * inner,
                cx + cos(angle).toFloat() * tickOuter,
                cy + sin(angle).toFloat() * tickOuter,
                strokePaint,
            )
        }

        textPaint.textSize = sp(11f)
        val labelR = radius - dp(24f)
        drawCardinal(canvas, "N", -90.0, cx, cy, labelR)
        drawCardinal(canvas, "E", 0.0, cx, cy, labelR)
        drawCardinal(canvas, "S", 90.0, cx, cy, labelR)
        drawCardinal(canvas, "W", 180.0, cx, cy, labelR)
        canvas.restore()

        val needleLen = radius - dp(20f)
        northPath.reset()
        northPath.moveTo(cx, cy - needleLen)
        northPath.lineTo(cx + dp(6f), cy + dp(4f))
        northPath.lineTo(cx, cy + dp(2f))
        northPath.lineTo(cx - dp(6f), cy + dp(4f))
        northPath.close()
        fillPaint.color = capRed
        canvas.drawPath(northPath, fillPaint)

        southPath.reset()
        southPath.moveTo(cx, cy + needleLen * 0.85f)
        southPath.lineTo(cx + dp(5f), cy - dp(2f))
        southPath.lineTo(cx, cy)
        southPath.lineTo(cx - dp(5f), cy - dp(2f))
        southPath.close()
        fillPaint.color = ink
        canvas.drawPath(southPath, fillPaint)

        fillPaint.color = brass
        canvas.drawCircle(cx, cy, dp(4f), fillPaint)
        fillPaint.color = ink
        canvas.drawCircle(cx, cy, dp(2f), fillPaint)
    }

    private fun drawCardinal(
        canvas: Canvas,
        letter: String,
        angleDeg: Double,
        cx: Float,
        cy: Float,
        r: Float,
    ) {
        val rad = Math.toRadians(angleDeg)
        val x = cx + cos(rad).toFloat() * r
        val y = cy + sin(rad).toFloat() * r - (textPaint.descent() + textPaint.ascent()) / 2f
        canvas.drawText(letter, x, y, textPaint)
    }

    private fun dp(value: Float): Float = value * resources.displayMetrics.density
    private fun sp(value: Float): Float = value * resources.displayMetrics.scaledDensity
}
