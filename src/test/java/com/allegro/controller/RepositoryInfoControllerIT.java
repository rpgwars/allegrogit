package com.allegro.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.net.MalformedURLException;
import java.net.URL;

import com.allegro.model.RepositoryInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RepositoryInfoControllerIT {

    @LocalServerPort
    private int port;

    private String base;

    @Autowired
    private TestRestTemplate template;

    @Before
    public void setUp() {
        this.base = "http://localhost:" + port + "/repositories/";
    }

    @Test
    public void shouldGetRepoDetails() throws MalformedURLException {
        URL url = new URL(base + "rpgwars/IOSR2013");
        ResponseEntity<String> response = template.getForEntity(url.toString(), String.class);
        assertThat(response.getBody(),
                equalTo("{\"fullName\":\"rpgwars/IOSR2013\",\"description\":\"AGH IOSR project repository\",\"cloneUrl\":\"https://github.com/rpgwars/IOSR2013.git\",\"stars\":0,\"createdAt\":\"2013-03-09T08:59:32Z\"}"));
    }

    @Test
    public void shouldNotGetRepoDetailsForWrongURL() throws MalformedURLException {
        URL url = new URL(base + "rpgwarsrpgwarsrpgwarsrpgwarsrpgwarsrpgwarsrpgwarsrpgwarsrpgwarsrpgwarsrpgwarsrpgwarsrpgwars/IOSR2013");
        ResponseEntity<String> response = template.getForEntity(url.toString(), String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void shouldNotGetRepoDetailsForNonExistingUser() throws MalformedURLException {
        URL url = new URL(base + "nonExistingUser/nonExistingRepo");
        ResponseEntity<String> response = template.getForEntity(url.toString(), String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }


}
