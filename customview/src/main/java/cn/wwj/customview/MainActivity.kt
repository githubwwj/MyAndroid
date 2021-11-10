package cn.wwj.customview

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cn.wwj.customview.databinding.ActivityMainBinding
import cn.wwj.customview.widget.LetterSlideBar

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

        binding.tvSlideBar.setOnLetterListener(object : LetterSlideBar.LetterTouchListener {
            override fun setLetterTouchListener(letter: String?) {
                if (letter.isNullOrEmpty()) {
                    binding.tvSelectLetter.visibility = View.GONE
                } else {
                    binding.tvSelectLetter.visibility = View.VISIBLE
                    binding.tvSelectLetter.text = letter
                }
            }
        })

    }

}