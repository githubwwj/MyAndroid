package cn.wwj.android

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {


    var which = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ivBackground = findViewById<ImageView>(R.id.ivBackground)
        val btnGo = findViewById<View>(R.id.btnGo)

        btnGo.setOnClickListener {
            when (which) {
                0 -> {
                    which = 1
                    Glide.with(this)
                        .load("https://mobileapi.shanlomed.com/find_img?fileName=xl_fb_stimulate2.gif")
                        .fitCenter()
                        .into(ivBackground)
                }
                1 -> {
                    which = 2
                    Glide.with(this)
                        .load("https://mobileapi.shanlomed.com/find_img?fileName=xl_fb_sleep_10s.gif")
                        .fitCenter()
                        .into(ivBackground)
                }
                2 -> {
                    which = 0
                    Glide.with(this)
                        .load("https://mobileapi.shanlomed.com/find_img?fileName=xl_fb_10s_01.gif")
                        .fitCenter()
                        .into(ivBackground)
                }
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
    }

}