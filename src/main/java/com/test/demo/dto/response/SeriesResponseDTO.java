package com.test.demo.dto.response;

import java.util.List;

import com.test.demo.dto.SeriesItemDTO;

public class SeriesResponseDTO {
    private int page;
    private List<SeriesItemDTO> results;
    private int totalPages;
    private int totalResults;

    // Getters and Setters
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<SeriesItemDTO> getResults() {
        return results;
    }

    public void setResults(List<SeriesItemDTO> results) {
        this.results = results;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}

