package com.pavelprymak.propodcast.presentation.screens.filters;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pavelprymak.propodcast.App;
import com.pavelprymak.propodcast.R;
import com.pavelprymak.propodcast.databinding.FragmentLanguageFilterBinding;
import com.pavelprymak.propodcast.presentation.adapters.LanguageAdapter;
import com.pavelprymak.propodcast.presentation.adapters.LanguageClickListener;
import com.pavelprymak.propodcast.presentation.viewModels.LanguageViewModel;

import static com.pavelprymak.propodcast.utils.SettingsPreferenceManager.ALL_LANGUAGES;


public class LanguageFilterFragment extends Fragment implements LanguageClickListener {
    private FragmentLanguageFilterBinding mBinding;
    private LanguageAdapter mAdapter;
    private LanguageViewModel mLanguageViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mLanguageViewModel = ViewModelProviders.of(this).get(LanguageViewModel.class);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_language_filter, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepareRecycler();
        mLanguageViewModel.getLanguages().observe(this, languages -> {
            if (languages != null) {
                String selectedLanguage = App.mSettings.getFilterLanguage();
                mAdapter.updateList(languages, selectedLanguage);
                if (!selectedLanguage.equals(ALL_LANGUAGES)) {
                    int selectedItemPosition = mAdapter.getPositionByLanguageName(selectedLanguage);
                    mBinding.recyclerLanguages.smoothScrollToPosition(selectedItemPosition);
                }
            }
        });
        mBinding.toolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null)
                getActivity().onBackPressed();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mLanguageViewModel.getLanguages().removeObservers(this);
    }

    private void prepareRecycler() {
        mAdapter = new LanguageAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mBinding.recyclerLanguages.setLayoutManager(layoutManager);
        mBinding.recyclerLanguages.setHasFixedSize(true);
        mBinding.recyclerLanguages.setAdapter(mAdapter);
    }

    @Override
    public void onLanguageItemClick(String language) {
        App.mSettings.saveFilterLanguage(language);
    }
}
