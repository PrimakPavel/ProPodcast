package com.pavelprymak.propodcast.presentation.screens


import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pavelprymak.propodcast.MainActivity
import com.pavelprymak.propodcast.R
import com.pavelprymak.propodcast.model.db.FavoritePodcastEntity
import com.pavelprymak.propodcast.model.network.pojo.search.ResultsItem
import com.pavelprymak.propodcast.presentation.adapters.SearchPodcastAdapter
import com.pavelprymak.propodcast.presentation.adapters.SearchPodcastClickListener
import com.pavelprymak.propodcast.presentation.viewModels.FavoritePodcastsViewModel
import com.pavelprymak.propodcast.presentation.viewModels.SearchViewModel
import com.pavelprymak.propodcast.utils.ApiErrorHandler
import com.pavelprymak.propodcast.utils.KeyboardUtil
import com.pavelprymak.propodcast.utils.KeyboardUtil.showInputMethod
import com.pavelprymak.propodcast.utils.PodcastItemToFavoriteConverter.createFavorite
import com.pavelprymak.propodcast.utils.SettingsPreferenceManager
import com.pavelprymak.propodcast.utils.ShareUtil
import com.pavelprymak.propodcast.utils.firebase.FirebaseAnalyticsHelper.sentAnalyticSearchQueryData
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import retrofit2.HttpException
import java.util.*


private const val SCROLL_DELAY = 300L
class SearchFragment : Fragment(), SearchPodcastClickListener {
    private val mSearchViewModel: SearchViewModel by viewModel()
    private val mFavoritePodcastsViewModel: FavoritePodcastsViewModel by sharedViewModel()
    private val mSettingsPref: SettingsPreferenceManager by inject()

    private lateinit var mNavController: NavController
    private var mAdapter: SearchPodcastAdapter? = null
    private val mFavorites = ArrayList<FavoritePodcastEntity>()
    private val mDelayHandler = Handler()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (activity is MainActivity) {
            (activity as MainActivity).setNavViewVisibility(true)
        }
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mNavController = Navigation.findNavController(view)
        prepareRecycler()
        mFavoritePodcastsViewModel.favorites.observe(this, Observer { favoritePodcastEntities ->
            mFavorites.clear()
            if (favoritePodcastEntities != null) {
                mFavorites.addAll(favoritePodcastEntities)
            }
        })
        mSearchViewModel.searchResultsObserver.observe(this, Observer { resultsItems ->
            retryBtn.visibility = View.GONE
            mAdapter = SearchPodcastAdapter(this)
            searchRecycler.adapter = mAdapter
            mAdapter?.submitList(resultsItems)
        })
        mSearchViewModel.loadData.observe(this, Observer<Boolean> { this.showProgressBar(it) })
        mSearchViewModel.errorData.observe(this, Observer { throwable ->
            throwable?.let {
                retryBtn.visibility = View.VISIBLE
                if (throwable is HttpException && context != null) {
                    ApiErrorHandler.handleError(context!!, throwable)
                }
            }
        })
        mSearchViewModel.isEmptyListData.observe(this, Observer<Boolean> { this.showEmptyList(it) })

        retryBtn.setOnClickListener { v ->
            val size = mSearchViewModel.retryAfterErrorAndPrevLoadingListSize()
            mDelayHandler.postDelayed({ searchRecycler.scrollToPosition(size - 1) }, SCROLL_DELAY)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val filterLanguage = mSettingsPref.filterLanguage
                mSearchViewModel.prepareSearchRequest(query, filterLanguage)
                KeyboardUtil.hideKeyboard(activity)
                sentAnalyticSearchQueryData(query, filterLanguage)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        if (resources.getBoolean(R.bool.isTablet)) {
            fabFilter.hide()
        } else {
            fabFilter.setOnClickListener { v ->
                KeyboardUtil.hideKeyboard(activity)
                mNavController.navigate(R.id.actionFromSearchToLanguageFilter)
            }
        }
        searchViewShowKeyboard()
    }


    private fun searchViewShowKeyboard() {
        val searchText = searchView.findViewById<SearchView.SearchAutoComplete>(R.id.search_src_text)
        searchText.requestFocus()
        showInputMethod(context, searchText)
    }

    private fun showEmptyList(isEmpty: Boolean) {
        if (isEmpty) tvEmptySearch.visibility = View.VISIBLE else tvEmptySearch.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mFavoritePodcastsViewModel.favorites.removeObservers(this)
        mSearchViewModel.removeObservers(this)
    }

    private fun prepareRecycler() {
        mAdapter = SearchPodcastAdapter(this)
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        searchRecycler.layoutManager = layoutManager
        searchRecycler.adapter = mAdapter
    }

    private fun showProgressBar(isShow: Boolean?) {
        if (isShow == true) progressBar.visibility = View.VISIBLE else progressBar.visibility = View.GONE
    }

    override fun onPodcastItemClick(podcastId: String) {
        val args = Bundle()
        args.putString(ARG_PODCAST_ID, podcastId)
        mNavController.navigate(R.id.actionFromSearchToDetails, args)
    }

    override fun onPodcastMoreOptionsClick(podcastItem: ResultsItem, v: View) {
        showPopupMenu(v, podcastItem)
    }

    private fun showPopupMenu(v: View, podcastItem: ResultsItem) {
        context?.let { context ->
            podcastItem.id?.let { podcastId ->
                val popupMenu = PopupMenu(context, v)
                popupMenu.inflate(R.menu.podcast_popup_menu)
                val menuItem = popupMenu.menu.findItem(R.id.action_favorite)
                val isFavorite = mFavoritePodcastsViewModel.isFavorite(mFavorites, podcastId)
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
}
