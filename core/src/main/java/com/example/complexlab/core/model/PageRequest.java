package com.example.complexlab.core.model;

public class PageRequest {
    private final int page;
    private final int size;
    private final String sortBy;
    private final boolean ascending;

    public PageRequest(int page, int size) {
        this(page, size, "createdAt", true);
    }

    public PageRequest(int page, int size, String sortBy, boolean ascending) {
        this.page = Math.max(page, 0);
        this.size = Math.max(size, 1);
        this.sortBy = (sortBy == null || sortBy.isBlank()) ? "createdAt" : sortBy;
        this.ascending = ascending;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public String getSortBy() {
        return sortBy;
    }

    public boolean isAscending() {
        return ascending;
    }
}
