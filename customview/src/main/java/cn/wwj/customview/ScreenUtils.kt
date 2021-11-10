package cn.wwj.customview

import android.content.res.Resources
import android.util.TypedValue


fun dp2px(value: Float, resources: Resources): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics
    )
}
