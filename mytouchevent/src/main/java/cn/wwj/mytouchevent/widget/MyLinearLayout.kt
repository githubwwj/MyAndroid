package cn.wwj.mytouchevent.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.LinearLayout
import cn.wwj.mytouchevent.TAG_TOUCH_EVENT

class MyLinearLayout(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val result= super.dispatchTouchEvent(ev)
        Log.d(TAG_TOUCH_EVENT,"MyLinearLayout super.dispatchTouchEvent(ev)=$result")
        return result
    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        val result= super.onInterceptTouchEvent(event)
        Log.d(TAG_TOUCH_EVENT,"MyLinearLayout super.onInterceptTouchEvent(ev)=$result")
        return result
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val result= super.onTouchEvent(event)
        Log.d(TAG_TOUCH_EVENT,"MyLinearLayout super.onTouchEvent(ev)=$result")
        return result
    }

}