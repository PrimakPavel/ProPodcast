package com.pavelprymak.propodcast.presentation.screens

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pavelprymak.propodcast.App
import com.pavelprymak.propodcast.MainActivity
import com.pavelprymak.propodcast.R
import com.pavelprymak.propodcast.model.db.FavoritePodcastEntity
import com.pavelprymak.propodcast.model.network.pojo.podcasts.PodcastItem
import com.pavelprymak.propodcast.presentation.adapters.PodcastAdapter
import com.pavelprymak.propodcast.presentation.adapters.PodcastClickListener
import com.pavelprymak.propodcast.presentation.viewModels.BestPodcastsViewModel
import com.pavelprymak.propodcast.presentation.viewModels.FavoritePodcastsViewModel
import com.pavelprymak.propodcast.utils.ApiErrorHandler
import com.pavelprymak.propodcast.utils.PodcastItemToFavoriteConverter.createFavorite
import com.pavelprymak.propodcast.utils.SettingsPreferenceManager
import com.pavelprymak.propodcast.utils.ShareUtil
import com.pavelprymak.propodcast.utils.otto.filters.EventUpdateGenreFilter
import com.pavelprymak.propodcast.utils.otto.filters.EventUpdateRegionFilter
import com.squareup.otto.Subscribe
import kotlinx.android.synthetic.main.fragment_best_podcasts.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import retrofit2.HttpException
import java.util.*

private const val SCROLL_DELAY = 300L

class BestPodcastsFragment : Fragment(), PodcastClickListener {
    private val mBestPodcastsViewModel: BestPodcastsViewModel by sharedViewModel()
    private val mFavoritePodcastsViewModel: FavoritePodcastsViewModel by sharedViewModel()
    private val mFavorites = ArrayList<FavoritePodcastEntity>()
    private var mAdapter: PodcastAdapter? = null
    private lateinit var mNavController: NavController
    private val mDelayHandler = Handler()
    private val mSettings: SettingsPreferenceManager by inject()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_best_podcasts, container, false)
        if (activity is MainActivity) {
            (activity as MainActivity).setNavViewVisibility(true)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mNavController = Navigation.findNavController(view)
        prepareRecycler()
        val fragment = this
        mBestPodcastsViewModel.prepareBestPodcasts(mSettings.filterGenre, mSettings.filterRegion)
            .observe(this, Observer { podcastItems ->
                retryBtn.visibility = View.GONE
                mAdapter = PodcastAdapter(this)
                recyclerBestPodcasts.adapter = mAdapter
                mAdapter?.submitList(podcastItems)
            })
        with(mBestPodcastsViewModel.getPagingStateBatch()) {
            loading.observe(fragment, Observer<Boolean> { showProgressBar(it) })
            error.observe(fragment, Observer { throwable ->
                throwable?.let {
                    retryBtn.visibility = View.VISIBLE
                    if (throwable is HttpException && context != null) {
                        ApiErrorHandler.handleError(context!!, throwable)
                    }
                }
            })
            isEmptyListData.observe(fragment, Observer { isEmptyList ->
                if (isEmptyList) errorMessage.visibility = View.VISIBLE else errorMessage.visibility = View.GONE
            })
        }

        retryBtn.setOnClickListener {
            val size = mBestPodcastsViewModel.retryAfterErrorAndPrevLoadingListSize()
            mDelayHandler.postDelayed({ recyclerBestPodcasts.scrollToPosition(size - 1) }, SCROLL_DELAY)
        }
        mFavoritePodcastsViewModel.favorites.observe(this, Observer { favoritePodcastEntities ->
            mFavorites.clear()
            favoritePodcastEntities?.let {
                mFavorites.addAll(favoritePodcastEntities)
            }
        })
        if (!resources.getBoolean(R.bool.isTablet)) {
            toolbar.inflateMenu(R.menu.filters_menu)
            toolbar.setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.action_filters) {
                    mNavController.navigate(R.id.actionFromBestToFilters)
                    true
                } else false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBestPodcastsViewModel.removeObservers(this)
        mFavoritePodcastsViewModel.favorites.removeObservers(this)
    }

    override fun onPause() {
        super.onPause()
        App.eventBus.unregister(this)
    }

    override fun onResume() {
        super.onResume()
        App.eventBus.register(this)
    }

    @Subscribe
    fun onGenreFilterUpdate(eventUpdateGenreFilter: EventUpdateGenreFilter) {
        mBestPodcastsViewModel.prepareBestPodcasts(mSettings.filterGenre, mSettings.filterRegion)
    }

    @Subscribe
    fun onRegionFilterUpdate(eventUpdateRegionFilter: EventUpdateRegionFilter) {
        mBestPodcastsViewModel.prepareBestPodcasts(mSettings.filterGenre, mSettings.filterRegion)
    }

    private fun showProgressBar(isShow: Boolean?) {
        if (isShow == true) progressBar.visibility = View.VISIBLE else progressBar.visibility = View.GONE
    }

    private fun prepareRecycler() {
        mAdapter = PodcastAdapter(this)
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerBestPodcasts.layoutManager = layoutManager
        recyclerBestPodcasts.adapter = mAdapter
    }

    override fun onPodcastItemClick(podcastId: String) {
        val args = Bundle()
        args.putString(ARG_PODCAST_ID, podcastId)
        mNavController.navigate(R.id.actionFromBestToDetails, args)
    }

    override fun onPodcastMoreOptionsClick(podcastItem: PodcastItem, v: View) {
        showPopupMenu(v, podcastItem)
    }

    private fun showPopupMenu(v: View, podcastItem: PodcastItem) {
        context?.let { context ->
            podcastItem.id.let { podcastId ->
                val popupMenu = PopupMenu(context, v)
                popupMenu.inflate(R.menu.podcast_popup_menu)
                val menuItem = popupMenu.menu.findItem(R.id.action_favorite)
                val isFavorite = mFavoritePodcastsViewModel.isFavorite(mFavorites, podcastId)
                if (isFavorite) {
                    menuItem.setTitle(R.string.remove_from_favorite)
                } else {
                    menuItem.setTitle(R.string.add_to_favorite)
                }
                popupMenu
                    .setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.action_favorite -> {
                                if (isFavorite) {
                                    mFavoritePodcastsViewModel.removeFromFavorite(podcastId)
                                } else {
                                    mFavoritePodcastsViewModel.addToFavorite(createFavorite(podcastItem))
                                }
                                true
                            }
                            R.id.action_share -> {
                                ShareUtil.shareData(activity, podcastItem.listennotesUrl)
                                true
                            }
                            else -> false
                        }
                    }
                popupMenu.show()
            }
        }
    }
}
