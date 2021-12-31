package cn.wwj.customview.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import androidx.appcompat.widget.AppCompatImageView
import cn.wwj.customview.R
import cn.wwj.customview.dp2px

/**
 * 波浪
 */
class WaveView : AppCompatImageView {

    /**
     * 是否自动开始波浪
     */
    private var isAutoStart = true

    /**
     * 波浪线画笔
     */
    private var mWavePaint: Paint = Paint()

    /**
     * 设置波浪线宽度
     */
    var mWaveBorderWidth: Float = dp2px(10F).toFloat()
        set(value) {
            if (field == value) {
                return
            }
            field = value
            // 设置描边的宽度
            mWavePaint.strokeWidth = field
            invalidate()
        }


    /**
     * 设置波浪线的最小半径是中心图片的最小半径
     */
    private var mMinRadius: Float = 0F

    /**
     * 三个波浪线,三个水波纹动画
     */
    private var firstValueAnimator: ValueAnimator? = null
    private var secondValueAnimator: ValueAnimator? = null
    private var thirdValueAnimator: ValueAnimator? = null

    /**
     * 第一个圆圈半径
     */
    private var mFirstCircleRadius: Float = 10F

    /**
     * 第二个圆圈半径
     */
    private var mSecondCircleRadius: Float = dp2px(10F).toFloat()

    /**
     * 第二个圆圈半径
     */
    private var mThirdCircleRadius: Float = 0F


    /**
     * 圆的最大半径
     */
    private var mMaxRadius: Float = 0f

    /**
     * 设置外部圆弧的颜色
     */
    private var mWaveColor: Int = resources.getColor(R.color.wave)

    constructor(context: Context)
            : this(context, null)

    constructor(context: Context, attrs: AttributeSet?)
            : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        val appearance = context.obtainStyledAttributes(attrs, R.styleable.WaveView)

        mWaveColor = appearance.getColor(R.styleable.WaveView_waveColor, mWaveColor)
        mWaveBorderWidth =
            appearance.getDimension(R.styleable.WaveView_waveBorderWidth, mWaveBorderWidth)
        mFirstCircleRadius =
            appearance.getDimension(R.styleable.WaveView_waveRadius, mFirstCircleRadius)

        appearance.recycle()

        setPaint()
    }

    /**
     * 设置 圆弧画笔用于绘制圆弧 和 文本画笔，用于绘画走了多少步
     */
    private fun setPaint() {
        // 画笔的颜色
        mWavePaint.color = mWaveColor

        // 抗抖动
        mWavePaint.isDither = true

        // 抗锯齿
        mWavePaint.isAntiAlias = true

        // 画笔的样式描边,笔划突出为半圆
        mWavePaint.style = Paint.Style.STROKE

        // 设置描边的线帽样式
        mWavePaint.strokeCap = Paint.Cap.ROUND

        // 设置描边的宽度
        mWavePaint.strokeWidth = mWaveBorderWidth
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        /**
         * 获取控件的宽高
         */
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        /**
         * 如果宽度小于高度,取高度
         * 否则取宽度
         */
        val result = if (widthSize < heightSize) {
            heightSize
        } else {
            widthSize
        }

        mMaxRadius = (result / 2).toFloat() + mWaveBorderWidth
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        //最小半径 =  图片宽度的一半 减去 波浪线宽度的一半
        // 或者波浪线的一半
        mMinRadius = if (drawable != null) {
            drawable.intrinsicWidth.toFloat() / 2 + mWaveBorderWidth / 2
        } else {
            mFirstCircleRadius
        }
        if (isAutoStart) {
            startWave()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (firstValueAnimator?.isStarted == true) {
            mWavePaint.alpha = (255 - (mFirstCircleRadius - mMinRadius) / mMaxRadius * 255).toInt()
            canvas?.drawCircle(
                (width / 2).toFloat(), (height / 2).toFloat(),
                mFirstCircleRadius, mWavePaint
            )
        }

        if (secondValueAnimator?.isStarted == true) {
            mWavePaint.alpha = (255 - (mSecondCircleRadius - mMinRadius) / mMaxRadius * 255).toInt()
            canvas?.drawCircle(
                (width / 2).toFloat(), (height / 2).toFloat(),
                mSecondCircleRadius, mWavePaint
            )
        }

        if (thirdValueAnimator?.isStarted == true) {
            mWavePaint.alpha = (255 - (mThirdCircleRadius - mMinRadius) / mMaxRadius * 255).toInt()
            canvas?.drawCircle(
                (width / 2).toFloat(), (height / 2).toFloat(),
                mThirdCircleRadius, mWavePaint
            )
        }

    }

    /**
     * 开始波浪动画
     * @param duration 动画时长,默认 4000毫秒
     */
    fun startWave(duration: Long = 4000) {
        if (firstValueAnimator?.isStarted == true || mMinRadius <= 0) {
            return
        }

        stopWave()
        firstValueAnimator = ValueAnimator.ofFloat(mMinRadius, mMaxRadius)
        firstValueAnimator?.duration = duration
        firstValueAnimator?.interpolator = LinearInterpolator()
        firstValueAnimator?.repeatMode = ValueAnimator.RESTART
        firstValueAnimator?.repeatCount = ValueAnimator.INFINITE
        firstValueAnimator?.addUpdateListener {
            mFirstCircleRadius = it.animatedValue as Float
            if ((mFirstCircleRadius > ((mMaxRadius - mMinRadius) / 3 + mMinRadius))
                && secondValueAnimator?.isStarted == false
            ) {
                //itemHeight=181px    362px     543px
                secondValueAnimator?.start()
            }
            invalidate()
        }
        firstValueAnimator?.start()

        secondValueAnimator = ValueAnimator.ofFloat(mMinRadius, mMaxRadius)
        secondValueAnimator?.duration = duration
        secondValueAnimator?.interpolator = LinearInterpolator()
        secondValueAnimator?.repeatMode = ValueAnimator.RESTART
        secondValueAnimator?.repeatCount = ValueAnimator.INFINITE
        secondValueAnimator?.addUpdateListener {
            mSecondCircleRadius = it.animatedValue as Float
            if ((mSecondCircleRadius > ((mMaxRadius - mMinRadius) / 3 + mMinRadius))
                && thirdValueAnimator?.isStarted == false
            ) {
                thirdValueAnimator?.start()
            }
        }

        thirdValueAnimator = ValueAnimator.ofFloat(mMinRadius, mMaxRadius)
        thirdValueAnimator?.duration = duration
        thirdValueAnimator?.interpolator = LinearInterpolator()
        thirdValueAnimator?.repeatMode = ValueAnimator.RESTART
        thirdValueAnimator?.repeatCount = ValueAnimator.INFINITE
        thirdValueAnimator?.addUpdateListener {
            mThirdCircleRadius = it.animatedValue as Float
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopWave()
    }

    /**
     * 取消波浪动画
     */
    fun stopWave(isAutoStart: Boolean = true) {
        this.isAutoStart = isAutoStart
        firstValueAnimator?.cancel()
        firstValueAnimator = null

        secondValueAnimator?.cancel()
        secondValueAnimator = null

        thirdValueAnimator?.cancel()
        thirdValueAnimator = null
    }

}