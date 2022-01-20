package com.wwj.cououtines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.wwj.cououtines.viewmodel.MainVM
import kotlin.coroutines.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myCoroutine = suspend {
            5
        }.createCoroutine(object : Continuation<Int> {
            override val context: CoroutineContext
                get() = EmptyCoroutineContext

            override fun resumeWith(result: Result<Int>) {
                Log.d("tag", "conoutines end")
            }
        })

        myCoroutine.resume(Unit)


    }
}