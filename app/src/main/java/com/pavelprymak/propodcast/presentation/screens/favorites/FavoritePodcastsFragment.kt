package com.pavelprymak.propodcast.presentation.screens.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pavelprymak.propodcast.MainActivity
import com.pavelprymak.propodcast.R
import com.pavelprymak.propodcast.model.db.FavoritePodcastEntity
import com.pavelprymak.propodcast.presentation.adapters.FavoritePodcastAdapter
import com.pavelprymak.propodcast.presentation.adapters.FavoritePodcastClickListener
import com.pavelprymak.propodcast.presentation.screens.ARG_PODCAST_ID
import com.pavelprymak.propodcast.presentation.viewModels.FavoritePodcastsViewModel
import com.pavelprymak.propodcast.utils.ShareUtil
import kotlinx.android.synthetic.main.fragment_favorite_items.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.util.*

class FavoritePodcastsFragment : Fragment(), FavoritePodcastClickListener {
    private val mFavoritesViewModel: FavoritePodcastsViewModel by sharedViewModel()
    private var mNavController: NavController? = null

    private var mAdapter: FavoritePodcastAdapter? = null
    private val mFavorites = ArrayList<FavoritePodcastEntity>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorite_items, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity is MainActivity) {
            mNavController = (activity as MainActivity).navController
        }
        prepareRecycler()
        mFavoritesViewModel.favorites.observe(this, androidx.lifecycle.Observer { favoritePodcastEntities ->
            mFavorites.clear()
            if (favoritePodcastEntities != null && favoritePodcastEntities.isNotEmpty()) {
                mFavorites.addAll(favoritePodcastEntities)
                errorMessage.visibility = View.GONE
            } else {
                errorMessage.setText(R.string.empty_favorites_podcasts_list)
                errorMessage.visibility = View.VISIBLE
            }
            mAdapter?.updateList(mFavorites)
        })
    }

    private fun prepareRecycler() {
        mAdapter = FavoritePodcastAdapter(this)
        val layoutManager = GridLayoutManager(
            context,
            resources.getInteger(R.integer.recycler_column_count),
            RecyclerView.VERTICAL,
            false
        )
        recyclerFavorites.layoutManager = layoutManager
        recyclerFavorites.setHasFixedSize(true)
        recyclerFavorites.adapter = mAdapter
    }

    override fun onPodcastItemClick(podcastId: String) {
        mNavController?.let { navController ->
            val args = Bundle()
            args.putString(ARG_PODCAST_ID, podcastId)
            navController.navigate(R.id.actionFromFavoritesToDetails, args)
        }
    }

    override fun onPodcastMoreOptionsClick(podcastId: String, link: String, v: View) {
        showPopupMenu(v, link, podcastId)
    }

    private fun showPopupMenu(v: View, shareLink: String, podcastId: String) {
        context?.let { context ->

            val popupMenu = PopupMenu(context, v)
            popupMenu.inflate(R.menu.podcast_popup_menu)
            val menuItem = popupMenu.menu.findItem(R.id.action_favorite)
            menuItem.setTitle(R.string.remove_from_favorite)
            popupMenu
                .setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.action_favorite -> {
                            mFavoritesViewModel.removeFromFavorite(podcastId)
                            true
                        }
                        R.id.action_share -> {
                            ShareUtil.shareData(activity, shareLink)
                            true
                        }
                        else -> false
                    }
                }
            popupMenu.show()
        }
    }

    companion object {
        internal fun newInstance(): FavoritePodcastsFragment {
            val arg = Bundle()
            val fragment = FavoritePodcastsFragment()
            fragment.arguments = arg
            return fragment
        }
    }
}
