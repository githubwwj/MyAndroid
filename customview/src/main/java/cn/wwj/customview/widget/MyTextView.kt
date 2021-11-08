package cn.wwj.customview.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import cn.wwj.customview.R

class MyTextView : View {

    private var mText: String = ""
    private var mTextSize: Float = 0F
    private var mTextColor: Int = 0
    private var mTextPaint: Paint = Paint()
    private val TAG = javaClass.simpleName

    constructor(context: Context)
            : this(context, null)

    constructor(context: Context, attrs: AttributeSet?)
            : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        val appearance = context.obtainStyledAttributes(attrs, R.styleable.MyTextView)
        mText = appearance.getString(R.styleable.MyTextView_text) ?: ""
        mTextColor = appearance.getColor(R.styleable.MyTextView_textColor, mTextColor)
        mTextSize = appearance.getDimension(R.styleable.MyTextView_textSize, mTextSize)

        Log.d(TAG, "mTextSize=$mTextSize---textColor=$mTextColor")

        appearance.recycle()

        mTextPaint.textSize = mTextSize
        mTextPaint.color = mTextColor
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val withMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)

        if (withMode == MeasureSpec.AT_MOST) {
            val textRect = Rect()
            mTextPaint.getTextBounds(mText, 0, mText.length, textRect)
            widthSize = textRect.width() + paddingLeft + paddingRight
        }

        if (heightMode == MeasureSpec.AT_MOST) {
            val textRect = Rect()
            mTextPaint.getTextBounds(mText, 0, mText.length, textRect)
            heightSize = textRect.height() + paddingTop + paddingBottom
        }

        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val textRect = Rect()
        mTextPaint.getTextBounds(mText, 0, 1, textRect)

        val baseLine = getBaseline(mTextPaint) + height / 2
        val x = paddingLeft * 1.0F
        canvas?.drawText(mText, x, baseLine, mTextPaint)
    }

    /**
     * 计算绘制文字时的基线到中轴线的距离
     *
     * @param p
     * @param centerY
     * @return 基线和centerY的距离
     */
    private fun getBaseline(p: Paint): Float {
        val fontMetrics: Paint.FontMetrics = p.fontMetrics
        Log.d(
            TAG, "top=${fontMetrics.top} ---bottom=${fontMetrics.bottom}---" +
                    "descent=${fontMetrics.descent}---ascent=${fontMetrics.ascent}---leading=${fontMetrics.leading}"
        )
        return (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
    }


}