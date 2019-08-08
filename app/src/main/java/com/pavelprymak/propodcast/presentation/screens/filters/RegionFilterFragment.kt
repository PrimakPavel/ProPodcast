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
import com.pavelprymak.propodcast.R
import com.pavelprymak.propodcast.presentation.adapters.RegionAdapter
import com.pavelprymak.propodcast.presentation.adapters.RegionClickListener
import com.pavelprymak.propodcast.presentation.viewModels.RegionViewModel
import com.pavelprymak.propodcast.utils.SettingsPreferenceManager
import com.pavelprymak.propodcast.utils.SettingsPreferenceManager.ALL_REGIONS
import com.pavelprymak.propodcast.utils.otto.filters.EventUpdateRegionFilter
import kotlinx.android.synthetic.main.fragment_region_filter.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


class RegionFilterFragment : Fragment(), RegionClickListener {
    private var mAdapter: RegionAdapter? = null
    private val mRegionViewModel: RegionViewModel by viewModel()
    private val mSettingsPref: SettingsPreferenceManager by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_region_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecycler()
        mRegionViewModel.regions.observe(this, Observer { regionItems ->
            if (regionItems != null) {
                val selectedRegionShortName = mSettingsPref.filterRegion
                mAdapter?.updateList(regionItems, selectedRegionShortName)
                if (selectedRegionShortName != ALL_REGIONS) {
                    val selectedItemPosition = mAdapter?.getPositionByRegionShortName(selectedRegionShortName)
                    recyclerRegions.smoothScrollToPosition(selectedItemPosition ?: 0)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mRegionViewModel.regions.removeObservers(this)
    }

    private fun prepareRecycler() {
        mAdapter = RegionAdapter(this)
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerRegions.layoutManager = layoutManager
        recyclerRegions.setHasFixedSize(true)
        recyclerRegions.adapter = mAdapter
    }

    override fun onRegionItemClick(regionName: String) {
        mSettingsPref.saveFilterRegion(regionName)
        if (resources.getBoolean(R.bool.isTablet)) {
            App.eventBus.post(EventUpdateRegionFilter())
        }
    }

    companion object {
        internal fun newInstance(): RegionFilterFragment {
            val arg = Bundle()
            val fragment = RegionFilterFragment()
            fragment.arguments = arg
            return fragment
        }
    }
}
