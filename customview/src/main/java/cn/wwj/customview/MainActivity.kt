package cn.wwj.customview

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.wwj.customview.databinding.ActivityMainBinding
import cn.wwj.customview.widget.LetterSlideBar

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var mProgress = 0
    private var isCircle = false


    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val intent = Intent("com.test.intentfilter")
//        intent.addCategory(Intent.CATEGORY_DEFAULT)
        val resolveActivity = intent.resolveActivity(packageManager)
        if (null != resolveActivity) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "未找到Activity", Toast.LENGTH_SHORT).show()
        }

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

    }

}