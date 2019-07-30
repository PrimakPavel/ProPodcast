package com.pavelprymak.propodcast.model.network.pojo.search;

import java.util.List;
import com.squareup.moshi.Json;


public class ResultsItem{

	@Json(name = "image")
	private String image;

	@Json(name = "thumbnail")
	private String thumbnail;

	@Json(name = "description_original")
	private String descriptionOriginal;

	@Json(name = "itunes_id")
	private int itunesId;

	@Json(name = "explicit_content")
	private boolean explicitContent;

	@Json(name = "publisher_highlighted")
	private String publisherHighlighted;

	@Json(name = "earliest_pub_date_ms")
	private long earliestPubDateMs;

	@Json(name = "genre_ids")
	private List<Integer> genreIds;

	@Json(name = "listennotes_url")
	private String listennotesUrl;

	@Json(name = "total_episodes")
	private int totalEpisodes;

	@Json(name = "title_highlighted")
	private String titleHighlighted;

	@Json(name = "title_original")
	private String titleOriginal;

	@Json(name = "rss")
	private String rss;

	@Json(name = "description_highlighted")
	private String descriptionHighlighted;

	@Json(name = "publisher_original")
	private String publisherOriginal;

	@Json(name = "latest_pub_date_ms")
	private long latestPubDateMs;

	@Json(name = "id")
	private String id;

	@Json(name = "email")
	private String email;

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setThumbnail(String thumbnail){
		this.thumbnail = thumbnail;
	}

	public String getThumbnail(){
		return thumbnail;
	}

	public void setDescriptionOriginal(String descriptionOriginal){
		this.descriptionOriginal = descriptionOriginal;
	}

	public String getDescriptionOriginal(){
		return descriptionOriginal;
	}

	public void setItunesId(int itunesId){
		this.itunesId = itunesId;
	}

	public int getItunesId(){
		return itunesId;
	}

	public void setExplicitContent(boolean explicitContent){
		this.explicitContent = explicitContent;
	}

	public boolean isExplicitContent(){
		return explicitContent;
	}

	public void setPublisherHighlighted(String publisherHighlighted){
		this.publisherHighlighted = publisherHighlighted;
	}

	public String getPublisherHighlighted(){
		return publisherHighlighted;
	}

	public void setEarliestPubDateMs(long earliestPubDateMs){
		this.earliestPubDateMs = earliestPubDateMs;
	}

	public long getEarliestPubDateMs(){
		return earliestPubDateMs;
	}

	public void setGenreIds(List<Integer> genreIds){
		this.genreIds = genreIds;
	}

	public List<Integer> getGenreIds(){
		return genreIds;
	}

	public void setListennotesUrl(String listennotesUrl){
		this.listennotesUrl = listennotesUrl;
	}

	public String getListennotesUrl(){
		return listennotesUrl;
	}

	public void setTotalEpisodes(int totalEpisodes){
		this.totalEpisodes = totalEpisodes;
	}

	public int getTotalEpisodes(){
		return totalEpisodes;
	}

	public void setTitleHighlighted(String titleHighlighted){
		this.titleHighlighted = titleHighlighted;
	}

	public String getTitleHighlighted(){
		return titleHighlighted;
	}

	public void setTitleOriginal(String titleOriginal){
		this.titleOriginal = titleOriginal;
	}

	public String getTitleOriginal(){
		return titleOriginal;
	}

	public void setRss(String rss){
		this.rss = rss;
	}

	public String getRss(){
		return rss;
	}

	public void setDescriptionHighlighted(String descriptionHighlighted){
		this.descriptionHighlighted = descriptionHighlighted;
	}

	public String getDescriptionHighlighted(){
		return descriptionHighlighted;
	}

	public void setPublisherOriginal(String publisherOriginal){
		this.publisherOriginal = publisherOriginal;
	}

	public String getPublisherOriginal(){
		return publisherOriginal;
	}

	public void setLatestPubDateMs(long latestPubDateMs){
		this.latestPubDateMs = latestPubDateMs;
	}

	public long getLatestPubDateMs(){
		return latestPubDateMs;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	@Override
 	public String toString(){
		return 
			"ResultsItem{" + 
			"image = '" + image + '\'' + 
			",thumbnail = '" + thumbnail + '\'' + 
			",description_original = '" + descriptionOriginal + '\'' + 
			",itunes_id = '" + itunesId + '\'' + 
			",explicit_content = '" + explicitContent + '\'' + 
			",publisher_highlighted = '" + publisherHighlighted + '\'' + 
			",earliest_pub_date_ms = '" + earliestPubDateMs + '\'' + 
			",genre_ids = '" + genreIds + '\'' + 
			",listennotes_url = '" + listennotesUrl + '\'' + 
			",total_episodes = '" + totalEpisodes + '\'' + 
			",title_highlighted = '" + titleHighlighted + '\'' + 
			",title_original = '" + titleOriginal + '\'' + 
			",rss = '" + rss + '\'' + 
			",description_highlighted = '" + descriptionHighlighted + '\'' + 
			",publisher_original = '" + publisherOriginal + '\'' + 
			",latest_pub_date_ms = '" + latestPubDateMs + '\'' + 
			",id = '" + id + '\'' + 
			",email = '" + email + '\'' + 
			"}";
		}
}