package com.pavelprymak.propodcast.presentation.screens


import android.os.Bundle
import android.text.Html
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
import com.google.android.material.snackbar.Snackbar
import com.pavelprymak.propodcast.App
import com.pavelprymak.propodcast.MainActivity
import com.pavelprymak.propodcast.R
import com.pavelprymak.propodcast.model.db.FavoriteEpisodeEntity
import com.pavelprymak.propodcast.model.db.FavoritePodcastEntity
import com.pavelprymak.propodcast.model.network.pojo.podcastById.EpisodesItem
import com.pavelprymak.propodcast.model.network.pojo.podcastById.PodcastResponse
import com.pavelprymak.propodcast.model.network.pojo.podcasts.PodcastItem
import com.pavelprymak.propodcast.presentation.adapters.PodcastInfoAdapter
import com.pavelprymak.propodcast.presentation.adapters.PodcastInfoClickListener
import com.pavelprymak.propodcast.presentation.viewModels.FavoriteEpisodesViewModel
import com.pavelprymak.propodcast.presentation.viewModels.FavoritePodcastsViewModel
import com.pavelprymak.propodcast.presentation.viewModels.PodcastInfoViewModel
import com.pavelprymak.propodcast.utils.ApiErrorHandler
import com.pavelprymak.propodcast.utils.PodcastItemToFavoriteConverter.createFavorite
import com.pavelprymak.propodcast.utils.ShareUtil
import com.pavelprymak.propodcast.utils.firebase.FirebaseAnalyticsHelper.sentAnalyticEpisodeData
import com.pavelprymak.propodcast.utils.otto.player.EventStartTack
import com.pavelprymak.propodcast.utils.otto.player.EventUpdatePlayerVisibility
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_podcast_details.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import retrofit2.HttpException
import java.util.*


const val ARG_PODCAST_ID = "argPodcastId"

class PodcastDetailsFragment : Fragment(), PodcastInfoClickListener {
    private val mPodcastDataViewModel: PodcastInfoViewModel by viewModel()
    private val mFavoritePodcastsViewModel: FavoritePodcastsViewModel by sharedViewModel()
    private val mFavoriteEpisodesViewModel: FavoriteEpisodesViewModel by sharedViewModel()
    private lateinit var mNavController: NavController

    private var mPodcastId: String? = null

    private var mAdapter: PodcastInfoAdapter? = null

    private val mFavoritePodcasts = ArrayList<FavoritePodcastEntity>()
    private val mFavoriteEpisodes = ArrayList<FavoriteEpisodeEntity>()
    private val mEpisodes = ArrayList<EpisodesItem>()
    private var mPodcastResponse: PodcastResponse? = null
    private val mRecommendations = ArrayList<PodcastItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { arg ->
            mPodcastId = arg.getString(ARG_PODCAST_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (activity is MainActivity) {
            (activity as MainActivity).setNavViewVisibility(false)
        }
        mPodcastId?.let { id ->
            mPodcastDataViewModel.setItemId(id)
        }
        return inflater.inflate(R.layout.fragment_podcast_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mNavController = Navigation.findNavController(view)
        appBarLayout.setExpanded(false, false)
        prepareRecycler()
        mPodcastDataViewModel.preparePodcastInfoData()
        mPodcastDataViewModel.podcastDataBatch.data.observe(
            this,
            Observer<PodcastResponse> { podcastInfo -> podcastInfo?.let { showData(it) } })
        mPodcastDataViewModel.podcastDataBatch.loading.observe(this, Observer<Boolean> { isLoading ->
            if (isLoading != null) {
                progressBarVisibility(isLoading)
            }
        })
        mPodcastDataViewModel.podcastDataBatch.error.observe(this, Observer<Throwable> { throwable ->
            throwable?.let {
                val snackbar = Snackbar.make(view, R.string.error_connection, Snackbar.LENGTH_LONG)
                snackbar.show()
                if (throwable is HttpException && context != null) {
                    ApiErrorHandler.handleError(context!!, throwable)
                }
            }
        })

        mPodcastDataViewModel.getRecommendData().observe(this, Observer { recommendationsItems ->
            recommendationsItems?.let {
                mRecommendations.clear()
                mRecommendations.addAll(recommendationsItems)
                mAdapter?.updateLists(mEpisodes, mRecommendations)
            }
        })

        appBar.setNavigationOnClickListener { v ->
            activity?.onBackPressed()
        }

        mFavoritePodcastsViewModel.favorites.observe(this, Observer { favoritePodcastEntities ->
            mFavoritePodcasts.clear()
            if (favoritePodcastEntities != null) {
                mFavoritePodcasts.addAll(favoritePodcastEntities)
            }
        })

        mFavoriteEpisodesViewModel.favorites.observe(this, Observer { favoriteEpisodesEntities ->
            mFavoriteEpisodes.clear()
            if (favoriteEpisodesEntities != null) {
                mFavoriteEpisodes.addAll(favoriteEpisodesEntities)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mPodcastDataViewModel.removePodcastDataBatchObservers(this)
        mPodcastDataViewModel.getRecommendData().removeObservers(this)
        mFavoriteEpisodesViewModel.favorites.removeObservers(this)
        mFavoritePodcastsViewModel.favorites.removeObservers(this)
    }

    private fun prepareRecycler() {
        mAdapter = PodcastInfoAdapter(this)
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        podcastDataRecycler.layoutManager = layoutManager
        podcastDataRecycler.setHasFixedSize(true)
        podcastDataRecycler.adapter = mAdapter
    }

    override fun onEpisodeItemClick(episodesItem: EpisodesItem) {
        sentAnalyticEpisodeData(episodesItem)
        App.eventBus.post(
            EventStartTack(
                episodesItem.audio,
                episodesItem.title,
                episodesItem.thumbnail,
                mPodcastResponse?.publisher
            )
        )
        App.eventBus.post(EventUpdatePlayerVisibility(true))
    }

    override fun onMoreEpisodeClick() {
        mPodcastDataViewModel.loadMoreEpisodes()
    }

    override fun onEpisodeMoreOptionClick(episodesItem: EpisodesItem, view: View) {
        showEpisodePopupMenu(view, episodesItem)
    }

    override fun onRecommendationItemClick(podcastId: String) {
        val args = Bundle()
        args.putString(ARG_PODCAST_ID, podcastId)
        mNavController.navigate(R.id.podcastDetailsFragment, args)
    }

    override fun onPodcastMoreOptionsClick(podcastItem: PodcastItem, v: View) {
        showPodcastPopupMenu(v, podcastItem)
    }

    private fun progressBarVisibility(isVisible: Boolean) {
        if (isVisible) progressBar.visibility = View.VISIBLE else progressBar.visibility = View.GONE
    }

    private fun showData(podcastResponse: PodcastResponse) {
        mPodcastResponse = podcastResponse
        //Title
        if (!podcastResponse.title.isNullOrEmpty()) {
            tvTitleAppBar.text = podcastResponse.title
        }
        //Publisher
        if (!podcastResponse.publisher.isNullOrEmpty()) {
            tvPublisher.setText(R.string.publisher_label)
            tvPublisher.append(podcastResponse.publisher)
        }
        //Country
        if (!podcastResponse.country.isNullOrEmpty()) {
            tvCountry.setText(R.string.country_label)
            tvCountry.append(podcastResponse.country)
        }
        //Total episodes count
        tvTotalEpisodes.setText(R.string.episodes_count_label)
        tvTotalEpisodes.append(podcastResponse.totalEpisodes.toString())
        //Set photo
        if (!podcastResponse.image.isNullOrEmpty())
            Picasso.get()
                .load(podcastResponse.image)
                .into(photo)
        if (!podcastResponse.description.isNullOrEmpty()) {
            tvDescription.text = Html.fromHtml(podcastResponse.description)
        }
        //Add episodes
        podcastResponse.episodes?.let { episodes ->
            mEpisodes.clear()
            mEpisodes.addAll(episodes)
            mAdapter?.updateLists(mEpisodes, mRecommendations)
            mAdapter?.setMaxEpisodesCount(podcastResponse.totalEpisodes)
        }
        mPodcastId?.let { podcastId ->
            mFavoritePodcastsViewModel.getFavoriteById(podcastId).removeObservers(this)
            mFavoritePodcastsViewModel.getFavoriteById(podcastId).observe(this, Observer { podcastEntity ->
                if (podcastEntity == null) {
                    fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_border)
                    fabFavorite.setOnClickListener { v ->
                        mFavoritePodcastsViewModel.addToFavorite(
                            createFavorite(podcastResponse)
                        )
                    }
                } else {
                    fabFavorite.setImageResource(R.drawable.ic_baseline_favorite)
                    fabFavorite.setOnClickListener { v ->
                        mFavoritePodcastsViewModel.removeFromFavorite(
                            podcastEntity.id
                        )
                    }
                }
            })
        }

        appBarLayout.setExpanded(false, false)
    }

    private fun showPodcastPopupMenu(v: View, podcastItem: PodcastItem) {
        context?.let { context ->
            podcastItem.id?.let { podcastId ->
                val popupMenu = PopupMenu(context, v)
                popupMenu.inflate(R.menu.podcast_popup_menu)
                val menuItem = popupMenu.menu.findItem(R.id.action_favorite)
                val isFavorite = mFavoritePodcastsViewModel.isFavorite(mFavoritePodcasts, podcastId)
                if (isFavorite) menuItem.setTitle(R.string.remove_from_favorite) else menuItem.setTitle(R.string.add_to_favorite)
                popupMenu
                    .setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.action_favorite -> {
                                if (isFavorite) mFavoritePodcastsViewModel.removeFromFavorite(podcastId) else mFavoritePodcastsViewModel.addToFavorite(
                                    createFavorite(podcastItem)
                                )
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

    private fun showEpisodePopupMenu(v: View, episodesItem: EpisodesItem) {
        context?.let { context ->
            episodesItem.id?.let { episodeId ->
                val popupMenu = PopupMenu(context, v)
                popupMenu.inflate(R.menu.podcast_popup_menu)
                val menuItem = popupMenu.menu.findItem(R.id.action_favorite)
                val isFavorite = mFavoriteEpisodesViewModel.isFavorite(mFavoriteEpisodes, episodeId)
                if (isFavorite) menuItem.setTitle(R.string.remove_from_favorite) else menuItem.setTitle(R.string.add_to_favorite)
                popupMenu
                    .setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.action_favorite -> {
                                if (isFavorite) mFavoriteEpisodesViewModel.removeFromFavorite(episodeId) else mFavoriteEpisodesViewModel.addToFavorite(
                                    createFavorite(episodesItem)
                                )
                                true
                            }
                            R.id.action_share -> {
                                ShareUtil.shareData(activity, episodesItem.listennotesUrl)
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
