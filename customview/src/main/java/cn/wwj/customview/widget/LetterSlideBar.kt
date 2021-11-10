package cn.wwj.customview.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.postDelayed
import cn.wwj.customview.R
import cn.wwj.customview.dp2px

/**
 * 字母滑动条
 */
class LetterSlideBar : AppCompatTextView {

    /**
     * 选中的字母
     */
    private var mCurrentTouchChar: Char = ' '

    private val TAG = "SlideBar"

    private var mSelectColor = resources.getColor(android.R.color.holo_red_light)

    private var mLetters = listOf(
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
        'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
        'U', 'V', 'W', 'X', 'Y', 'Z', '#'
    )

    constructor(context: Context)
            : this(context, null)

    constructor(context: Context, attrs: AttributeSet?)
            : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        val appearance =
            context.obtainStyledAttributes(attrs, R.styleable.LetterSlideBar)

        mSelectColor = appearance.getColor(R.styleable.LetterSlideBar_selectTextColor, mSelectColor)

        appearance.recycle()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val withMode = MeasureSpec.getMode(widthMeasureSpec)

        /**
         * 获取控件的宽高
         */
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        /**
         * 如果控件的高度是包裹内容,字母的宽度 + 30dp
         */
        if (withMode == MeasureSpec.AT_MOST) {
            widthSize = (paint.measureText("A") + dp2px(30F, resources)).toInt()
        }
        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onDraw(canvas: Canvas?) {
        var x: Float
        var y: Float

        // 获取每一个字母的高度
        val itemHeight = (height - paddingTop - paddingBottom) / mLetters.size


        mLetters.forEachIndexed { index, c ->
            // 每一个字母x的位置 等于控件宽度的一半 - 字母宽度的一半
            x = width / 2 - paint.measureText(c.toString(), 0, 1) / 2

            // 每一个字母高度的一半 + 每一个字母的高度 + 前面有几个字母的高度
            y = itemHeight / 2 + getBaseline(paint) + index * itemHeight

            if (mCurrentTouchChar == c) {
                // 如果是选中的字母,当前选中的文字颜色
                paint.color = mSelectColor
                canvas?.drawText(c.toString(), x, y, paint)
            } else {
                // 设置文本颜色
                paint.color = currentTextColor
                canvas?.drawText(c.toString(), x, y, paint)
            }

        }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // 每一项高度
        val itemHeight = height / mLetters.size

        // 获取字母的索引
        var letterIndex = (event.y / itemHeight).toInt()
        // 如果字母索引小于0,最小值0
        if (letterIndex < 0) {
            letterIndex = 0

            // 如果字母索引大于最大字母长度,则为最大字母长度减1
        } else if (letterIndex >= mLetters.size) {
            letterIndex = mLetters.size - 1
        }

        when (event.action) {
            // 如果是手指按下或者滑动,设置了触摸监听,获取当前触摸的字符
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                mOnLetterTouchListener?.let {
                    mCurrentTouchChar = mLetters[letterIndex]
                    // 回调触摸监听,传递当前选中的字符并刷新界面
                    it.setLetterTouchListener(mLetters[letterIndex].toString())
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                mOnLetterTouchListener?.let {
                    // 300 毫秒后取消设置监听,刷新界面
                    postDelayed(300) {
                        // 回调触摸监听,当前未选中的字符
                        it.setLetterTouchListener(null)
                        mCurrentTouchChar = ' '
                        invalidate()
                    }
                }
            }
        }
        return true
    }

    interface LetterTouchListener {
        fun setLetterTouchListener(letter: String?)
    }

    private var mOnLetterTouchListener: LetterTouchListener? = null

    /**
     * 设置字母滑动监听
     */
    fun setOnLetterTouchListener(letterTouchListener: LetterTouchListener) {
        this.mOnLetterTouchListener = letterTouchListener
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


}