package com.pavelprymak.propodcast.model.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_episodes")
public class FavoriteEpisodeEntity {

    @PrimaryKey()
    @NonNull
    private String id;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "thumbnail")
    private String thumbnail;

    @ColumnInfo(name = "audio_length_sec")
    private Integer audioLengthSec;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "audio")
    private String audio;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "pub_date_ms")
    private long pubDateMs;

    @ColumnInfo(name = "listennotes_url")
    private String listennotesUrl;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Integer getAudioLengthSec() {
        return audioLengthSec;
    }

    public void setAudioLengthSec(Integer audioLengthSec) {
        this.audioLengthSec = audioLengthSec;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getPubDateMs() {
        return pubDateMs;
    }

    public void setPubDateMs(long pubDateMs) {
        this.pubDateMs = pubDateMs;
    }

    public String getListennotesUrl() {
        return listennotesUrl;
    }

    public void setListennotesUrl(String listennotesUrl) {
        this.listennotesUrl = listennotesUrl;
    }

    @Override
    public String toString() {
        return "FavoriteEpisodeEntity{" +
                "id='" + id + '\'' +
                ", image='" + image + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", audioLengthSec=" + audioLengthSec +
                ", description='" + description + '\'' +
                ", audio='" + audio + '\'' +
                ", title='" + title + '\'' +
                ", pubDateMs=" + pubDateMs +
                ", listennotesUrl='" + listennotesUrl + '\'' +
                '}';
    }
}
