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
import com.pavelprymak.propodcast.databinding.FragmentGenreFilterBinding;
import com.pavelprymak.propodcast.presentation.adapters.GenreAdapter;
import com.pavelprymak.propodcast.presentation.adapters.GenreClickListener;
import com.pavelprymak.propodcast.presentation.viewModels.GenreViewModel;

import static com.pavelprymak.propodcast.utils.SettingsPreferenceManager.ALL_GENRE;


public class GenreFilterFragment extends Fragment implements GenreClickListener {
    private FragmentGenreFilterBinding mBinding;
    private GenreAdapter mAdapter;
    private GenreViewModel mGenreViewModel;

    public GenreFilterFragment() {
    }

    static GenreFilterFragment newInstance() {
        Bundle arg = new Bundle();
        GenreFilterFragment fragment = new GenreFilterFragment();
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getActivity() != null) {
            mGenreViewModel = ViewModelProviders.of(getActivity()).get(GenreViewModel.class);
        }
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_genre_filter, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepareRecycler();
        mGenreViewModel.getGenres().observe(this, genresItems -> {
            if (genresItems != null) {
                int selectedGenreId = App.mSettings.getFilterGenre();
                mAdapter.updateList(genresItems, selectedGenreId);
                if (selectedGenreId != ALL_GENRE) {
                    int selectedItemPosition = mAdapter.getPositionByGenreId(selectedGenreId);
                    mBinding.recyclerGenres.smoothScrollToPosition(selectedItemPosition);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mGenreViewModel.getGenres().removeObservers(this);
    }

    private void prepareRecycler() {
        mAdapter = new GenreAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mBinding.recyclerGenres.setLayoutManager(layoutManager);
        mBinding.recyclerGenres.setHasFixedSize(true);
        mBinding.recyclerGenres.setAdapter(mAdapter);
    }

    @Override
    public void onGenreClick(int id) {
        App.mSettings.saveFilterGenre(id);
    }
}
