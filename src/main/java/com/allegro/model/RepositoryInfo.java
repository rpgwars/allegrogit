package com.allegro.model;

import java.time.ZonedDateTime;

public class RepositoryInfo {
    private final String fullName;
    private final String description;
    private final String cloneUrl;
    private final int stars;
    private final String createdAt;

    public RepositoryInfo(String fullName, String description, String cloneUrl, int stars, String createdAt) {
        this.fullName = fullName;
        this.description = description;
        this.cloneUrl = cloneUrl;
        this.stars = stars;
        this.createdAt = createdAt;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDescription() {
        return description;
    }

    public String getCloneUrl() {
        return cloneUrl;
    }

    public int getStars() {
        return stars;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
