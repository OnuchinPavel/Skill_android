package ru.prsolution.winstrike.presentation.profile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * Created by Oleg Sitnikov on 2019-01-28
 */

class TabAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm!!) {
    private val fragments = mutableListOf<Fragment>()
    private val mFragmentTitleList = mutableListOf<String>()

    fun addFragments(fragment: Fragment) {
        fragments.add(fragment)
    }

    fun addFragments(fragment: Fragment, title: String) {
        fragments.add(fragment)
        mFragmentTitleList.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList[position]
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }
}