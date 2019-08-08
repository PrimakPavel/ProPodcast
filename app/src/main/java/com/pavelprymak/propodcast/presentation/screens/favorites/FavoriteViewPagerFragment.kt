package com.pavelprymak.propodcast.presentation.screens.favorites


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
import kotlinx.android.synthetic.main.fragment_favorite_pager.*

class FavoriteViewPagerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (activity is MainActivity) {
            (activity as MainActivity).setNavViewVisibility(true)
        }
        return inflater.inflate(R.layout.fragment_favorite_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = FavoritesPagerAdapter(childFragmentManager)
        viewPagerFavorites.adapter = adapter
        tabLayout.setupWithViewPager(viewPagerFavorites)
        viewPagerFavorites.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        })
        toolbar.setNavigationOnClickListener { v ->
            activity?.onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewPagerFavorites.clearOnPageChangeListeners()
    }

    private inner class FavoritesPagerAdapter internal constructor(fm: FragmentManager) :
        FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return if (position == 0) {
                FavoritePodcastsFragment.newInstance()
            } else FavoriteEpisodesFragment.newInstance()
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return if (position == 0) {
                getString(R.string.favorite_podcats_title)
            } else getString(R.string.favorite_episodes_title)
        }


        override fun getCount(): Int {
            return 2
        }
    }

}
