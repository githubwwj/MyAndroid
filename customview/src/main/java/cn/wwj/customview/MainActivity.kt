package cn.wwj.customview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.wwj.customview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var mProgress = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.stepView.setOnClickListener {
            mProgress += 20
            binding.stepView.setProgress(mProgress,800)
            if (mProgress >= 100) {
                mProgress = 0
            }
        }
        binding.stepView.setMaxStep(2000)
    }

}