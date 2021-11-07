package cn.wwj.mytouchevent

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter

class TabFragmentPagerAdapter(
    val mfragmentManager: FragmentManager,
    val mlist: List<Fragment>
) : FragmentPagerAdapter(mfragmentManager) {

    override fun getItem(arg0: Int): Fragment {
        return mlist[arg0] //显示第几个页面
    }

    override fun getCount(): Int {
        return mlist.size //有几个页面
    }

}