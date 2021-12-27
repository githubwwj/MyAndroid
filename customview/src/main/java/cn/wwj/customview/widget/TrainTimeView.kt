package cn.wwj.customview.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import cn.wwj.customview.R
import cn.wwj.customview.sp2px

/**
 * 事件训练
 */
class TrainTimeView : AppCompatTextView {

    /**
     * 文本画笔
     */
    private var mTextPaint: Paint = TextPaint()

    /**
     * 边框画笔
     */
    private var mBorderPaint: Paint = Paint()

    private var leftText = "-"
    private var rightText = "+"

    private var rectF = RectF()

    /**
     * 设置边框线的颜色
     */
    var mBorderColor: Int = resources.getColor(R.color.light_gray)
        set(value) {
            if (field == value) {
                return
            }
            field = value
            // 设置描边的宽度
            mBorderPaint.color = field
            invalidate()
        }

    /**
     * 日志过滤标签
     */
    private val TAG = javaClass.simpleName

    /**
     *  比如用于获取文字的大小,onDraw()方法会调用多次,不必每次都创建Rect()对象
     *  用同一个对象即可
     */
    private val mTextRect = Rect()

    /**
     * 设置边框线宽度
     */
    var mBorderWidth: Float = dp2px(0.5F)
        set(value) {
            if (field == value) {
                return
            }
            field = value
            // 设置描边的宽度
            mTextPaint.strokeWidth = field
            invalidate()
        }

    /**
     * 设置左边宽度（也就是减号的宽度）
     */
    var mLeftWidth: Float = dp2px(18F)
        set(value) {
            if (field == value) {
                return
            }
            field = value
            invalidate()
        }

    /**
     * 设置中间宽度 （30min）
     */
    var mCenterWidth: Float = dp2px(46F)
        set(value) {
            if (field == value) {
                return
            }
            field = value
            invalidate()
        }


    /**
     * 设置右边宽度
     */
    var mRightWidth: Float = dp2px(18F)
        set(value) {
            if (field == value) {
                return
            }
            field = value
            invalidate()
        }

    constructor(context: Context)
            : this(context, null)

    constructor(context: Context, attrs: AttributeSet?)
            : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        val appearance =
            context.obtainStyledAttributes(attrs, R.styleable.TrainTimeView)

        mBorderColor = appearance.getColor(R.styleable.TrainTimeView_trainBorderColor, mBorderColor)
        mBorderWidth = appearance.getFloat(R.styleable.TrainTimeView_trainBorderWidth, mBorderWidth)

        mLeftWidth = appearance.getFloat(R.styleable.TrainTimeView_leftWidth, mLeftWidth)
        mCenterWidth = appearance.getFloat(R.styleable.TrainTimeView_centerWidth, mCenterWidth)
        mRightWidth = appearance.getFloat(R.styleable.TrainTimeView_rightWidth, mRightWidth)

        appearance.recycle()

        setPaint()
    }

    /**
     * 设置 圆弧画笔用于绘制圆弧 和 文本画笔，用于绘画走了多少步
     */
    private fun setPaint() {
        // 画笔的颜色
        mBorderPaint.color = mBorderColor

        // 抗抖动
        mBorderPaint.isDither = true

        // 抗锯齿
        mBorderPaint.isAntiAlias = true

        // 画笔的样式描边,笔划突出为半圆
        mBorderPaint.style = Paint.Style.STROKE

        // 设置描边的线帽样式
        mBorderPaint.strokeCap = Paint.Cap.SQUARE

        // 设置描边的宽度
        mBorderPaint.strokeWidth = mBorderWidth


        // 画笔的颜色
        mTextPaint.color = currentTextColor

        // 抗抖动
        mTextPaint.isDither = true

        // 抗锯齿
        mTextPaint.isAntiAlias = true

        // 画笔的样式描边,笔划突出为半圆
        mTextPaint.style = Paint.Style.FILL

        // 设置描边的线帽样式
        mTextPaint.strokeCap = Paint.Cap.ROUND

        // 设置文本大小
        mTextPaint.textSize = textSize
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val oldTextSize = textSize

        // 重新计算左边最小宽高
        mTextPaint.textSize = sp2px(16F, resources)
        mTextPaint.getTextBounds(leftText, 0, leftText.length, mTextRect)
        val leftWidth = mTextRect.width() + dp2px(12F)
        if (mLeftWidth < leftWidth) {
            mLeftWidth = leftWidth
        }

        // 重新计算中间最小宽高
        mTextPaint.textSize = oldTextSize
        mTextPaint.getTextBounds(text.toString(), 0, text.length, mTextRect)
        val centerWidth = mTextRect.width() + dp2px(12F)
        if (mCenterWidth < centerWidth) {
            mCenterWidth = centerWidth
        }

        val height = mTextRect.height() + dp2px(10F).toInt()
        var heightSize = if (heightMode == MeasureSpec.EXACTLY) {
            MeasureSpec.getSize(heightMeasureSpec)
        } else {
            height
        }

        // 重新计算右边最小宽高
        mTextPaint.textSize = sp2px(14F, resources)
        mTextPaint.getTextBounds(rightText, 0, rightText.length, mTextRect)
        val rightWidth = mTextRect.width() + dp2px(12F)
        if (mRightWidth < rightWidth) {
            mRightWidth = rightWidth
        }

        val width = mLeftWidth + mCenterWidth + mRightWidth

//        Log.d(TAG, "----------width=$width----height=$heightSize")

        /**
         * 设置控件的宽高
         */
        setMeasuredDimension(width.toInt(), heightSize)
    }

    override fun onDraw(canvas: Canvas?) {

        val oldTextSize = textSize

        rectF.set(0f, 0f, width.toFloat(), height.toFloat())
        canvas?.drawRect(rectF, mBorderPaint)

        // 得到左边文字的宽高
        mTextPaint.textSize = sp2px(16F, resources)
        mTextPaint.getTextBounds(leftText, 0, leftText.length, mTextRect)
        var textX = mLeftWidth / 2F - mTextRect.width() / 2
        var textY = height / 2F + getBaseline(mTextPaint)
        canvas?.drawText(leftText, 0, leftText.length, textX, textY, mTextPaint)
        // 绘制第一条分割线
        canvas?.drawLine(mLeftWidth, 0F, mLeftWidth, height.toFloat(), mBorderPaint)


        // 得到中间文字的宽高
        mTextPaint.textSize = oldTextSize
        mTextPaint.getTextBounds(text.toString(), 0, text.length, mTextRect)
        textX = mLeftWidth + mCenterWidth / 2F - mTextRect.width() / 2
        textY = height / 2F + getBaseline(mTextPaint)
        canvas?.drawText(text, 0, text.length, textX, textY, mTextPaint)

        // 绘制第二条分割线
        canvas?.drawLine(
            mLeftWidth + mCenterWidth,
            0F,
            mLeftWidth + mCenterWidth,
            height.toFloat(),
            mBorderPaint
        )

        // 得到右边文字的宽高
        mTextPaint.textSize = sp2px(14F, resources)
        mTextPaint.getTextBounds(rightText, 0, rightText.length, mTextRect)
        textX = mLeftWidth + mCenterWidth + mRightWidth / 2F - mTextRect.width() / 2
        textY = height / 2F + getBaseline(mTextPaint)
        // 绘制右边的文本
        canvas?.drawText(rightText, 0, rightText.length, textX, textY, mTextPaint)
    }

    /**
     * 计算绘制文字时的基线到中轴线的距离
     * @param paint 画笔
     * @return 返回基线的距离
     */
    private fun getBaseline(paint: Paint): Float {
        val fontMetrics: Paint.FontMetrics = paint.fontMetrics
        return (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
    }


    fun dp2px(value: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics
        )
    }
}