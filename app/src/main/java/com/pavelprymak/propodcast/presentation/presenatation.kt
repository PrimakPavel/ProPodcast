package com.pavelprymak.propodcast.presentation

import com.pavelprymak.propodcast.presentation.viewModels.*
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val presentation = module {
    viewModel { BestPodcastsViewModel(get(), get()) }
    viewModel { FavoriteEpisodesViewModel(get()) }
    viewModel { FavoritePodcastsViewModel(get()) }
    viewModel { GenreViewModel(get(), get()) }
    viewModel { LanguageViewModel() }
    viewModel { PodcastInfoViewModel(get()) }
    viewModel { RegionViewModel() }
    viewModel { SearchViewModel(get(), get()) }
}