package cn.wwj.mytouchevent.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class MyViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {

    private val TAG = "ON_TOUCH"

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.d(TAG,"-------MyViewPager dispatchTouchEvent")
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        Log.d(TAG,"-------MyViewPager onInterceptTouchEvent")
//        return super.onInterceptTouchEvent(ev)
        if(ev.action == MotionEvent.ACTION_DOWN){
            super.onInterceptTouchEvent(ev)
            return false
        }
        return true
//        return super.onInterceptTouchEvent(ev)
//        return false
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        Log.d(TAG,"-------MyViewPager onTouchEvent")
        return super.onTouchEvent(ev)
    }


}