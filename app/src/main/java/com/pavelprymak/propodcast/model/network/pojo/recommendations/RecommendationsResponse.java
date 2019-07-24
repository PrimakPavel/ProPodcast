package com.pavelprymak.propodcast.model.network.pojo.recommendations;

import java.util.List;

import com.pavelprymak.propodcast.model.network.pojo.podcasts.PodcastItem;
import com.squareup.moshi.Json;


public class RecommendationsResponse{

	@Json(name = "recommendations")
	private List<PodcastItem> recommendations;

	public void setRecommendations(List<PodcastItem> recommendations){
		this.recommendations = recommendations;
	}

	public List<PodcastItem> getRecommendations(){
		return recommendations;
	}

	@Override
 	public String toString(){
		return 
			"RecommendationsResponse{" + 
			"recommendations = '" + recommendations + '\'' + 
			"}";
		}
}