package com.pavelprymak.propodcast.presentation.screens.filters


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.pavelprymak.propodcast.App
import com.pavelprymak.propodcast.R
import com.pavelprymak.propodcast.presentation.adapters.GenreAdapter
import com.pavelprymak.propodcast.presentation.adapters.GenreClickListener
import com.pavelprymak.propodcast.presentation.viewModels.GenreViewModel
import com.pavelprymak.propodcast.utils.SettingsPreferenceManager
import com.pavelprymak.propodcast.utils.SettingsPreferenceManager.ALL_GENRE
import com.pavelprymak.propodcast.utils.otto.filters.EventUpdateGenreFilter
import kotlinx.android.synthetic.main.fragment_genre_filter.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel


class GenreFilterFragment : Fragment(), GenreClickListener {
    private var mAdapter: GenreAdapter? = null
    private val mGenreViewModel: GenreViewModel by sharedViewModel()
    private val mSettingsPref: SettingsPreferenceManager by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_genre_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecycler()
        val fragment = this
        with(mGenreViewModel.getGenresBatch()) {
            data.observe(fragment, Observer { genresItems ->
                genresItems?.let {
                    val selectedGenreId = mSettingsPref.filterGenre
                    mAdapter?.updateList(genresItems, selectedGenreId)
                    if (selectedGenreId != ALL_GENRE) {
                        val selectedItemPosition = mAdapter?.getPositionByGenreId(selectedGenreId)
                        recyclerGenres.smoothScrollToPosition(selectedItemPosition ?: 0)
                    }
                }
            })
            loading.observe(fragment, Observer<Boolean> { showLoading(it) })
            error.observe(fragment, Observer { error -> showError(error, view) })
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) progressBar.visibility = View.VISIBLE else progressBar.visibility = View.GONE
    }

    private fun showError(throwable: Throwable?, view: View) {
        throwable?.let {
            val snackbar = Snackbar.make(view, R.string.error_connection, Snackbar.LENGTH_LONG)
            snackbar.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mGenreViewModel.removeObservers(this)
    }

    private fun prepareRecycler() {
        mAdapter = GenreAdapter(this)
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerGenres.layoutManager = layoutManager
        recyclerGenres.setHasFixedSize(true)
        recyclerGenres.adapter = mAdapter
    }

    override fun onGenreClick(id: Int) {
        mSettingsPref.saveFilterGenre(id)
        if (resources.getBoolean(R.bool.isTablet)) {
            App.eventBus.post(EventUpdateGenreFilter())
        }
    }

    companion object {
        internal fun newInstance(): GenreFilterFragment {
            val arg = Bundle()
            val fragment = GenreFilterFragment()
            fragment.arguments = arg
            return fragment
        }
    }
}
