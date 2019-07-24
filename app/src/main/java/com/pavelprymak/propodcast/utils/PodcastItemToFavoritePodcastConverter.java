package com.pavelprymak.propodcast.utils;

import com.pavelprymak.propodcast.model.db.FavoritePodcastEntity;
import com.pavelprymak.propodcast.model.network.pojo.podcastById.PodcastResponse;
import com.pavelprymak.propodcast.model.network.pojo.podcasts.PodcastItem;

public class PodcastItemToFavoritePodcastConverter {
    public static FavoritePodcastEntity createFavorite(PodcastItem podcastItem) {
        FavoritePodcastEntity favoritePodcastEntity = new FavoritePodcastEntity();
        favoritePodcastEntity.setPodcastId(podcastItem.getId());
        favoritePodcastEntity.setImage(podcastItem.getImage());
        favoritePodcastEntity.setCountry(podcastItem.getCountry());
        favoritePodcastEntity.setDescription(podcastItem.getDescription());
        favoritePodcastEntity.setPublisher(podcastItem.getPublisher());
        favoritePodcastEntity.setClaimed(podcastItem.isIsClaimed());
        favoritePodcastEntity.setEarliestPubDateMs(podcastItem.getEarliestPubDateMs());
        favoritePodcastEntity.setLanguage(podcastItem.getLanguage());
        favoritePodcastEntity.setThumbnail(podcastItem.getThumbnail());
        favoritePodcastEntity.setEmail(podcastItem.getEmail());
        favoritePodcastEntity.setGenreIds(podcastItem.getGenreIds());
        favoritePodcastEntity.setItunesId(podcastItem.getItunesId());
        favoritePodcastEntity.setListennotesUrl(podcastItem.getListennotesUrl());
        favoritePodcastEntity.setRss(podcastItem.getRss());
        favoritePodcastEntity.setTitle(podcastItem.getTitle());
        favoritePodcastEntity.setTotalEpisodes(podcastItem.getTotalEpisodes());
        favoritePodcastEntity.setWebsite(podcastItem.getWebsite());
        return favoritePodcastEntity;
    }

    public static FavoritePodcastEntity createFavorite(PodcastResponse podcastItem) {
        FavoritePodcastEntity favoritePodcastEntity = new FavoritePodcastEntity();
        favoritePodcastEntity.setPodcastId(podcastItem.getId());
        favoritePodcastEntity.setImage(podcastItem.getImage());
        favoritePodcastEntity.setCountry(podcastItem.getCountry());
        favoritePodcastEntity.setDescription(podcastItem.getDescription());
        favoritePodcastEntity.setPublisher(podcastItem.getPublisher());
        favoritePodcastEntity.setClaimed(podcastItem.isIsClaimed());
        favoritePodcastEntity.setEarliestPubDateMs(podcastItem.getEarliestPubDateMs());
        favoritePodcastEntity.setLanguage(podcastItem.getLanguage());
        favoritePodcastEntity.setThumbnail(podcastItem.getThumbnail());
        favoritePodcastEntity.setEmail(podcastItem.getEmail());
        favoritePodcastEntity.setGenreIds(podcastItem.getGenreIds());
        favoritePodcastEntity.setItunesId(podcastItem.getItunesId());
        favoritePodcastEntity.setListennotesUrl(podcastItem.getListennotesUrl());
        favoritePodcastEntity.setRss(podcastItem.getRss());
        favoritePodcastEntity.setTitle(podcastItem.getTitle());
        favoritePodcastEntity.setTotalEpisodes(podcastItem.getTotalEpisodes());
        favoritePodcastEntity.setWebsite(podcastItem.getWebsite());
        return favoritePodcastEntity;
    }
}
