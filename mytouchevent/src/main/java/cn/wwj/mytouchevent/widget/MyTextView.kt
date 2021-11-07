package cn.wwj.mytouchevent.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import cn.wwj.mytouchevent.TAG_TOUCH_EVENT

class MyTextView(context: Context, attrs: AttributeSet?) : androidx.appcompat.widget.AppCompatTextView(context, attrs) {

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        val result= super.dispatchTouchEvent(event)
        Log.d(TAG_TOUCH_EVENT,"MyTextView super.dispatchTouchEvent(ev)=$result")
        return result
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val result= super.onTouchEvent(event)
        Log.d(TAG_TOUCH_EVENT,"MyTextView super.onTouchEvent(ev)=$result")
        return result
    }

}