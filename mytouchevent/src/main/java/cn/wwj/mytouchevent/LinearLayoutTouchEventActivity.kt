package cn.wwj.mytouchevent

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import cn.wwj.mytouchevent.databinding.ActivityLinearTouchEventBinding

class LinearLayoutTouchEventActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLinearTouchEventBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.ll.setOnClickListener {
            Log.d(TAG_TOUCH_EVENT,"ll onClickListener")
        }

        binding.tv.setOnClickListener {
            Log.d(TAG_TOUCH_EVENT,"tv onClickListener")
        }

    }
}