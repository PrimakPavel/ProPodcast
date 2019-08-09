package com.pavelprymak.propodcast.presentation.screens.filters


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.pavelprymak.propodcast.MainActivity
import com.pavelprymak.propodcast.R
import kotlinx.android.synthetic.main.fragment_filters_view_pager.*


class FiltersViewPagerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (activity is MainActivity) {
            if (!resources.getBoolean(R.bool.isTablet)) {
                (activity as MainActivity).setNavViewVisibility(false)
            }
        }
        return inflater.inflate(R.layout.fragment_filters_view_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = FiltersPagerAdapter(childFragmentManager)
        viewPagerFilters.adapter = adapter
        tabLayout.setupWithViewPager(viewPagerFilters)
        val genreTab = tabLayout.getTabAt(0)
        val regionTab = tabLayout.getTabAt(1)
        genreTab?.setIcon(R.drawable.ic_baseline_category)
        regionTab?.setIcon(R.drawable.ic_baseline_region)
        viewPagerFilters.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        })
        // Back btn init
        toolbar.setNavigationOnClickListener { activity?.onBackPressed()}
        if (resources.getBoolean(R.bool.isTablet)) {
            toolbar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewPagerFilters.clearOnPageChangeListeners()
    }

    private inner class FiltersPagerAdapter internal constructor(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return if (position == 0) {
                GenreFilterFragment.newInstance()
            } else RegionFilterFragment.newInstance()
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return if (position == 0) {
                getString(R.string.filters_genre_title)
            } else getString(R.string.filters_region_title)
        }

        override fun getCount(): Int {
            return 2
        }
    }
}
