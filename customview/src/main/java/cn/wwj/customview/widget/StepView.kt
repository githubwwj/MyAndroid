package cn.wwj.customview.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.widget.AppCompatTextView
import cn.wwj.customview.R
import cn.wwj.customview.dp2px


/**
 * 用圆弧/或圆圈统计跑步器
 */
class StepView : AppCompatTextView {

    /**
     * 开始颜色
     */
    private var mStartColor: Int = -1

    /**
     * 中间颜色
     */
    private var mCenterColor: Int = -1

    /**
     * 结束颜色
     */
    private var mEndColor: Int = -1

    /**
     * 当前走了多少步
     */
    private var mCurrentStep: Int = 0

    /**
     * 最大走多少步,比如两万步 20000步
     * 默认100步,有个成语50步笑100步
     */
    private var mMaxStep: Int = 100

    /**
     * 是否显示步长
     */
    private var mIsShowStep: Boolean = true
        set(value) {
            if (field == value) {
                return
            }
            field = value
            invalidate()
        }


    /**
     * 单位: 如 步  %  等等
     */
    var mUnit: String?
        set(value) {
            if (field == value) {
                return
            }
            field = value
            invalidate()
        }

    /**
     * 设置圆弧的边框线宽度
     */
    var mBorderWidth: Float = dp2px(6F)
        set(value) {
            if (field == value) {
                return
            }
            field = value
            // 设置描边的宽度
            mArcPaint.strokeWidth = field
            invalidate()
        }

    /**
     * 设置外部圆弧的颜色
     */
    private var mOuterColor: Int = resources.getColor(android.R.color.holo_blue_light)

    /**
     * 设置内部圆弧的颜色
     */
    private var mInnerColor: Int = resources.getColor(android.R.color.holo_red_light)

    /**
     * 圆弧画笔
     */
    private var mArcPaint: Paint = Paint()

    /**
     * 弧线画笔
     */
    private var mArcLinePaint: Paint = Paint()

    /**
     * 弧线颜色
     */
    private var mArcLineColor: Int = -1

    /**
     * 弧线宽度
     */
    private var mArcLineWidth = dp2px(1f)


    /**
     * 文本画笔，用于绘画走了多少步
     */
    private var mTextPaint: Paint = Paint()


    /**
     * 日志过滤标签
     */
    private val TAG = javaClass.simpleName

    /**
     * 建议圆弧起始角度
     */
    private val mSuggestionStartAngle = 135F


    /**
     * 建议圆弧从起始角度开始，扫描过的角度
     */
    private var mSuggestionSweepAngle = mSuggestionStartAngle * 2

    /**
     * 圆弧起始角度
     */
    private var mStartAngle = mSuggestionStartAngle

    /**
     * 圆弧从起始角度开始，扫描过的角度,超过360度,变为一个圆圈
     */
    private var mSweepAngle = mSuggestionSweepAngle

    /**
     *  比如用于获取一个圆弧的矩形,onDraw()方法会调用多次,不必每次都创建Rect()对象
     */
    private val mArcRect = RectF()

    /**
     *  比如用于获取文字的大小,onDraw()方法会调用多次,不必每次都创建Rect()对象
     *  用同一个对象即可
     */
    private val mTextRect = Rect()

    /**
     * 值动画师
     */
    private var valueAnimator: ValueAnimator? = null

    /**
     * 是否是圆圈进度条,默认false
     */
    private var mIsCircle: Boolean = true

    /**
     * 最大进度
     */
    private val MAX_PROGRESS = 100F

    constructor(context: Context)
            : this(context, null)

    constructor(context: Context, attrs: AttributeSet?)
            : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        val appearance = context.obtainStyledAttributes(attrs, R.styleable.StepView)

        mBorderWidth = appearance.getDimension(R.styleable.StepView_borderWidth, mBorderWidth)
        mOuterColor = appearance.getColor(R.styleable.StepView_outColor, mOuterColor)
        mInnerColor = appearance.getColor(R.styleable.StepView_innerColor, mInnerColor)
        mStartColor = appearance.getColor(R.styleable.StepView_startColor, mStartColor)
        mCenterColor = appearance.getColor(R.styleable.StepView_centerColor, mCenterColor)
        mEndColor = appearance.getColor(R.styleable.StepView_endColor, mEndColor)
        mUnit = appearance.getString(R.styleable.StepView_unit)
        mCurrentStep = appearance.getInt(R.styleable.StepView_currentStep, 0)
        mMaxStep = appearance.getInt(R.styleable.StepView_maxStep, mMaxStep)
        mStartAngle = appearance.getFloat(R.styleable.StepView_startAngle, mStartAngle)
        mSweepAngle = appearance.getFloat(R.styleable.StepView_endAngle, mSweepAngle)
        mArcLineColor = appearance.getColor(R.styleable.StepView_arcLineColor, mArcLineColor)
        mArcLineWidth = appearance.getDimension(R.styleable.StepView_arcLineWidth, mArcLineWidth)
        setIsCircle(appearance.getBoolean(R.styleable.StepView_isCircle, false))
        mIsShowStep = appearance.getBoolean(R.styleable.StepView_isShowStep, mIsShowStep)

        appearance.recycle()

        setPaint()
    }


    /**
     * 设置 圆弧画笔用于绘制圆弧 和 文本画笔，用于绘画走了多少步
     */
    private fun setPaint() {
        // 抗抖动
        mArcPaint.isDither = true

        // 抗锯齿
        mArcPaint.isAntiAlias = true

        // 画笔的样式描边,笔划突出为半圆
        mArcPaint.style = Paint.Style.STROKE

        // 当画笔样式为STROKE或FILL_OR_STROKE时，设置笔刷的图形样式，如圆形样式
        // Cap.ROUND(圆形样式)或Cap.SQUARE(方形样式)
        mArcPaint.strokeCap = Paint.Cap.ROUND

        // 设置画笔粗细
        mArcPaint.strokeWidth = mBorderWidth

        // 画笔的颜色
        mArcPaint.color = mOuterColor


        //------------弧线画笔
        // 抗抖动
        mArcLinePaint.isDither = true

        // 抗锯齿
        mArcLinePaint.isAntiAlias = true

        // 画笔的样式描边,笔划突出为半圆
        mArcLinePaint.style = Paint.Style.STROKE

        // 当画笔样式为STROKE或FILL_OR_STROKE时，设置笔刷的图形样式，如圆形样式
        // Cap.ROUND(圆形样式)或Cap.SQUARE(方形样式)
        mArcLinePaint.strokeCap = Paint.Cap.ROUND

        // 设置画笔粗细
        mArcLinePaint.strokeWidth = mArcLineWidth

        // 画笔的颜色
        mArcLinePaint.color = mArcLineColor
        //------------弧线画笔


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

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Log.d(TAG, "-------------onLayout")
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.d(TAG, "-------onSizeChanged---w=$w,h=$h")
        if (mStartColor != -1 && mEndColor != -1) {
            val colorArray = arrayListOf(mStartColor, mCenterColor, mEndColor).toIntArray()
            val linearGradient = LinearGradient(
                0F, height * 1F, width * 1F, height * 1F,
                colorArray, null, Shader.TileMode.CLAMP
            )
            mArcPaint.shader = linearGradient
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        /**
         * 获取控件的宽高
         */
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        Log.d(TAG, "-------onMeasure---w=$widthSize,h=$heightSize")

        /**
         * 如果宽度大于高度,取高度
         * 否则取宽度
         */
        val result = if (widthSize > heightSize) {
            heightSize
        } else {
            widthSize
        }

        /**
         * 设置控件的宽高
         */
        setMeasuredDimension(result, result)
    }

    override fun onDraw(canvas: Canvas?) {

        // 将矩形设置为 (0,0,0,0)
        mArcRect.setEmpty()
        mTextRect.setEmpty()

        // 圆弧矩形左边距,顶边距,右边距,底边距
        var left = mBorderWidth / 2
        var top = mBorderWidth / 2
        var right = width - mBorderWidth / 2
        var bottom = height - mBorderWidth / 2
        mArcRect.set(left, top, right, bottom)

        // 绘制外部圆弧
//        mArcPaint.color = mOuterColor
//        canvas?.drawArc(mArcRect, mStartAngle, mSweepAngle, false, mArcPaint)


        // 绘制外部弧线
        left = mArcLineWidth / 2
        top = mArcLineWidth / 2
        right = width - mArcLineWidth / 2
        bottom = height - mArcLineWidth / 2
        mArcRect.set(left, top, right, bottom)
        canvas?.drawArc(mArcRect, mStartAngle, mSweepAngle, false, mArcLinePaint)

        // 绘制内部弧线
        left = mArcLineWidth / 2 + dp2px(20F)
        top = mArcLineWidth / 2 + dp2px(20F)
        right = width - mArcLineWidth / 2 - dp2px(20F)
        bottom = height - mArcLineWidth / 2 - dp2px(20F)
        mArcRect.set(left, top, right, bottom)
        canvas?.drawArc(mArcRect, mStartAngle, mSweepAngle, false, mArcLinePaint)

        // 绘制内部部圆弧
        val sweepAngle = mCurrentStep * 1F / mMaxStep * mSweepAngle
//        mArcPaint.color = mInnerColor

        left = mBorderWidth / 2
        top = mBorderWidth / 2
        right = width - mBorderWidth / 2
        bottom = height - mBorderWidth / 2
        mArcRect.set(left, top, right, bottom)
        canvas?.drawArc(mArcRect, mStartAngle, sweepAngle, false, mArcPaint)


        Log.d(TAG, "-------onDraw---mStartAngle=$mStartAngle---sweepAngle=$sweepAngle")

        if (mIsShowStep) {
            val stepText = if (mUnit != null) {
                "$mCurrentStep $mUnit"
            } else {
                mCurrentStep.toString()
            }

            // 获取文本的宽高
            mTextPaint.getTextBounds(stepText, 0, stepText.length, mTextRect)
            val textX = width / 2F - mTextRect.width() / 2
            val textY = height / 2F + getBaseline(mTextPaint)
            // 绘制文本,第二个参数文本的起始索引,第三个参数要绘制的文字长度
            // 开始绘制文字的x 坐标 y 坐标
            canvas?.drawText(stepText, 0, stepText.length, textX, textY, mTextPaint)
        }
    }

    /**
     * @param progress 进入0-100 之间
     * @param duration 动画时长,默认 350毫秒
     */
    fun setProgress(progress: Int, duration: Long = 350) {
        valueAnimator?.cancel()
        valueAnimator = null
        val step = (progress / MAX_PROGRESS * mMaxStep).toInt()
        valueAnimator = ValueAnimator.ofInt(mCurrentStep, step.coerceAtMost(mMaxStep))
        valueAnimator?.duration = duration
        valueAnimator?.interpolator = AccelerateInterpolator()
        valueAnimator?.addUpdateListener {
            mCurrentStep = it.animatedValue as Int
            Log.d(TAG, "------$mCurrentStep")
            invalidate()
        }
        valueAnimator?.startDelay = 10
        valueAnimator?.start()
    }

    /**
     * @param maxStep  最多走多少步，比如2000步
     * @param duration 默认动画时长200
     */
    fun setMaxStep(maxStep: Int, duration: Long = 0) {
        mMaxStep = maxStep
        val progress = (mCurrentStep * 1F / mMaxStep * 100).toInt()
        setProgress(progress, duration)
    }

    /**
     * @param currentStep 当前走了多少步
     * @param duration 默认动画时长200
     */
    fun setCurrentStep(currentStep: Int, duration: Long = 200) {
        mCurrentStep = currentStep
        val progress = (mCurrentStep * 1F / mMaxStep * 100).toInt()
        setProgress(progress, duration)
    }

    /**
     * @param outerColor
     * 设置外部圆弧的颜色
     */
    fun setOuterColor(outerColor: Int) {
        if (mOuterColor == outerColor) {
            return
        }
        mOuterColor = outerColor
        invalidate()
    }

    /**
     * @param outerColor
     * 设置外部圆弧的颜色
     */
    fun setInnerColor(innerColor: Int) {
        if (mInnerColor == innerColor) {
            return
        }
        mInnerColor = innerColor
        invalidate()
    }

    /**
     * 设置是否是圆圈
     */
    fun setIsCircle(isCircle: Boolean = false) {
        if (isCircle == mIsCircle) {
            return
        }
        this.mIsCircle = isCircle
        if (isCircle) {
            mStartAngle = 0F
            // 超过360度,变为一个圆圈
            mSweepAngle = 360F
        }
        invalidate()
    }

    /**
     * 圆弧起始角度
     */
    fun setStartAngle(startAngle: Float) {
        mStartAngle = startAngle
        invalidate()
    }

    /**
     * 圆弧从起始角度开始，扫描过的角度,超过360度,变为一个圆圈
     */
    fun setSweepAngle(sweepAngle: Float) {
        mSweepAngle = sweepAngle
        invalidate()
    }

    /**
     * 视图从窗口分离时
     */
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        valueAnimator?.cancel()
        valueAnimator = null
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