package com.pavelprymak.propodcast.model.network.pojo.podcasts;

import com.squareup.moshi.Json;


public class Extra {

    @Json(name = "twitter_handle")
    private String twitterHandle;

    @Json(name = "instagram_handle")
    private String instagramHandle;

    @Json(name = "url3")
    private String url3;

    @Json(name = "url1")
    private String url1;

    @Json(name = "url2")
    private String url2;

    @Json(name = "facebook_handle")
    private String facebookHandle;

    @Json(name = "linkedin_url")
    private String linkedinUrl;

    @Json(name = "youtube_url")
    private String youtubeUrl;

    @Json(name = "google_url")
    private String googleUrl;

    @Json(name = "spotify_url")
    private String spotifyUrl;

    @Json(name = "wechat_handle")
    private String wechatHandle;

    @Json(name = "patreon_handle")
    private String patreonHandle;

    public void setTwitterHandle(String twitterHandle) {
        this.twitterHandle = twitterHandle;
    }

    public String getTwitterHandle() {
        return twitterHandle;
    }

    public void setInstagramHandle(String instagramHandle) {
        this.instagramHandle = instagramHandle;
    }

    public String getInstagramHandle() {
        return instagramHandle;
    }

    public void setUrl3(String url3) {
        this.url3 = url3;
    }

    public String getUrl3() {
        return url3;
    }

    public void setUrl1(String url1) {
        this.url1 = url1;
    }

    public String getUrl1() {
        return url1;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }

    public String getUrl2() {
        return url2;
    }

    public void setFacebookHandle(String facebookHandle) {
        this.facebookHandle = facebookHandle;
    }

    public String getFacebookHandle() {
        return facebookHandle;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setGoogleUrl(String googleUrl) {
        this.googleUrl = googleUrl;
    }

    public String getGoogleUrl() {
        return googleUrl;
    }

    public void setSpotifyUrl(String spotifyUrl) {
        this.spotifyUrl = spotifyUrl;
    }

    public String getSpotifyUrl() {
        return spotifyUrl;
    }

    public void setWechatHandle(String wechatHandle) {
        this.wechatHandle = wechatHandle;
    }

    public String getWechatHandle() {
        return wechatHandle;
    }

    public void setPatreonHandle(String patreonHandle) {
        this.patreonHandle = patreonHandle;
    }

    public String getPatreonHandle() {
        return patreonHandle;
    }

    @Override
    public String toString() {
        return
                "Extra{" +
                        "twitter_handle = '" + twitterHandle + '\'' +
                        ",instagram_handle = '" + instagramHandle + '\'' +
                        ",url3 = '" + url3 + '\'' +
                        ",url1 = '" + url1 + '\'' +
                        ",url2 = '" + url2 + '\'' +
                        ",facebook_handle = '" + facebookHandle + '\'' +
                        ",linkedin_url = '" + linkedinUrl + '\'' +
                        ",youtube_url = '" + youtubeUrl + '\'' +
                        ",google_url = '" + googleUrl + '\'' +
                        ",spotify_url = '" + spotifyUrl + '\'' +
                        ",wechat_handle = '" + wechatHandle + '\'' +
                        ",patreon_handle = '" + patreonHandle + '\'' +
                        "}";
    }
}