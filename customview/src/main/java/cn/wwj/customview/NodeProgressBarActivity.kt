package cn.wwj.customview

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import cn.wwj.customview.widget.NodePointProcessBar

/**
 * 节点进度Activity
 */
class NodeProgressBarActivity : AppCompatActivity() {

    /**
     * 数据结合
     */
    private val mTextList: List<String> = mutableListOf("提交申请", "商家处理", "寄回商品", "商家退款", "退款成功")

    /**
     * 正在处理的节点索引结合
     */
    private var mProgressIndexSet: Set<Int> = mutableSetOf(0, 1,2,3,4,6)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_node_progress_bar)
        val nodePointPb: NodePointProcessBar = findViewById(R.id.nodePointPb)

        Handler(Looper.getMainLooper()).postDelayed({
            nodePointPb.setNodeData(mTextList, mProgressIndexSet)
        }, 1000)
    }
}