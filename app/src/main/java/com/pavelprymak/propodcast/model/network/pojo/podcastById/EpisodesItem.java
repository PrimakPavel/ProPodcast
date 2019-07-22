package com.pavelprymak.propodcast.model.network.pojo.podcastById;


import com.squareup.moshi.Json;


public class EpisodesItem {

    @Json(name = "image")
    private String image;

    @Json(name = "thumbnail")
    private String thumbnail;

    @Json(name = "explicit_content")
    private boolean explicitContent;

    @Json(name = "listennotes_edit_url")
    private String listennotesEditUrl;

    @Json(name = "audio_length_sec")
    private Integer audioLengthSec;

    @Json(name = "description")
    private String description;

    @Json(name = "id")
    private String id;

    @Json(name = "audio")
    private String audio;

    @Json(name = "title")
    private String title;

    @Json(name = "pub_date_ms")
    private long pubDateMs;

    @Json(name = "listennotes_url")
    private String listennotesUrl;

    @Json(name = "maybe_audio_invalid")
    private boolean maybeAudioInvalid;

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setExplicitContent(boolean explicitContent) {
        this.explicitContent = explicitContent;
    }

    public boolean isExplicitContent() {
        return explicitContent;
    }

    public void setListennotesEditUrl(String listennotesEditUrl) {
        this.listennotesEditUrl = listennotesEditUrl;
    }

    public String getListennotesEditUrl() {
        return listennotesEditUrl;
    }

    public void setAudioLengthSec(Integer audioLengthSec) {
        this.audioLengthSec = audioLengthSec;
    }

    public Integer getAudioLengthSec() {
        return audioLengthSec;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getAudio() {
        return audio;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setPubDateMs(long pubDateMs) {
        this.pubDateMs = pubDateMs;
    }

    public long getPubDateMs() {
        return pubDateMs;
    }

    public void setListennotesUrl(String listennotesUrl) {
        this.listennotesUrl = listennotesUrl;
    }

    public String getListennotesUrl() {
        return listennotesUrl;
    }

    public void setMaybeAudioInvalid(boolean maybeAudioInvalid) {
        this.maybeAudioInvalid = maybeAudioInvalid;
    }

    public boolean isMaybeAudioInvalid() {
        return maybeAudioInvalid;
    }

    @Override
    public String toString() {
        return
                "EpisodesItem{" +
                        "image = '" + image + '\'' +
                        ",thumbnail = '" + thumbnail + '\'' +
                        ",explicit_content = '" + explicitContent + '\'' +
                        ",listennotes_edit_url = '" + listennotesEditUrl + '\'' +
                        ",audio_length_sec = '" + audioLengthSec + '\'' +
                        ",description = '" + description + '\'' +
                        ",id = '" + id + '\'' +
                        ",audio = '" + audio + '\'' +
                        ",title = '" + title + '\'' +
                        ",pub_date_ms = '" + pubDateMs + '\'' +
                        ",listennotes_url = '" + listennotesUrl + '\'' +
                        ",maybe_audio_invalid = '" + maybeAudioInvalid + '\'' +
                        "}";
    }
}