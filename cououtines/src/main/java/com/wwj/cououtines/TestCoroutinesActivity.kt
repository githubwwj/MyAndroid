package com.wwj.cououtines

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import java.lang.Exception
import java.lang.IllegalArgumentException
import kotlin.coroutines.*
import kotlin.system.measureTimeMillis

class TestCoroutinesActivity : AppCompatActivity() {

    lateinit var tv: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv = findViewById<TextView>(R.id.tv)

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

        lifecycleScope.launch {
            val duration = measureTimeMillis {
                val one = async {
                    doOne()
                }
                val two = async {
                    doTwo()
                }
                tv.text = "${one.await() + two.await()}"
            }
            tv.text = "${tv.text},时长=$duration,线程=${Thread.currentThread().name}"

            delay(2000)

            val jobDefault =
                launch(context = Dispatchers.Default, start = CoroutineStart.DEFAULT) {
                    var i = 0
                    try {
                        while (true) {
                            delay(500)
                            Log.d(
                                javaClass.simpleName,
                                "------------" + Thread.currentThread().name
                            )
                            if (i > 2) {
                                throw IllegalArgumentException("挂了")
                            }
                            withContext(Dispatchers.Main) {
                                tv.text = "${i++},线程=${Thread.currentThread().name}"
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            delay(3000)
            val cr = jobDefault.cancel()
            tv.text = "是否取消=${jobDefault.isActive}"
            Toast.makeText(
                this@TestCoroutinesActivity,
                "${jobDefault.isActive}",
                Toast.LENGTH_SHORT
            )
                .show()

        }
        Log.d("TAG", "------------------$localClassName-----------")


//        CoroutineScope(Job())
        val scope = CoroutineScope(SupervisorJob())
        scope.launch {
            Log.d("TAG", "------------------${Thread.currentThread().name}-----------")
        }

        scope.launch {
            Log.d("TAG", "------------------${Thread.currentThread().name}-----------")
            val myJob = coroutineContext[Job]
            Log.d("TAG", "------------------${myJob?.isActive}-----------")
        }
        scope.cancel()

        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            Log.d("TAG", "------------------${exception}-----------")
        }

        lifecycleScope.launch(exceptionHandler) {

        }

    }

    private suspend fun doOne(): Int {
        tv.text = Thread.currentThread().name
        delay(3000)
        return 15
    }

    private suspend fun doTwo(): Int {
        delay(3000)
        return 20
    }

}