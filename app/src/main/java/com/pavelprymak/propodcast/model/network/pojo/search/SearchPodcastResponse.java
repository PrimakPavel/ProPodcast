package com.pavelprymak.propodcast.model.network.pojo.search;

import java.util.List;
import com.squareup.moshi.Json;


public class SearchPodcastResponse{

	@Json(name = "took")
	private double took;

	@Json(name = "total")
	private int total;

	@Json(name = "count")
	private int count;

	@Json(name = "next_offset")
	private int nextOffset;

	@Json(name = "results")
	private List<ResultsItem> results;

	public void setTook(double took){
		this.took = took;
	}

	public double getTook(){
		return took;
	}

	public void setTotal(int total){
		this.total = total;
	}

	public int getTotal(){
		return total;
	}

	public void setCount(int count){
		this.count = count;
	}

	public int getCount(){
		return count;
	}

	public void setNextOffset(int nextOffset){
		this.nextOffset = nextOffset;
	}

	public int getNextOffset(){
		return nextOffset;
	}

	public void setResults(List<ResultsItem> results){
		this.results = results;
	}

	public List<ResultsItem> getResults(){
		return results;
	}

	@Override
 	public String toString(){
		return 
			"SearchPodcastResponse{" + 
			"took = '" + took + '\'' + 
			",total = '" + total + '\'' + 
			",count = '" + count + '\'' + 
			",next_offset = '" + nextOffset + '\'' + 
			",results = '" + results + '\'' + 
			"}";
		}
}