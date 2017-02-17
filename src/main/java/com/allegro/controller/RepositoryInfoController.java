package com.allegro.controller;

import com.allegro.model.RepositoryInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.allegro.service.RepositoryInfoService;

@RestController
public class RepositoryInfoController {

    @Autowired
    private RepositoryInfoService repositoryInfoService;

    @RequestMapping("/repositories/{owner}/{repositoryName}")
    public @ResponseBody ResponseEntity<RepositoryInfo> getRepositoryInfo(@PathVariable String owner, @PathVariable String repositoryName) {
        return null;
    }
}
