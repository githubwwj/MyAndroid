package cn.wwj.android

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import java.io.File

class MainActivity : AppCompatActivity() {

    var which = 0
    @SuppressLint("CheckResult")
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

}