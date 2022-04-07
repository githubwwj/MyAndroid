package cn.wwj.android

import android.app.Service
import android.content.Intent
import android.os.IBinder

class TestService : Service() {

    inner class MathServiceImpl : IMathService.Stub() {
        override fun add(a: Long, b: Long): Long {
            return a + b
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return MathServiceImpl()
    }


}