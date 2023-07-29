package com.example.roulette

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import java.lang.Math.cos
import java.lang.Math.sin
import java.lang.RuntimeException

class Roulette @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr){

    private val strokePaint = Paint()
    private val rectF = RectF()
    private val fillPaint = Paint()
    private val textPaint = Paint()

    private var rouletteSize = 5
    private var rouletteDataList: List<String> = listOf()

    init {
        strokePaint.apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = 15f
            isAntiAlias = true
        }

        fillPaint.apply {
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        textPaint.apply {
            color = Color.BLACK
            textSize = 60f
            textAlign = Paint.Align.CENTER
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val rectLeft = left.toFloat() + paddingLeft
        val rectRight = right - paddingRight
        val rectTop = height / 2f - rectRight / 2f + paddingTop
        val rectBottom = height / 2f + rectRight / 2f - paddingRight

        rectF.set(rectLeft, rectTop, rectRight.toFloat(), rectBottom)
        drawRoulette(canvas, rectF)
    }

    private fun drawRoulette(canvas: Canvas?, rectf: RectF) {
        canvas?.drawArc(rectF, 0f, 360f, true, strokePaint)

        if(rouletteSize in 2.. 8) {
            val sweepAngle = 360f / rouletteSize.toFloat()
            val centerX = (rectF.left + rectF.right) / 2    // 룰렛 중심 좌표
            val centerY = (rectF.top + rectF.bottom) / 2
            val radius = (rectF.right - rectF.left) / 2 * 0.5
            val colors = listOf("#fe4a49", "#2ab7ca", "#fed766", "#e6e6ea", "#f6abb6", "#005b96", "#7bc043", "#f37735")

            for(i in 0 until rouletteSize) {
                fillPaint.color = Color.parseColor(colors[i])

                val startAngle = if (i==0) 0f else sweepAngle * i
                canvas?.drawArc(rectf, startAngle, sweepAngle, true, fillPaint)

                val medianAngle = (startAngle + sweepAngle / 2f) * Math.PI / 180f // 룰렛 내부 중앙 각도
                val x = (centerX + (radius * cos(medianAngle))).toFloat()   // 텍스트를 그려줄 좌표
                val y = (centerY + (radius * sin(medianAngle))).toFloat()
                // 텍스트 데이터 리스트에 있는 텍스트만 표시하고 빈 부분은 "empty"라는 임의의 값으로 대체
                val text = if (i  >rouletteDataList.size - 1) "empty" else rouletteDataList[i]

                // 텍스트를 그려주는 좌표
                canvas?.drawText(text, x, y, textPaint)
            }
        } else throw RuntimeException("size out of roulette")
    }
}

fun rotateRoulette(toDegrees: Float, duration: Long) {
    val rotateAnim = RotateAnimation(
        0f, toDegrees,
        Animation.RELATIVE_TO_SELF, 0.5f,
        Animation.RELATIVE_TO_SELF, 0.5f
    )

    rotateAnim.duration = duration
    rotateAnim.fillAfter = true

//    startAnimation(rotateAnim)
}