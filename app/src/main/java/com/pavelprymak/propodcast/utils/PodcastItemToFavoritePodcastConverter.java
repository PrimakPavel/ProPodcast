package com.pavelprymak.propodcast.utils;

import com.pavelprymak.propodcast.model.db.FavoritePodcastEntity;
import com.pavelprymak.propodcast.model.network.pojo.podcastById.PodcastResponse;
import com.pavelprymak.propodcast.model.network.pojo.podcasts.PodcastItem;
import com.pavelprymak.propodcast.model.network.pojo.search.ResultsItem;

public class PodcastItemToFavoritePodcastConverter {
    public static FavoritePodcastEntity createFavorite(PodcastItem podcastItem) {
        FavoritePodcastEntity favoritePodcastEntity = new FavoritePodcastEntity();
        favoritePodcastEntity.setId(podcastItem.getId());
        favoritePodcastEntity.setImage(podcastItem.getImage());
        favoritePodcastEntity.setCountry(podcastItem.getCountry().trim());
        favoritePodcastEntity.setDescription(podcastItem.getDescription().trim());
        favoritePodcastEntity.setPublisher(podcastItem.getPublisher().trim());
        favoritePodcastEntity.setClaimed(podcastItem.isIsClaimed());
        favoritePodcastEntity.setEarliestPubDateMs(podcastItem.getEarliestPubDateMs());
        favoritePodcastEntity.setLanguage(podcastItem.getLanguage().trim());
        favoritePodcastEntity.setThumbnail(podcastItem.getThumbnail());
        favoritePodcastEntity.setEmail(podcastItem.getEmail());
        favoritePodcastEntity.setGenreIds(podcastItem.getGenreIds());
        favoritePodcastEntity.setItunesId(podcastItem.getItunesId());
        favoritePodcastEntity.setListennotesUrl(podcastItem.getListennotesUrl());
        favoritePodcastEntity.setRss(podcastItem.getRss());
        favoritePodcastEntity.setTitle(podcastItem.getTitle().trim());
        favoritePodcastEntity.setTotalEpisodes(podcastItem.getTotalEpisodes());
        favoritePodcastEntity.setWebsite(podcastItem.getWebsite());
        return favoritePodcastEntity;
    }

    public static FavoritePodcastEntity createFavorite(PodcastResponse podcastItem) {
        FavoritePodcastEntity favoritePodcastEntity = new FavoritePodcastEntity();
        favoritePodcastEntity.setId(podcastItem.getId());
        favoritePodcastEntity.setImage(podcastItem.getImage());
        favoritePodcastEntity.setCountry(podcastItem.getCountry().trim());
        favoritePodcastEntity.setDescription(podcastItem.getDescription().trim());
        favoritePodcastEntity.setPublisher(podcastItem.getPublisher().trim());
        favoritePodcastEntity.setClaimed(podcastItem.isIsClaimed());
        favoritePodcastEntity.setEarliestPubDateMs(podcastItem.getEarliestPubDateMs());
        favoritePodcastEntity.setLanguage(podcastItem.getLanguage().trim());
        favoritePodcastEntity.setThumbnail(podcastItem.getThumbnail());
        favoritePodcastEntity.setEmail(podcastItem.getEmail());
        favoritePodcastEntity.setGenreIds(podcastItem.getGenreIds());
        favoritePodcastEntity.setItunesId(podcastItem.getItunesId());
        favoritePodcastEntity.setListennotesUrl(podcastItem.getListennotesUrl());
        favoritePodcastEntity.setRss(podcastItem.getRss());
        favoritePodcastEntity.setTitle(podcastItem.getTitle().trim());
        favoritePodcastEntity.setTotalEpisodes(podcastItem.getTotalEpisodes());
        favoritePodcastEntity.setWebsite(podcastItem.getWebsite());
        return favoritePodcastEntity;
    }

    public static FavoritePodcastEntity createFavorite(ResultsItem podcastItem) {
        FavoritePodcastEntity favoritePodcastEntity = new FavoritePodcastEntity();
        favoritePodcastEntity.setId(podcastItem.getId());
        favoritePodcastEntity.setImage(podcastItem.getImage());
        favoritePodcastEntity.setDescription(podcastItem.getDescriptionOriginal().trim());
        favoritePodcastEntity.setPublisher(podcastItem.getPublisherOriginal().trim());
        favoritePodcastEntity.setEarliestPubDateMs(podcastItem.getEarliestPubDateMs());
        favoritePodcastEntity.setThumbnail(podcastItem.getThumbnail());
        favoritePodcastEntity.setEmail(podcastItem.getEmail());
        favoritePodcastEntity.setGenreIds(podcastItem.getGenreIds());
        favoritePodcastEntity.setItunesId(podcastItem.getItunesId());
        favoritePodcastEntity.setListennotesUrl(podcastItem.getListennotesUrl());
        favoritePodcastEntity.setRss(podcastItem.getRss());
        favoritePodcastEntity.setTitle(podcastItem.getTitleOriginal().trim());
        favoritePodcastEntity.setTotalEpisodes(podcastItem.getTotalEpisodes());
        return favoritePodcastEntity;
    }
}
