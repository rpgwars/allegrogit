package com.allegro.service;

import com.allegro.model.RepositoryInfo;

import java.util.Locale;

public interface RepositoryInfoService {
    RepositoryInfo getRepositoryInfo(String owner, String repositoryName, Locale locale) throws Exception;
}
