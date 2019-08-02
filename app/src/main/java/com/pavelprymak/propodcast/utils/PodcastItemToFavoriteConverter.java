package com.pavelprymak.propodcast.utils;

import com.pavelprymak.propodcast.model.db.FavoriteEpisodeEntity;
import com.pavelprymak.propodcast.model.db.FavoritePodcastEntity;
import com.pavelprymak.propodcast.model.network.pojo.podcastById.EpisodesItem;
import com.pavelprymak.propodcast.model.network.pojo.podcastById.PodcastResponse;
import com.pavelprymak.propodcast.model.network.pojo.podcasts.PodcastItem;
import com.pavelprymak.propodcast.model.network.pojo.search.ResultsItem;

public class PodcastItemToFavoriteConverter {
    public static FavoritePodcastEntity createFavorite(PodcastItem podcastItem) {
        FavoritePodcastEntity favoritePodcastEntity = new FavoritePodcastEntity();
        favoritePodcastEntity.setId(podcastItem.getId());
        favoritePodcastEntity.setImage(podcastItem.getImage());
        favoritePodcastEntity.setCountry(podcastItem.getCountry());
        if (podcastItem.getDescription() != null)
            favoritePodcastEntity.setDescription(podcastItem.getDescription().trim());
        if (podcastItem.getPublisher() != null)
            favoritePodcastEntity.setPublisher(podcastItem.getPublisher().trim());
        favoritePodcastEntity.setClaimed(podcastItem.isIsClaimed());
        favoritePodcastEntity.setEarliestPubDateMs(podcastItem.getEarliestPubDateMs());
        favoritePodcastEntity.setLanguage(podcastItem.getLanguage());
        favoritePodcastEntity.setThumbnail(podcastItem.getThumbnail());
        favoritePodcastEntity.setEmail(podcastItem.getEmail());
        favoritePodcastEntity.setGenreIds(podcastItem.getGenreIds());
        favoritePodcastEntity.setItunesId(podcastItem.getItunesId());
        favoritePodcastEntity.setListennotesUrl(podcastItem.getListennotesUrl());
        favoritePodcastEntity.setRss(podcastItem.getRss());
        if (podcastItem.getTitle() != null)
            favoritePodcastEntity.setTitle(podcastItem.getTitle().trim());
        favoritePodcastEntity.setTotalEpisodes(podcastItem.getTotalEpisodes());
        favoritePodcastEntity.setWebsite(podcastItem.getWebsite());
        return favoritePodcastEntity;
    }

    public static FavoritePodcastEntity createFavorite(PodcastResponse podcastItem) {
        FavoritePodcastEntity favoritePodcastEntity = new FavoritePodcastEntity();
        favoritePodcastEntity.setId(podcastItem.getId());
        favoritePodcastEntity.setImage(podcastItem.getImage());
        favoritePodcastEntity.setCountry(podcastItem.getCountry());
        if (podcastItem.getDescription() != null)
            favoritePodcastEntity.setDescription(podcastItem.getDescription().trim());
        if (podcastItem.getPublisher() != null)
            favoritePodcastEntity.setPublisher(podcastItem.getPublisher().trim());
        favoritePodcastEntity.setClaimed(podcastItem.isIsClaimed());
        favoritePodcastEntity.setEarliestPubDateMs(podcastItem.getEarliestPubDateMs());
        favoritePodcastEntity.setLanguage(podcastItem.getLanguage());
        favoritePodcastEntity.setThumbnail(podcastItem.getThumbnail());
        favoritePodcastEntity.setEmail(podcastItem.getEmail());
        favoritePodcastEntity.setGenreIds(podcastItem.getGenreIds());
        favoritePodcastEntity.setItunesId(podcastItem.getItunesId());
        favoritePodcastEntity.setListennotesUrl(podcastItem.getListennotesUrl());
        favoritePodcastEntity.setRss(podcastItem.getRss());
        if (podcastItem.getTitle() != null)
            favoritePodcastEntity.setTitle(podcastItem.getTitle().trim());
        favoritePodcastEntity.setTotalEpisodes(podcastItem.getTotalEpisodes());
        favoritePodcastEntity.setWebsite(podcastItem.getWebsite());
        return favoritePodcastEntity;
    }

    public static FavoritePodcastEntity createFavorite(ResultsItem podcastItem) {
        FavoritePodcastEntity favoritePodcastEntity = new FavoritePodcastEntity();
        favoritePodcastEntity.setId(podcastItem.getId());
        favoritePodcastEntity.setImage(podcastItem.getImage());
        if (podcastItem.getDescriptionOriginal() != null)
            favoritePodcastEntity.setDescription(podcastItem.getDescriptionOriginal().trim());
        if (podcastItem.getPublisherOriginal() != null)
            favoritePodcastEntity.setPublisher(podcastItem.getPublisherOriginal().trim());
        favoritePodcastEntity.setEarliestPubDateMs(podcastItem.getEarliestPubDateMs());
        favoritePodcastEntity.setThumbnail(podcastItem.getThumbnail());
        favoritePodcastEntity.setEmail(podcastItem.getEmail());
        favoritePodcastEntity.setGenreIds(podcastItem.getGenreIds());
        favoritePodcastEntity.setItunesId(podcastItem.getItunesId());
        favoritePodcastEntity.setListennotesUrl(podcastItem.getListennotesUrl());
        favoritePodcastEntity.setRss(podcastItem.getRss());
        if (podcastItem.getTitleOriginal() != null)
            favoritePodcastEntity.setTitle(podcastItem.getTitleOriginal().trim());
        favoritePodcastEntity.setTotalEpisodes(podcastItem.getTotalEpisodes());
        return favoritePodcastEntity;
    }

    public static FavoriteEpisodeEntity createFavorite(EpisodesItem episodesItem) {
        FavoriteEpisodeEntity favoriteEpisodeEntity = new FavoriteEpisodeEntity();
        favoriteEpisodeEntity.setId(episodesItem.getId());
        favoriteEpisodeEntity.setImage(episodesItem.getImage());
        if (episodesItem.getDescription() != null)
            favoriteEpisodeEntity.setDescription(episodesItem.getDescription().trim());
        favoriteEpisodeEntity.setThumbnail(episodesItem.getThumbnail());
        favoriteEpisodeEntity.setListennotesUrl(episodesItem.getListennotesUrl());
        if (episodesItem.getTitle() != null)
            favoriteEpisodeEntity.setTitle(episodesItem.getTitle().trim());
        favoriteEpisodeEntity.setAudio(episodesItem.getAudio());
        favoriteEpisodeEntity.setPubDateMs(episodesItem.getPubDateMs());
        favoriteEpisodeEntity.setAudioLengthSec(episodesItem.getAudioLengthSec());
        return favoriteEpisodeEntity;
    }
}
