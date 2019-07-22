package com.pavelprymak.propodcast.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "favorite_podcasts")
public class FavoritePodcastEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "country")
    private String country;

    @ColumnInfo(name = "thumbnail")
    private String thumbnail;

    @ColumnInfo(name = "website")
    private String website;

    @ColumnInfo(name = "itunes_id")
    private int itunesId;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "earliest_pub_date_ms")
    private long earliestPubDateMs;

    @ColumnInfo(name = "language")
    private String language;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "genre_ids")
    private List<Integer> genreIds;

    @ColumnInfo(name = "listennotes_url")
    private String listennotesUrl;

    @ColumnInfo(name = "total_episodes")
    private int totalEpisodes;

    @ColumnInfo(name = "is_claimed")
    private boolean isClaimed;

    @ColumnInfo(name = "rss")
    private String rss;

    @ColumnInfo(name = "publisher")
    private String publisher;

    @ColumnInfo(name = "latest_pub_date_ms")
    private long latestPubDateMs;

    @ColumnInfo(name = "podcast_id")
    private String podcastId;

    @ColumnInfo(name = "email")
    private String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public int getItunesId() {
        return itunesId;
    }

    public void setItunesId(int itunesId) {
        this.itunesId = itunesId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getEarliestPubDateMs() {
        return earliestPubDateMs;
    }

    public void setEarliestPubDateMs(long earliestPubDateMs) {
        this.earliestPubDateMs = earliestPubDateMs;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public String getListennotesUrl() {
        return listennotesUrl;
    }

    public void setListennotesUrl(String listennotesUrl) {
        this.listennotesUrl = listennotesUrl;
    }

    public int getTotalEpisodes() {
        return totalEpisodes;
    }

    public void setTotalEpisodes(int totalEpisodes) {
        this.totalEpisodes = totalEpisodes;
    }

    public boolean isClaimed() {
        return isClaimed;
    }

    public void setClaimed(boolean claimed) {
        isClaimed = claimed;
    }

    public String getRss() {
        return rss;
    }

    public void setRss(String rss) {
        this.rss = rss;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public long getLatestPubDateMs() {
        return latestPubDateMs;
    }

    public void setLatestPubDateMs(long latestPubDateMs) {
        this.latestPubDateMs = latestPubDateMs;
    }

    public String getPodcastId() {
        return podcastId;
    }

    public void setPodcastId(String podcastId) {
        this.podcastId = podcastId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
