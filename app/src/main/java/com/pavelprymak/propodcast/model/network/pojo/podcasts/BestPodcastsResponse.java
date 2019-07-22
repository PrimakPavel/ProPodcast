package com.pavelprymak.propodcast.model.network.pojo.podcasts;

import java.util.List;
import com.squareup.moshi.Json;


public class BestPodcastsResponse {

	@Json(name = "total")
	private int total;

	@Json(name = "podcasts")
	private List<PodcastItem> podcasts;

	@Json(name = "page_number")
	private int pageNumber;

	@Json(name = "has_previous")
	private boolean hasPrevious;

	@Json(name = "parent_id")
	private Object parentId;

	@Json(name = "next_page_number")
	private int nextPageNumber;

	@Json(name = "name")
	private String name;

	@Json(name = "has_next")
	private boolean hasNext;

	@Json(name = "id")
	private int id;

	@Json(name = "listennotes_url")
	private String listennotesUrl;

	@Json(name = "previous_page_number")
	private int previousPageNumber;

	public void setTotal(int total){
		this.total = total;
	}

	public int getTotal(){
		return total;
	}

	public void setPodcasts(List<PodcastItem> podcasts){
		this.podcasts = podcasts;
	}

	public List<PodcastItem> getPodcasts(){
		return podcasts;
	}

	public void setPageNumber(int pageNumber){
		this.pageNumber = pageNumber;
	}

	public int getPageNumber(){
		return pageNumber;
	}

	public void setHasPrevious(boolean hasPrevious){
		this.hasPrevious = hasPrevious;
	}

	public boolean isHasPrevious(){
		return hasPrevious;
	}

	public void setParentId(Object parentId){
		this.parentId = parentId;
	}

	public Object getParentId(){
		return parentId;
	}

	public void setNextPageNumber(int nextPageNumber){
		this.nextPageNumber = nextPageNumber;
	}

	public int getNextPageNumber(){
		return nextPageNumber;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setHasNext(boolean hasNext){
		this.hasNext = hasNext;
	}

	public boolean isHasNext(){
		return hasNext;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setListennotesUrl(String listennotesUrl){
		this.listennotesUrl = listennotesUrl;
	}

	public String getListennotesUrl(){
		return listennotesUrl;
	}

	public void setPreviousPageNumber(int previousPageNumber){
		this.previousPageNumber = previousPageNumber;
	}

	public int getPreviousPageNumber(){
		return previousPageNumber;
	}

	@Override
 	public String toString(){
		return 
			"BestPodcastsResponse{" +
			"total = '" + total + '\'' + 
			",podcasts = '" + podcasts + '\'' + 
			",page_number = '" + pageNumber + '\'' + 
			",has_previous = '" + hasPrevious + '\'' + 
			",parent_id = '" + parentId + '\'' + 
			",next_page_number = '" + nextPageNumber + '\'' + 
			",name = '" + name + '\'' + 
			",has_next = '" + hasNext + '\'' + 
			",id = '" + id + '\'' + 
			",listennotes_url = '" + listennotesUrl + '\'' + 
			",previous_page_number = '" + previousPageNumber + '\'' + 
			"}";
		}
}