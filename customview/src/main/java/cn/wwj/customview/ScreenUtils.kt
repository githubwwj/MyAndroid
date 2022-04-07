package cn.wwj.customview

import android.content.res.Resources
import android.util.TypedValue


fun dp2px(value: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, value, Resources.getSystem().displayMetrics
    )
}


fun sp2px(value: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP, value, Resources.getSystem().displayMetrics
    )
}