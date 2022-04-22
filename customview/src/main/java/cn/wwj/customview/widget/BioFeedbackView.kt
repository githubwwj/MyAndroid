package cn.wwj.customview.widget

import android.content.Context
import android.content.res.Configuration
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatImageView
import cn.wwj.customview.R
import cn.wwj.customview.dp2px
import cn.wwj.customview.sp2px
import kotlin.math.abs

/**
 * 生物反馈的View
 */
class BioFeedbackView : AppCompatImageView {


    private var previousX: Float = 0f

    private var previousY: Float = 0f

    /**
     * 阶段时长
     */
    var mStageDuration: Int = 0

    /**
     * 数据间隔200毫秒
     */
    var mDataInterval: Float = 0f

    /**
     * 数据长度
     */
    private val mDataLength = 5

    /**
     * 高度有几个格子
     */
    private val mGridHeightCount: Int = 5

    /**
     * 宽度有几个格子
     */
    private var mGridWidthCount: Int = 10

    /**
     * 网格高度
     */
    private var mGridHeight: Float = 0F

    /**
     * 网格宽度
     */
    private var mGridWidth: Float = 0F

    /**
     * 水平线底部偏移量
     */
    var mOffsetBottomY = dp2px(10f)
        set(value) {
            if (field == value) {
                return
            }
            field = value
            // 设置描边的宽度
            invalidate()
        }

    /**
     * 水平线顶部偏移量
     */
    var mOffsetTopY = dp2px(20f)

    /**
     * 是否绘制垂线
     */
    var isDrawVerticalLine = false
        set(value) {
            if (field == value) {
                return
            }
            field = value
            // 设置描边的宽度
            invalidate()
        }

    /**
     * 纵轴偏移量
     */
    var mTitle = ""
        set(value) {
            if (field == value) {
                return
            }
            field = value
            // 设置描边的宽度
            invalidate()
        }


    /**
     * 左侧第一个提示
     */
    var mLeftFirstCommit = "调整曲线"
        set(value) {
            if (field == value) {
                return
            }
            field = value
            // 设置描边的宽度
            invalidate()
        }

    /**
     * 左侧第二个提示
     */
    var mLeftSecondCommit = ""
        set(value) {
            if (field == value) {
                return
            }
            field = value
            // 设置描边的宽度
            invalidate()
        }

    /**
     * 边框线画笔
     */
    private var mBorderPaint: Paint = Paint()

    /**
     * 文字画笔
     */
    private var mTextPaint: Paint = TextPaint()

    /**
     * 水平线左侧偏移量
     */
    private var mOffsetLeftX = 0F

    /**
     * 设置边框线宽度
     */
    var mBorderLineWidth: Float = dp2px(1F)
        set(value) {
            if (field == value) {
                return
            }
            field = value
            // 设置描边的宽度
            mBorderPaint.strokeWidth = field
            invalidate()
        }

    /**
     * 最小值
     */
    var mMinValue = 0f
        set(value) {
            if (field == value) {
                return
            }
            field = value
            invalidate()
        }

    /**
     * 最大值
     */
    var mMaxValue = 100f
        set(value) {
            if (field == value) {
                return
            }
            field = value / 0.7f
            invalidate()
        }

    /**
     * 实时值
     */
    var mRealValue = 0f
        set(value) {
            if (field == value) {
                return
            }
            field = value
            invalidate()
        }

    /**
     * Y轴最大值
     */
    private val YAxisMaxValue = 100


    val TAG_VIEW = "BioFeedbackView"

    /**
     *  比如用于获取文字的大小,onDraw()方法会调用多次,不必每次都创建Rect()对象
     *  用同一个对象即可
     */
    private val mTextRect = Rect()

    /**
     * 绘制路径
     */
    private val mPath = Path()

    private var startX = 0f

    /**
     * 设置边框线的颜色
     */
    private var mBorderLineColor: Int = resources.getColor(R.color.bioBorderLineColor)

    /**
     * 纵坐标的文本颜色
     */
    private var mTextColor: Int = resources.getColor(R.color.bioTextColor)

    /**
     * 底部文本的颜色
     */
    private var mBottomTextColor: Int = resources.getColor(R.color.bioCommitTextColor)

    /**
     * 标题颜色
     */
    private var mTitleColor: Int = resources.getColor(R.color.bioTitleColor)

    /**
     * 底部第一条线的颜色
     */
    private var bioBottomFirstLineColor: Int = resources.getColor(R.color.bioBottomFirstLineColor)

    /**
     * 底部第二条线的颜色
     */
    private var bioBottomSecondLineColor: Int = resources.getColor(R.color.bioBottomSecondLineColor)

    /**
     * 值列表
     */
    private val valueList = ArrayList<Float>()

    constructor(context: Context)
            : this(context, null)

    constructor(context: Context, attrs: AttributeSet?)
            : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        val appearance = context.obtainStyledAttributes(attrs, R.styleable.BioFeedbackView)

        mBorderLineColor =
            appearance.getColor(R.styleable.BioFeedbackView_bioBorderLineColor, mBorderLineColor)
        mTextColor = appearance.getColor(R.styleable.BioFeedbackView_bioTextColor, mTextColor)
        mTitle = appearance.getString(R.styleable.BioFeedbackView_bioTitle) ?: mTitle
        mBottomTextColor =
            appearance.getColor(R.styleable.BioFeedbackView_bioCommitTextColor, mBottomTextColor)
        mTitleColor = appearance.getColor(R.styleable.BioFeedbackView_bioTitleColor, mTitleColor)
        bioBottomFirstLineColor = appearance.getColor(
            R.styleable.BioFeedbackView_bioBottomFirstLineColor,
            bioBottomFirstLineColor
        )
        bioBottomSecondLineColor = appearance.getColor(
            R.styleable.BioFeedbackView_bioBottomSecondLineColor,
            bioBottomSecondLineColor
        )
        mBorderLineWidth =
            appearance.getDimension(R.styleable.BioFeedbackView_bioBorderWidth, mBorderLineWidth)
        isDrawVerticalLine =
            appearance.getBoolean(
                R.styleable.BioFeedbackView_bioIsDrawVerticalLine,
                isDrawVerticalLine
            )
        mOffsetBottomY =
            appearance.getDimension(R.styleable.BioFeedbackView_bioOffsetY, mOffsetBottomY)

        appearance.recycle()

        setPaint()
    }

    /**
     * 设置 圆弧画笔用于绘制圆弧 和 文本画笔，用于绘画走了多少步
     */
    private fun setPaint() {
        // 画笔的颜色
        mBorderPaint.color = mBorderLineColor

        // 抗抖动
        mBorderPaint.isDither = true

        // 抗锯齿
        mBorderPaint.isAntiAlias = true

        // 画笔的样式描边,笔划突出为半圆
        mBorderPaint.style = Paint.Style.STROKE

        // 设置描边的线帽样式
        mBorderPaint.strokeCap = Paint.Cap.SQUARE

        // 设置描边的宽度
        mBorderPaint.strokeWidth = mBorderLineWidth


        // 文本画笔的颜色
        mTextPaint.color = mTextColor

        // 抗抖动
        mTextPaint.isDither = true

        // 抗锯齿
        mTextPaint.isAntiAlias = true

        // 设置描边的宽度
        mTextPaint.textSize = sp2px(14f)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        /**
         * 获取控件的宽高
         */
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

//        Log.d(TAG, "-------onMeasure---w=$widthSize,h=$heightSize")
        /**
         * 设置控件的宽高
         */
        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Log.d(TAG_VIEW, "-----------onLayout()-----")
        mTextRect.setEmpty()
        mTextPaint.getTextBounds("000", 0, 3, mTextRect)
        mOffsetLeftX = mTextRect.width() + dp2px(5F)

        mTextRect.setEmpty()
        mTextPaint.getTextBounds(mTitle, 0, mTitle.length, mTextRect)
        mOffsetTopY = mTextRect.height() + dp2px(9F)


        mTextRect.setEmpty()
        mTextPaint.getTextBounds(mLeftFirstCommit, 0, mLeftFirstCommit.length, mTextRect)
        mOffsetBottomY += mTextRect.height().toFloat() + dp2px(10f)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mGridHeight =
                ((height - mOffsetTopY - mOffsetBottomY - mBorderLineWidth) / mGridHeightCount)
            mGridWidth = ((width - mBorderLineWidth - mOffsetLeftX) / mGridWidthCount)
        } else if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mGridHeight =
                ((height - mOffsetTopY - mOffsetBottomY - mBorderLineWidth) / mGridWidthCount)
            mGridWidth = ((width - mBorderLineWidth - mOffsetLeftX) / mGridHeightCount)
        }

        onLayoutListener?.invoke()
    }

    var onLayoutListener: (() -> Unit)? = null

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (mGridHeight == 0f || mGridWidth == 0f) {
            return
        }
        mTextRect.setEmpty()
        mBorderPaint.strokeWidth = mBorderLineWidth
        mBorderPaint.color = mBorderLineColor

        // 获取文本的宽高
        mTextPaint.getTextBounds(mTitle, 0, mTitle.length, mTextRect)
        var textX = dp2px(40f)
        var textY = getBaseline(mTextPaint) + mTextRect.height() / 2
        // 绘制文本,第二个参数文本的起始索引,第三个参数要绘制的文字长度
        // 开始绘制文字的x 坐标 y 坐标
        mTextPaint.color = mTitleColor
        canvas?.drawText(mTitle, textX, textY, mTextPaint)


        // 水平线数量
        val horizontalLineCount =
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mGridHeightCount
            } else {
                mGridWidthCount
            }

        var currentYAxis = YAxisMaxValue
        mTextPaint.getTextBounds("$currentYAxis", 0, "$YAxisMaxValue".length, mTextRect)
        // 绘制水平线
        for (i in 0..horizontalLineCount) {
            // 绘制水平线起始文本,如 100,80,60,40,20,0
            textX = ((3 - "$currentYAxis".length) * mTextRect.width() / 3).toFloat()
            textY = mGridHeight * i + getBaseline(mTextPaint) + mOffsetTopY
            mTextPaint.color = mTextColor
            canvas?.drawText("$currentYAxis", textX, textY, mTextPaint)

            canvas?.drawLine(
                mBorderLineWidth / 2 + mOffsetLeftX,
                mBorderLineWidth / 2 + mGridHeight * i + mOffsetTopY,
                width * 1F - mBorderLineWidth / 2 + mOffsetLeftX,
                mBorderLineWidth / 2 + mGridHeight * i + mOffsetTopY,
                mBorderPaint
            )
            currentYAxis -= 20
        }

        if (isDrawVerticalLine) {
            val verticalLineCount =
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    mGridWidthCount
                } else {
                    mGridHeightCount
                }
            // 绘制垂线
            for (i in 0..verticalLineCount) {
                canvas?.drawLine(
                    mBorderLineWidth / 2 + mGridWidth * i + mOffsetLeftX,
                    mBorderLineWidth / 2 + mOffsetTopY,
                    mBorderLineWidth / 2 + mGridWidth * i + mOffsetLeftX,
                    height - mBorderLineWidth / 2 - mOffsetBottomY,
                    mBorderPaint
                )
            }
        }

        // 设置描边的线帽样式
        mBorderPaint.strokeCap = Paint.Cap.ROUND
        canvas?.drawPath(mPath, mBorderPaint)


        mBorderPaint.strokeCap = Paint.Cap.SQUARE
        // 获取左侧底部第一个文本提示的大小
        val lineWidth = dp2px(17f)
        val lineRightMargin = dp2px(8f)
        if (mLeftFirstCommit.isNotEmpty()) {
            mTextRect.setEmpty()
            mTextPaint.getTextBounds(mLeftFirstCommit, 0, mLeftFirstCommit.length, mTextRect)
            textX = mOffsetLeftX + lineWidth + lineRightMargin
            textY = height - getBaseline(mTextPaint)
            // 绘制文本,第二个参数文本的起始索引,第三个参数要绘制的文字长度
            // 开始绘制文字的x 坐标 y 坐标
            mTextPaint.color = mBottomTextColor
            canvas?.drawText(mLeftFirstCommit, textX, textY, mTextPaint)

            // 绘制左侧底部第一个小长条
            mBorderPaint.strokeWidth = dp2px(5f)
            mBorderPaint.color = bioBottomFirstLineColor
            canvas?.drawLine(
                mOffsetLeftX,
                height - mTextRect.height() / 2 - mBorderPaint.strokeWidth / 2,
                mOffsetLeftX + dp2px(17f),
                height - mTextRect.height() / 2 - mBorderPaint.strokeWidth / 2,
                mBorderPaint
            )
        }

        if (mLeftSecondCommit.isNotEmpty()) {
            // 获取左侧底部第二个文本提示的大小
            val leftSecondOffsetX = textX + mTextRect.width() + dp2px(30f)
            textX = lineWidth + lineRightMargin + leftSecondOffsetX
            mTextRect.setEmpty()
            mTextPaint.getTextBounds(mLeftSecondCommit, 0, mLeftSecondCommit.length, mTextRect)
            textY = height - getBaseline(mTextPaint)
            // 绘制文本,第二个参数文本的起始索引,第三个参数要绘制的文字长度
            // 开始绘制文字的x 坐标 y 坐标

            canvas?.drawText(mLeftSecondCommit, textX, textY, mTextPaint)

            // 绘制左侧底部第二个小长条
            mBorderPaint.strokeWidth = dp2px(5f)
            mBorderPaint.color = bioBottomSecondLineColor
            canvas?.drawLine(
                leftSecondOffsetX,
                height - mTextRect.height() / 2 - mBorderPaint.strokeWidth / 2,
                leftSecondOffsetX + dp2px(17f),
                height - mTextRect.height() / 2 - mBorderPaint.strokeWidth / 2,
                mBorderPaint
            )
        }

    }

    /**
     * 每次生成5个点
     */
    private fun randomValue() {
        for (i in 0..4) {
            valueList.add((Math.random() * 30 + 1).toFloat())
        }
    }

    fun drawLine() {
        randomValue()
        val useWidth = mGridWidth * mGridWidthCount
        val useHeight = mGridHeight * mGridHeightCount
        mPath.reset()
        valueList.forEachIndexed { index, value ->
            mRealValue = value

            // 计算出横坐标每个像素的间隔
            val pixelInterval = useWidth / (mStageDuration / mDataInterval * mDataLength)

            // 计算出x坐标
            var x = index * pixelInterval + mBorderLineWidth * 2 + mOffsetLeftX
            if (x > useWidth + mBorderLineWidth / 2 + mOffsetLeftX) {
                x = useWidth + mBorderLineWidth / 2 + mOffsetLeftX
            }

            if (value >= mMaxValue) {
                mMaxValue = value / 0.7f
            }

            // 计算出y坐标
            val y =
                useHeight - (mRealValue - mMinValue) / (mMaxValue - mMinValue) * useHeight - mBorderLineWidth / 2 + mOffsetTopY


            Log.d(TAG_VIEW, "--------x=$x,y=$y")

            if (mPath.isEmpty) {
                mPath.moveTo(x, y)
            } else {
                val dx = abs(x - previousX)
                val dy = abs(y - previousY)
                if (dx >= 3 || dy >= 3) {
                    // 设置贝塞尔曲线的控制点为起点和终点的一半
                    val cX = (x + previousX) / 2
                    val cY = (y + previousY) / 2
                    // 二次贝塞尔曲线
                    //实现绘制贝塞尔平滑曲线；previousX, previousY为操作点，cX, cY为终点
                    mPath.quadTo(previousX, previousY, cX, cY);
                }
            }
            previousX = x
            previousY = y
            invalidate()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
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