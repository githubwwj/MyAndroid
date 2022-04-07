package cn.wwj.customview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.wwj.customview.widget.WaveView

/**
 * 波浪Activity
 */
class WaveRippleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wave_ripple)
        val waveView: WaveView = findViewById(R.id.waterRipple)
    }
}