package cn.wwj.customview

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.wwj.customview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var mProgress = 0
    private var isCircle = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.stepView.setOnClickListener {
            mProgress += 50
            if (mProgress > 111) {
                mProgress = 0
                isCircle = !isCircle
                binding.stepView.setIsCircle(isCircle)
            }
            binding.stepView.setProgress(mProgress, 800)
        }
        binding.stepView.setMaxStep(2000)

    }

}