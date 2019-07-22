package com.pavelprymak.propodcast.model.network.pojo.podcastById;

import com.squareup.moshi.Json;

import java.util.List;


public class PodcastResponse {

    @Json(name = "image")
    private String image;

    @Json(name = "country")
    private String country;

    @Json(name = "thumbnail")
    private String thumbnail;

    @Json(name = "website")
    private String website;

    @Json(name = "explicit_content")
    private boolean explicitContent;

    @Json(name = "itunes_id")
    private int itunesId;

    @Json(name = "description")
    private String description;

    @Json(name = "earliest_pub_date_ms")
    private long earliestPubDateMs;

    @Json(name = "language")
    private String language;

    @Json(name = "title")
    private String title;

    @Json(name = "genre_ids")
    private List<Integer> genreIds;

    @Json(name = "listennotes_url")
    private String listennotesUrl;

    @Json(name = "total_episodes")
    private Integer totalEpisodes;

    @Json(name = "is_claimed")
    private boolean isClaimed;

    @Json(name = "next_episode_pub_date")
    private long nextEpisodePubDate;

    @Json(name = "rss")
    private String rss;

    @Json(name = "looking_for")
    private LookingFor lookingFor;

    @Json(name = "extra")
    private Extra extra;

    @Json(name = "publisher")
    private String publisher;

    @Json(name = "latest_pub_date_ms")
    private long latestPubDateMs;

    @Json(name = "id")
    private String id;

    @Json(name = "email")
    private String email;

    @Json(name = "episodes")
    private List<EpisodesItem> episodes;

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getWebsite() {
        return website;
    }

    public void setExplicitContent(boolean explicitContent) {
        this.explicitContent = explicitContent;
    }

    public boolean isExplicitContent() {
        return explicitContent;
    }

    public void setItunesId(int itunesId) {
        this.itunesId = itunesId;
    }

    public int getItunesId() {
        return itunesId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setEarliestPubDateMs(long earliestPubDateMs) {
        this.earliestPubDateMs = earliestPubDateMs;
    }

    public long getEarliestPubDateMs() {
        return earliestPubDateMs;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setListennotesUrl(String listennotesUrl) {
        this.listennotesUrl = listennotesUrl;
    }

    public String getListennotesUrl() {
        return listennotesUrl;
    }

    public void setTotalEpisodes(Integer totalEpisodes) {
        this.totalEpisodes = totalEpisodes;
    }

    public Integer getTotalEpisodes() {
        return totalEpisodes;
    }

    public void setIsClaimed(boolean isClaimed) {
        this.isClaimed = isClaimed;
    }

    public boolean isIsClaimed() {
        return isClaimed;
    }

    public void setNextEpisodePubDate(long nextEpisodePubDate) {
        this.nextEpisodePubDate = nextEpisodePubDate;
    }

    public long getNextEpisodePubDate() {
        return nextEpisodePubDate;
    }

    public void setRss(String rss) {
        this.rss = rss;
    }

    public String getRss() {
        return rss;
    }

    public void setLookingFor(LookingFor lookingFor) {
        this.lookingFor = lookingFor;
    }

    public LookingFor getLookingFor() {
        return lookingFor;
    }

    public void setExtra(Extra extra) {
        this.extra = extra;
    }

    public Extra getExtra() {
        return extra;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setLatestPubDateMs(long latestPubDateMs) {
        this.latestPubDateMs = latestPubDateMs;
    }

    public long getLatestPubDateMs() {
        return latestPubDateMs;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEpisodes(List<EpisodesItem> episodes) {
        this.episodes = episodes;
    }

    public List<EpisodesItem> getEpisodes() {
        return episodes;
    }

    @Override
    public String toString() {
        return
                "PodcastResponse{" +
                        "image = '" + image + '\'' +
                        ",country = '" + country + '\'' +
                        ",thumbnail = '" + thumbnail + '\'' +
                        ",website = '" + website + '\'' +
                        ",explicit_content = '" + explicitContent + '\'' +
                        ",itunes_id = '" + itunesId + '\'' +
                        ",description = '" + description + '\'' +
                        ",earliest_pub_date_ms = '" + earliestPubDateMs + '\'' +
                        ",language = '" + language + '\'' +
                        ",title = '" + title + '\'' +
                        ",genre_ids = '" + genreIds + '\'' +
                        ",listennotes_url = '" + listennotesUrl + '\'' +
                        ",total_episodes = '" + totalEpisodes + '\'' +
                        ",is_claimed = '" + isClaimed + '\'' +
                        ",next_episode_pub_date = '" + nextEpisodePubDate + '\'' +
                        ",rss = '" + rss + '\'' +
                        ",looking_for = '" + lookingFor + '\'' +
                        ",extra = '" + extra + '\'' +
                        ",publisher = '" + publisher + '\'' +
                        ",latest_pub_date_ms = '" + latestPubDateMs + '\'' +
                        ",id = '" + id + '\'' +
                        ",email = '" + email + '\'' +
                        ",episodes = '" + episodes + '\'' +
                        "}";
    }
}