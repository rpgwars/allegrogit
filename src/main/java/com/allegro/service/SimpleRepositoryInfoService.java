package com.allegro.service;

import com.allegro.model.RepositoryInfo;
import org.springframework.stereotype.Service;

@Service
public class SimpleRepositoryInfoService implements RepositoryInfoService {

    @Override
    public RepositoryInfo getRepositoryInfo(String owner, String repositoryName) {
        return null;
    }
}
