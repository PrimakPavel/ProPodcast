package com.pavelprymak.propodcast.model.network.pojo.recommendations;

import com.squareup.moshi.Json;


public class LookingFor{

	@Json(name = "cross_promotion")
	private boolean crossPromotion;

	@Json(name = "sponsors")
	private boolean sponsors;

	@Json(name = "guests")
	private boolean guests;

	@Json(name = "cohosts")
	private boolean cohosts;

	public void setCrossPromotion(boolean crossPromotion){
		this.crossPromotion = crossPromotion;
	}

	public boolean isCrossPromotion(){
		return crossPromotion;
	}

	public void setSponsors(boolean sponsors){
		this.sponsors = sponsors;
	}

	public boolean isSponsors(){
		return sponsors;
	}

	public void setGuests(boolean guests){
		this.guests = guests;
	}

	public boolean isGuests(){
		return guests;
	}

	public void setCohosts(boolean cohosts){
		this.cohosts = cohosts;
	}

	public boolean isCohosts(){
		return cohosts;
	}

	@Override
 	public String toString(){
		return 
			"LookingFor{" + 
			"cross_promotion = '" + crossPromotion + '\'' + 
			",sponsors = '" + sponsors + '\'' + 
			",guests = '" + guests + '\'' + 
			",cohosts = '" + cohosts + '\'' + 
			"}";
		}
}