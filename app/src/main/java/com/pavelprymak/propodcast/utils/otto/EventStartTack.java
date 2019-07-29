package com.pavelprymak.propodcast.utils.otto;

public class EventStartTack {
    String trackLink;
    String trackTitle;
    String imageUrl;
    String trackAuthor;

    public EventStartTack(String trackLink, String trackTitle, String imageUrl, String trackAuthor) {
        this.trackLink = trackLink;
        this.trackTitle = trackTitle;
        this.imageUrl = imageUrl;
        this.trackAuthor = trackAuthor;
    }

    public String getTrackLink() {
        return trackLink;
    }

    public void setTrackLink(String trackLink) {
        this.trackLink = trackLink;
    }

    public String getTrackTitle() {
        return trackTitle;
    }

    public void setTrackTitle(String trackTitle) {
        this.trackTitle = trackTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTrackAuthor() {
        return trackAuthor;
    }

    public void setTrackAuthor(String trackAuthor) {
        this.trackAuthor = trackAuthor;
    }
}
