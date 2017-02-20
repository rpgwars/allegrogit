package com.allegro.service;

import com.allegro.exception.NotFoundException;
import com.allegro.model.RepositoryInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(SpringRunner.class)
public class SimpleRepositoryInfoServiceTest {

    private static final String FULL_NAME_PROPERTY = "full_name";
    private static final String DESCRIPTION_PROPERTY = "description";
    private static final String CLONE_URL_PROPERTY = "clone_url" ;
    private static final String STARS_PROPERTY = "stargazers_count";
    private static final String CREATED_AT_PROPERTY = "created_at";

    @InjectMocks
    private SimpleRepositoryInfoService simpleRepositoryInfoService;

    @Mock
    private CloseableHttpClient closeableHttpClient;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private CloseableHttpResponse closeableHttpResponse;

    @Mock
    private StatusLine statusLine;

    @Mock
    private HttpEntity httpEntity;

    @Mock
    private InputStream inputStream;

    @Mock
    private Map map;

    @Before
    public void setUp() throws IOException {
        given(closeableHttpClient.execute(any(HttpGet.class))).willReturn(closeableHttpResponse);
        given(closeableHttpResponse.getStatusLine()).willReturn(statusLine);
    }

    @Test
    public void shouldReturnRepositoryInfo() throws IOException {
        //given
        given(statusLine.getStatusCode()).willReturn(200);
        given(closeableHttpResponse.getEntity()).willReturn(httpEntity);
        given(httpEntity.getContent()).willReturn(inputStream);
        given(objectMapper.readValue(inputStream, Map.class)).willReturn(map);
        given(map.get(FULL_NAME_PROPERTY)).willReturn("some full name");
        given(map.get(CLONE_URL_PROPERTY)).willReturn("some clone url");
        given(map.get(DESCRIPTION_PROPERTY)).willReturn("some description");
        given(map.get(STARS_PROPERTY)).willReturn(5);
        given(map.get(CREATED_AT_PROPERTY)).willReturn("some created at");

        //when
        RepositoryInfo repositoryInfo = null;
        try {
            repositoryInfo = simpleRepositoryInfoService.getRepositoryInfo("a", "b", null);
        } catch (Exception e) {
            fail();
        }

        //then
        assertThat(repositoryInfo.getFullName(), equalTo("some full name"));
        assertThat(repositoryInfo.getCloneUrl(), equalTo("some clone url"));
        assertThat(repositoryInfo.getDescription(), equalTo("some description"));
        assertThat(repositoryInfo.getStars(), equalTo(5));
        assertThat(repositoryInfo.getCreatedAt(), equalTo("some created at"));
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowExceptionOnNotFound() throws Exception {
        //given
        given(statusLine.getStatusCode()).willReturn(404);

        //when
        simpleRepositoryInfoService.getRepositoryInfo("a", "b", null);

        //then
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionOnWrongHttpStatus() throws Exception {
        //given
        given(statusLine.getStatusCode()).willReturn(402);

        //when
        simpleRepositoryInfoService.getRepositoryInfo("a", "b", null);

        //then
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionOnParseException() throws Exception {
        //given
        given(statusLine.getStatusCode()).willReturn(200);
        given(closeableHttpResponse.getEntity()).willReturn(httpEntity);
        given(httpEntity.getContent()).willReturn(inputStream);
        given(objectMapper.readValue(inputStream, Map.class)).willThrow(IOException.class);

        //when
        simpleRepositoryInfoService.getRepositoryInfo("a", "b", null);

        //then
    }

    @Test
    public void shouldReturnRepositoryInfoDateFormatted() throws IOException {
        //given
        given(statusLine.getStatusCode()).willReturn(200);
        given(closeableHttpResponse.getEntity()).willReturn(httpEntity);
        given(httpEntity.getContent()).willReturn(inputStream);
        given(objectMapper.readValue(inputStream, Map.class)).willReturn(map);
        given(map.get(CREATED_AT_PROPERTY)).willReturn("2017-02-16T07:37:22Z");
        given(map.get(STARS_PROPERTY)).willReturn(5);
        Locale plLocale = Locale.forLanguageTag("pl-PL");
        //no static mocking
        simpleRepositoryInfoService.setDateTimeFormatter(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL));

        //when
        RepositoryInfo repositoryInfo = null;
        try {
            repositoryInfo = simpleRepositoryInfoService.getRepositoryInfo("a", "b", plLocale);
        } catch (Exception e) {
            fail();
        }

        //then
        assertThat(repositoryInfo.getCreatedAt(), equalTo("czwartek, 16 lutego 2017 07:37:22 Z"));
    }
}
