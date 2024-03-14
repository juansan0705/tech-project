package com.example.accessingdatamongodb.entity;

public class SearchResponse {
	
    private String searchId;

    public SearchResponse(String searchId) {
        this.searchId = searchId;
    }

	public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }
}