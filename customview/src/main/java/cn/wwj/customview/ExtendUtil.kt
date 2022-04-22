package cn.wwj.customview

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

/**
 * 通过Class跳转界面
 */
fun Activity.startActivityForResult(cls: Class<*>, requestCode: Int) {
    startActivityForResult(cls, requestCode, null)
}

/**
 * 含有Bundle通过Class跳转界面
 */
fun Activity.startActivityForResult(cls: Class<*>, requestCode: Int, bundle: Bundle?) {
    val intent = Intent()
    intent.setClass(this, cls)
    if (bundle != null) {
        intent.putExtras(bundle)
    }
    startActivityForResult(intent, requestCode)
}

fun View.setOnClickListener1(onClickListener: (View) -> Unit) {
    setOnClickListener {
        if (isFastClick()) {
            return@setOnClickListener
        }
        onClickListener.invoke(this)
    }
}


/**
 * 含有Bundle通过Class跳转界面
 * @param cls 跳转界面
 */
@JvmOverloads
fun Activity.startActivity(cls: Class<*>, bundle: Bundle? = null) {
    val intent = Intent()
    intent.setClass(this, cls)
    if (bundle != null) {
        intent.putExtras(bundle)
    }
    startActivity(intent)
}

/**
 * 获取字体所对应的像素
 */
fun getSp(sp: Float): Float {
    val resources = Resources.getSystem()
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.displayMetrics)
}

/**
 * 上一次点击的默认时间0
 */
var mLastClickTime: Long = 0
private const val MIN_CLICK_DELAY_TIME: Long = 600

/**
 * 是否快速点击  true 是  false 否
 * @param minClickDelayTime 单位毫秒
 */
fun isFastClick(minClickDelayTime: Long = MIN_CLICK_DELAY_TIME): Boolean {
    val currentTime = System.currentTimeMillis()
    if (currentTime - mLastClickTime > minClickDelayTime) {
        mLastClickTime = currentTime
        return false
    }
    return true
}

fun Int.sp2px(): Int {
    val displayMetrics = Resources.getSystem().displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.toFloat(), displayMetrics)
        .toInt()
}

fun Float.sp2px(): Float {
    val displayMetrics = Resources.getSystem().displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, displayMetrics)
}

fun Int.dp2px(): Int {
    val displayMetrics = Resources.getSystem().displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), displayMetrics)
        .toInt()
}

fun Float.dp2px(): Float {
    val displayMetrics = Resources.getSystem().displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, displayMetrics)
}


/**
 * 倒计时的协程
 */
fun countDownCoroutines(
    total: Int,
    scope: CoroutineScope,
    onTick: ((Int) -> Unit)? = null,
    onStart: (() -> Unit)? = null,
    onFinish: (() -> Unit)? = null,
    intervalTime: Long = 1000,
): Job {
    return flow {
        for (i in (total - 1) downTo 0) {
            emit(i)
            delay(intervalTime)
        }
    }.flowOn(Dispatchers.IO)
        .onStart { onStart?.invoke() }
        .onCompletion { onFinish?.invoke() }
        .onEach { onTick?.invoke(it) }
        .launchIn(scope)
}