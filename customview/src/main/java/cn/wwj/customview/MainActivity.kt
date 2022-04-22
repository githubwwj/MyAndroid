package cn.wwj.customview

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import cn.wwj.customview.databinding.ActivityMainBinding
import cn.wwj.customview.widget.LetterSlideBar

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var mProgress = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.cureProgressView.setOnClickListener {
//            mProgress += 100
//            if (mProgress > 100) {
//                mProgress = 0
//            }
//            binding.stepView.setProgress(mProgress, 800)
            setProgress()
        }
        binding.cureProgressView.setMaxStep(30)

        binding.tvSlideBar.setOnLetterTouchListener(object : LetterSlideBar.LetterTouchListener {
            override fun setLetterTouchListener(letter: String?) {
                if (letter.isNullOrEmpty()) {
                    binding.tvSelectLetter.visibility = View.GONE
                } else {
                    binding.tvSelectLetter.visibility = View.VISIBLE
                    binding.tvSelectLetter.text = letter
                }
            }
        })

//        startActivity(Intent(this,WaveRippleActivity::class.java))
//        startActivity(Intent(this, BioFeedBackActivity::class.java))
        startActivity(Intent(this, NodeProgressBarActivity::class.java))
        finish()

    }

    fun setProgress() {
        val valueAnimator = ValueAnimator.ofInt(0, 30)
        valueAnimator?.duration = 3000
        valueAnimator?.interpolator = AccelerateInterpolator()
        valueAnimator?.addUpdateListener {
            val value = it.animatedValue as Int
            binding.cureProgressView.setCurrentStep(value)
        }
        valueAnimator?.startDelay = 0
        valueAnimator?.start()
    }

}