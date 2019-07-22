package com.pavelprymak.propodcast.model.network.pojo.recommendations;

import java.util.List;
import com.squareup.moshi.Json;


public class RecommendationsResponse{

	@Json(name = "recommendations")
	private List<RecommendationsItem> recommendations;

	public void setRecommendations(List<RecommendationsItem> recommendations){
		this.recommendations = recommendations;
	}

	public List<RecommendationsItem> getRecommendations(){
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