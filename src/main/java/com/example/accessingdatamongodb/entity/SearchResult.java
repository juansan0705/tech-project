package com.example.accessingdatamongodb.entity;

public class SearchResult {
	
    private String searchId;
    private PayloadEntity search;
    private int count;
    
    public SearchResult(String searchId, PayloadEntity search, int count) {
        this.searchId = searchId;
        this.search = search;
        this.count = count;
    }
    
	public String getSearchId() {
		return searchId;
	}
	public void setSearchId(String searchId) {
		this.searchId = searchId;
	}
	public PayloadEntity getSearch() {
		return search;
	}
	public void setSearch(PayloadEntity search) {
		this.search = search;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}

    
}