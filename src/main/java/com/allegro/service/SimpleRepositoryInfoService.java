package com.allegro.service;

import com.allegro.exception.NotFoundException;
import com.allegro.exception.ParseException;
import com.allegro.model.RepositoryInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Service
public class SimpleRepositoryInfoService implements RepositoryInfoService {

    private static final String GITHUB_API_ROOT = "https://api.github.com";
    private static final String userRepositoryPath = "%s/repos/%s/%s";

    private static final String FULL_NAME_PROPERTY = "full_name";
    private static final String DESCRIPTION_PROPERTY = "description";
    private static final String CLONE_URL_PROPERTY = "clone_url" ;
    private static final String STARS_PROPERTY = "stargazers_count";
    private static final String CREATED_AT_PROPERTY = "created_at";

    private static final List<String> REPOSITORY_PROPERTIES = Arrays.asList(FULL_NAME_PROPERTY, DESCRIPTION_PROPERTY,
            CLONE_URL_PROPERTY, STARS_PROPERTY, CREATED_AT_PROPERTY);

    @Autowired
    private CloseableHttpClient defaultHttpClient;

    @Autowired
    private ObjectMapper objectMapper;

    private final Logger logger = Logger.getLogger(SimpleRepositoryInfoService.class.getName());

    @Override
    public RepositoryInfo getRepositoryInfo(String owner, String repositoryName) throws Exception {
        HttpGet httpGet = new HttpGet(getUserRepositoryUrl(owner, repositoryName));
        try {
            CloseableHttpResponse response = defaultHttpClient.execute(httpGet);
            if(response.getStatusLine().getStatusCode() == OK.value()) {
                return createResponse(parseResponse(response));
            }
            else if(response.getStatusLine().getStatusCode() == NOT_FOUND.value()){
                logger.log(Level.INFO, "Repository " + repositoryName + " of user " + owner + " not found");
                throw new NotFoundException();
            }
            else{
                logger.log(Level.SEVERE, "Wrong http status " + response.getStatusLine().getStatusCode() + " status");
                throw new Exception();
            }
        } catch (IOException | ParseException e) {
            logger.log(Level.SEVERE, "Unable to connect to get the data from api", e);
            throw new Exception();
        }
    }

    private RepositoryInfo createResponse(Map<String, Object> repositoryInfoJSONMap){
        REPOSITORY_PROPERTIES.stream()
                .filter(property -> repositoryInfoJSONMap.get(property) == null)
                .forEach(property -> logger.log(Level.WARNING, "property " + property + " not found"));

        return new RepositoryInfo((String)repositoryInfoJSONMap.get(FULL_NAME_PROPERTY),
                (String)repositoryInfoJSONMap.get(DESCRIPTION_PROPERTY),
                (String)repositoryInfoJSONMap.get(CLONE_URL_PROPERTY),
                (int)repositoryInfoJSONMap.get(STARS_PROPERTY),
                (String)repositoryInfoJSONMap.get(CREATED_AT_PROPERTY));
    }

    private Map<String, Object> parseResponse(CloseableHttpResponse response) throws IOException, ParseException {
        HttpEntity entity = response.getEntity();
        try {
            return objectMapper.readValue(response.getEntity().getContent(), Map.class);
        } catch (IOException e) {
            throw new ParseException();
        }
        finally{
            EntityUtils.consume(entity);
        }
    }

    private String getUserRepositoryUrl(String owner, String repositoryName){
        return String.format(userRepositoryPath, GITHUB_API_ROOT, owner, repositoryName);
    }
}
