package cn.wwj.mytouchevent.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.ListView
import kotlin.math.abs

class MyListView(context: Context?, attrs: AttributeSet?) :
    ListView(context, attrs) {

    private val TAG = "ON_TOUCH"
    private var lastX = 0F
    private var lastY = 0F

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val x = ev.x
        val y = ev.y
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                parent?.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                if (abs(x - lastX) > abs(y - lastY)) {
                    parent?.requestDisallowInterceptTouchEvent(false)
                }
            }
            MotionEvent.ACTION_UP -> {

            }
        }
        lastX = x
        lastY = y

        Log.d(TAG, "-------ListView dispatchTouchEvent")
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        Log.d(TAG, "-------ListView onInterceptTouchEvent")
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        Log.d(TAG, "-------ListView onTouchEvent")
        return super.onTouchEvent(ev)
    }


}