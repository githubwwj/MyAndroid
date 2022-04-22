package cn.wwj.customview.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.marginTop
import cn.wwj.customview.R
import cn.wwj.customview.dp2px
import cn.wwj.customview.sp2px

/**
 * 节点进度条
 */
class NodePointProcessBar : AppCompatTextView {

    /**
     * 文字画笔
     */
    private lateinit var mTextPaint: TextPaint

    /**
     * 圆画笔
     */
    private lateinit var mCirclePaint: Paint

    private var isDebug = false

    /**
     * 已完成文字颜色
     */
    private var mCompleteTextColor: Int = ContextCompat.getColor(context, android.R.color.black)

    /**
     * 处理中文字颜色
     */
    private var mProcessTextColor: Int = ContextCompat.getColor(context, R.color.purple)

    /**
     * 待处理的文字颜色
     */
    private var mWaitProcessTextColor: Int = ContextCompat.getColor(context, R.color.gray_text)

    /**
     * 绘制的节点个数，由底部节点标题数量控制
     */
    private var mCircleCount = 0


    private var TAG = "NodePointProcessBar"

    /**
     * 圆的半径
     */
    private var mCircleRadius = 5f.dp2px()

    /**
     * 圆圈的边框线
     */
    private var mCircleBorder = 1f.dp2px()


    /**
     * 线的宽度
     */
    private var mLineWidth = 1f.dp2px()

    /**
     * 线之间的左右边距
     */
    private var mLineMargin = 4f.dp2px()

    /**
     * 文字和圆圈之间的距离
     */
    var mTextCircleMargin = 7f.dp2px()

    /**
     * 文字的水平边距
     */
    private var mTextLeftRightMargin = 8f.dp2px()

    /**
     * 计算内容的高度 和 宽度
     */
    private var mContentHeight = 0f
    private var mContentWidth = 0f

    /**
     * 节点底部的文字列表
     */
    private var mTextList: List<String> = mutableListOf()

    /**
     * 选中项集合
     */
    private var mSelectedIndexSet: Set<Int> = mutableSetOf()

    /**
     * 文字同宽高的矩形，用来测量文字
     */
    private var mTextBoundList: MutableList<Rect> = mutableListOf()

    /**
     * 计算文字宽高的矩形
     */
    private val mRect = Rect()


    constructor(context: Context) : this(context, null)

    constructor(context: Context, @Nullable attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(
        context: Context,
        @Nullable attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        val appearance = context.obtainStyledAttributes(attrs, R.styleable.NodePointProcessBar)
        mCompleteTextColor = appearance.getColor(
            R.styleable.NodePointProcessBar_completedTextColor, mCompleteTextColor
        )
        mWaitProcessTextColor = appearance.getColor(
            R.styleable.NodePointProcessBar_processTextColor, mWaitProcessTextColor
        )
        mProcessTextColor =
            appearance.getColor(
                R.styleable.NodePointProcessBar_waitProcessTextColor,
                mProcessTextColor
            )
        isDebug = appearance.getBoolean(
            R.styleable.NodePointProcessBar_isDebug, isDebug
        )
        mTextCircleMargin = appearance.getDimension(
            R.styleable.NodePointProcessBar_textCircleMargin, mTextCircleMargin
        )
        mTextLeftRightMargin = appearance.getDimension(
            R.styleable.NodePointProcessBar_textLeftRightMargin, mTextLeftRightMargin
        )
        mCircleRadius = appearance.getDimension(
            R.styleable.NodePointProcessBar_npbCircleRadius, mCircleRadius
        )
        mCircleBorder = appearance.getDimension(
            R.styleable.NodePointProcessBar_circleBorder, mCircleBorder
        )
        mLineWidth = appearance.getDimension(
            R.styleable.NodePointProcessBar_lineWidth, mLineWidth
        )
        mLineMargin = appearance.getDimension(
            R.styleable.NodePointProcessBar_lineMargin, mLineMargin
        )
        initPaint()
        show(mTextList, mSelectedIndexSet)
    }

    /**
     * 初始化画笔属性
     */
    private fun initPaint() {
        // 设置文字画笔
        mTextPaint = TextPaint()
        mTextPaint.isAntiAlias = true
        mTextPaint.textSize = textSize
        mTextPaint.color = mWaitProcessTextColor

        // 设置圆圈画笔
        mCirclePaint = Paint()
        mCirclePaint.isAntiAlias = true
        mCirclePaint.color = mProcessTextColor
        mCirclePaint.style = Paint.Style.STROKE
        mCirclePaint.strokeWidth = mCircleBorder
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        Log.d(TAG, "---------------onMeasure()")
        measureText()

        val widthSize = if (MeasureSpec.EXACTLY == widthMode) {
            MeasureSpec.getSize(widthMeasureSpec)
        } else {
            mContentWidth.toInt()
        }

        val heightSize = if (MeasureSpec.EXACTLY == heightMode) {
            MeasureSpec.getSize(heightMeasureSpec)
        } else {
            mContentHeight.toInt()
        }

        /**
         * 设置控件的宽高
         */
        setMeasuredDimension(widthSize, heightSize)
        calcContentWidthHeight()
    }

    /**
     * 测量文字的长宽，将文字视为rect矩形
     */
    private fun measureText() {
        Log.d(TAG, "---------------measureText()")
        mTextBoundList.clear()
        for (name in mTextList) {
            mRect.setEmpty()
            mTextPaint.getTextBounds(name, 0, name.length, mRect)
            mTextBoundList.add(mRect)
        }
    }

    /**
     * 获取内容的高度
     */
    private fun calcContentWidthHeight() {
        mContentHeight = if (mTextBoundList.isNotEmpty()) {
            mCircleRadius * 2 + mTextCircleMargin + mRect.height() + getBaseline(mTextPaint)
        } else {
            mTextPaint.getTextBounds("中", 0, 1, mRect)
            mCircleRadius * 2 + mTextCircleMargin + +mRect.height() + getBaseline(mTextPaint)
        }
        if (measuredWidth == 0 || mTextBoundList.isEmpty()) {
            return
        }
        mContentWidth = 0f
        for (rect in mTextBoundList) {
            mContentWidth += rect.width()
        }
        Log.d(TAG, "---------------measuredWidth=$measuredWidth,mContentWidth=$mContentWidth")
        if (measuredWidth - mContentWidth < (mTextLeftRightMargin * (mTextList.size - 1))) {
            mTextPaint.textSize = mTextPaint.textSize - 1f.sp2px()
            measureText()
            calcContentWidthHeight()
            return
        }
        mTextLeftRightMargin =
            (measuredWidth - mContentWidth) / if (mTextBoundList.size - 1 == 0) 1 else mTextBoundList.size - 1
        setMeasuredDimension(measuredWidth, mContentHeight.toInt())
    }

    override fun onDraw(canvas: Canvas) {
        //若未设置节点标题或者选中项的列表，则取消绘制
        if (mTextList.isEmpty() || mTextBoundList.isEmpty()) {
            return
        }
        //画灰色圆圈的个数
        mCircleCount = mTextList.size

        // 每一段文字的Y坐标
        val textY = getBaseline(mTextPaint) + height / 2 + marginTop / 2

        mCirclePaint.strokeWidth = mCircleBorder
        //绘制文字和圆形
        for (i in 0 until mCircleCount) {
            // 正在处理
            if (mSelectedIndexSet.contains(i)) {
                // 正在处理中
                if (mSelectedIndexSet.size == i + 1) {
                    mCirclePaint.style = Paint.Style.FILL
                    // 正在处理中的文字颜色
                    mTextPaint.color = mProcessTextColor
                    mCirclePaint.color = mProcessTextColor
                } else { //已经处理过了==>过去
                    //已经处理过的圆圈空心
                    mCirclePaint.style = Paint.Style.STROKE
                    // 已经处理过的文字颜色
                    mTextPaint.color = mCompleteTextColor
                    mCirclePaint.color = mProcessTextColor
                }
            } else {
                //未处理中
                mCirclePaint.color = mWaitProcessTextColor
                mCirclePaint.style = Paint.Style.FILL
                mTextPaint.color = mWaitProcessTextColor
            }

            //每一段文字宽度
            val textWidth = mTextBoundList[i].width()
            // 每一段宽度
            val itemWidth = width * 1f / mCircleCount
            // 每一段文字居中
            // |----text----|----text----|
            //    一段文字       一段文字
            //每一段文字起始的X坐标
            val textX = itemWidth / 2f - textWidth / 2f + i * itemWidth
            canvas.drawText(mTextList[i], textX, textY, mTextPaint)


            //每一个圆圈的Y坐标
            val circleY = height / 2f - mCircleRadius - mTextCircleMargin / 2
            //每一个圆圈的X坐标
            val circleX = itemWidth / 2 + i * itemWidth
            canvas.drawCircle(
                circleX,
                circleY,
                mCircleRadius,
                mCirclePaint
            )

            // 画线,两个圆圈之间一条线段
            mCirclePaint.strokeWidth = mLineWidth
            if (i < mCircleCount - 1) {
                //选中的线颜色
                if (mSelectedIndexSet.contains(i + 1)) {
                    mCirclePaint.color = mProcessTextColor
                } else {
                    // 未选中线的颜色
                    mCirclePaint.color = mWaitProcessTextColor
                }
                // 线段起始 x 坐标
                val lineStartX = itemWidth * i + itemWidth / 2f + mCircleRadius + mLineMargin
                // 线段结束 x 坐标
                val lineEndX = itemWidth * i + itemWidth + itemWidth / 2f - mCircleRadius - mLineMargin
                canvas.drawLine(
                    lineStartX,
                    circleY,
                    lineEndX,
                    circleY,
                    mCirclePaint
                )
            }
            Log.d("tag", "--------itemWidth=$itemWidth")
        }

        if (isDebug) {
            mCirclePaint.color = Color.RED
            canvas.drawLine(
                0f,
                height / 2f - 1f.dp2px() / 2,
                width * 1F,
                height / 2f + 1f.dp2px() / 2,
                mCirclePaint
            )
        }
    }

    /**
     * 供外部调用，显示控件
     * @param titles 底部标题内容列表
     * @param indexSet 选中项Set
     */
    fun show(titles: List<String>, indexSet: Set<Int>) {
        mTextList = titles
        mSelectedIndexSet = indexSet
        measureText()
        calcContentWidthHeight()
        invalidate()
    }

    /**
     * 获取文字的基线
     */
    private fun getBaseline(p: Paint): Float {
        val fontMetrics: Paint.FontMetrics = p.fontMetrics
        return (fontMetrics.bottom - fontMetrics.top) - fontMetrics.descent
    }


}
