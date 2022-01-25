package cn.wwj.customview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cn.wwj.customview.widget.BioFeedbackView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

/**
 * 生物反馈的Activity
 */
class BioFeedBackActivity : AppCompatActivity() {

    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bio_feedback)
        val bioFeedbackView: BioFeedbackView = findViewById(R.id.waterRipple)
        bioFeedbackView.mStageDuration = 8
        bioFeedbackView.mDataInterval = 0.2f

        bioFeedbackView.onLayoutListener = {
            job = countDownCoroutines(40, lifecycleScope, {
                bioFeedbackView.drawLine()
            }, {

            })
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
        job = null
    }

    private fun countDownCoroutines(
        total: Int,
        scope: CoroutineScope,
        onTick: (Int) -> Unit,
        onFinish: (() -> Unit)? = null,
    ): Job {
        return flow {
            for (i in (total - 1) downTo 0) {
                emit(i)
                delay(100)
            }
        }.flowOn(Dispatchers.Default)
            .onCompletion { onFinish?.invoke() }
            .onEach { onTick.invoke(it) }
            .launchIn(scope)
    }


}