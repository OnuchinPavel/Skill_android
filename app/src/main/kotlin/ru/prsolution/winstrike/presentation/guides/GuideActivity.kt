package ru.prsolution.winstrike.presentation.guides

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.fmt_guides.vp_pager
import ru.prsolution.winstrike.R

/*
 * Created by oleg on 31.01.2018.
 */

class GuideActivity : AppCompatActivity() {
    private val pagerAdapter: PagerAdapter? = null
    private val viewPager: ViewPager? = null
    private val GUIDE_COUNT = 3

    fun getViewPager(): ViewPager? {
        /*        ViewPagerCustomDuration vp = (ViewPagerCustomDuration) findViewById(R.id.vp_pager);
        vp.setScrollDurationFactor(4.0); // make the animation twice as slow
        return vp;*/
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fmt_guides)

        //        pagerAdapter = new GuidesFragmentPagerAdapter(getSupportFragmentManager());
        vp_pager!!.adapter = pagerAdapter
    }

    private inner class GuidesFragmentPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return GuideFragment.newInstance(position)
        }

        override fun getCount(): Int {
            return GUIDE_COUNT
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
