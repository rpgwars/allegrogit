package com.allegro.service;

import com.allegro.exception.NotFoundException;
import com.allegro.model.RepositoryInfo;
import org.springframework.stereotype.Service;

public interface RepositoryInfoService {
    RepositoryInfo getRepositoryInfo(String owner, String repositoryName) throws Exception;
}
