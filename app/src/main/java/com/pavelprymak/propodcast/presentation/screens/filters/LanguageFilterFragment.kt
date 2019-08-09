package com.pavelprymak.propodcast.presentation.screens.filters


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pavelprymak.propodcast.App
import com.pavelprymak.propodcast.MainActivity
import com.pavelprymak.propodcast.R
import com.pavelprymak.propodcast.presentation.adapters.LanguageAdapter
import com.pavelprymak.propodcast.presentation.adapters.LanguageClickListener
import com.pavelprymak.propodcast.presentation.viewModels.LanguageViewModel
import com.pavelprymak.propodcast.utils.SettingsPreferenceManager
import com.pavelprymak.propodcast.utils.SettingsPreferenceManager.ALL_LANGUAGES
import com.pavelprymak.propodcast.utils.otto.filters.EventUpdateLanguageFilter
import kotlinx.android.synthetic.main.fragment_language_filter.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


class LanguageFilterFragment : Fragment(), LanguageClickListener {
    private var mAdapter: LanguageAdapter? = null
    private val mLanguageViewModel: LanguageViewModel by viewModel()
    private val mSettingsPref: SettingsPreferenceManager by inject()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (activity is MainActivity) {
            if (!resources.getBoolean(R.bool.isTablet)) {
                (activity as MainActivity).setNavViewVisibility(false)
            }
        }
        return inflater.inflate(R.layout.fragment_language_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecycler()
        mLanguageViewModel.languages.observe(this, Observer { languages ->
            if (languages != null) {
                val selectedLanguage = mSettingsPref.filterLanguage
                mAdapter?.updateList(languages, selectedLanguage)
                if (selectedLanguage != ALL_LANGUAGES) {
                    val selectedItemPosition = mAdapter?.getPositionByLanguageName(selectedLanguage)
                    recyclerLanguages.smoothScrollToPosition(selectedItemPosition ?: 0)
                }
            }
        })
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        if (resources.getBoolean(R.bool.isTablet)) {
            toolbar.navigationIcon = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mLanguageViewModel.languages.removeObservers(this)
    }

    private fun prepareRecycler() {
        mAdapter = LanguageAdapter(this)
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerLanguages.layoutManager = layoutManager
        recyclerLanguages.setHasFixedSize(true)
        recyclerLanguages.adapter = mAdapter
    }

    override fun onLanguageItemClick(language: String) {
        mSettingsPref.saveFilterLanguage(language)
        if (resources.getBoolean(R.bool.isTablet)) {
            App.eventBus.post(EventUpdateLanguageFilter())
        }
    }
}
