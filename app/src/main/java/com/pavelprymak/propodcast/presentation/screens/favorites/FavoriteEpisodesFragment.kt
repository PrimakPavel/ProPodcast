package com.pavelprymak.propodcast.presentation.screens.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pavelprymak.propodcast.App
import com.pavelprymak.propodcast.R
import com.pavelprymak.propodcast.model.db.FavoriteEpisodeEntity
import com.pavelprymak.propodcast.presentation.adapters.FavoriteEpisodeAdapter
import com.pavelprymak.propodcast.presentation.adapters.FavoriteEpisodeClickListener
import com.pavelprymak.propodcast.presentation.viewModels.FavoriteEpisodesViewModel
import com.pavelprymak.propodcast.utils.DateFormatUtil
import com.pavelprymak.propodcast.utils.ShareUtil
import com.pavelprymak.propodcast.utils.firebase.FirebaseAnalyticsHelper.sentAnalyticEpisodeData
import com.pavelprymak.propodcast.utils.otto.player.EventStartTack
import com.pavelprymak.propodcast.utils.otto.player.EventUpdatePlayerVisibility
import kotlinx.android.synthetic.main.fragment_favorite_items.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.util.*

class FavoriteEpisodesFragment : Fragment(), FavoriteEpisodeClickListener {
    private val mFavoritesViewModel: FavoriteEpisodesViewModel by sharedViewModel()
    private val mFavorites = ArrayList<FavoriteEpisodeEntity>()
    private var mAdapter: FavoriteEpisodeAdapter? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorite_items, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecycler()
        mFavoritesViewModel.favorites.observe(this, Observer { favoriteEpisodesEntities ->
            mFavorites.clear()
            if (favoriteEpisodesEntities != null && favoriteEpisodesEntities.isNotEmpty()) {
                mFavorites.addAll(favoriteEpisodesEntities)
                errorMessage.visibility = View.GONE
            } else {
                errorMessage.setText(R.string.empty_favorites_episodes_list)
                errorMessage.visibility = View.VISIBLE
            }
            mAdapter?.updateList(mFavorites)
        })
    }

    override fun onEpisodeItemClick(episodesItem: FavoriteEpisodeEntity) {
        sentAnalyticEpisodeData(episodesItem)
        val publishDate = Date(episodesItem.pubDateMs)
        App.eventBus.post(
            EventStartTack(
                episodesItem.audio,
                episodesItem.title,
                episodesItem.thumbnail,
                DateFormatUtil.PUBLISH_DATE_FORMAT.format(publishDate)
            )
        )
        App.eventBus.post(EventUpdatePlayerVisibility(true))
    }

    override fun onEpisodeMoreOptionsClick(episodeId: String, link: String?, v: View) {
        showPopupMenu(v, link, episodeId)
    }

    private fun prepareRecycler() {
        mAdapter = FavoriteEpisodeAdapter(this)
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

    private fun showPopupMenu(v: View, shareLink: String?, episodeId: String) {
        context?.let {context->
            val popupMenu = PopupMenu(context, v)
            popupMenu.inflate(R.menu.podcast_popup_menu)
            val menuItem = popupMenu.menu.findItem(R.id.action_favorite)
            menuItem.setTitle(R.string.remove_from_favorite)
            popupMenu
                .setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.action_favorite -> {
                            mFavoritesViewModel.removeFromFavorite(episodeId)
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
        internal fun newInstance(): FavoriteEpisodesFragment {
            val fragment = FavoriteEpisodesFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
