package com.allegro.controller;

import com.allegro.exception.NotFoundException;
import com.allegro.model.RepositoryInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.allegro.service.RepositoryInfoService;

@RestController
public class RepositoryInfoController {

    @Autowired
    private RepositoryInfoService repositoryInfoService;

    @RequestMapping("/repositories/{owner}/{repositoryName}")
    public @ResponseBody ResponseEntity<RepositoryInfo> getRepositoryInfo(@PathVariable String owner,
                                                                          @PathVariable String repositoryName,
                                                                          @RequestHeader(value="Date", required = false) String date) {
        if(!validatePathVariableString(owner) || !validatePathVariableString(repositoryName))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            return new ResponseEntity<>(repositoryInfoService.getRepositoryInfo(owner, repositoryName), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //https://jira.spring.io/browse/SPR-6380
    private boolean validatePathVariableString(String pathVariableString){
        return pathVariableString.length() <= 30;
    }
}
