package com.allegro.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.allegro.exception.NotFoundException;
import com.allegro.model.RepositoryInfo;
import com.allegro.service.RepositoryInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.HttpServletRequest;

@RunWith(SpringRunner.class)
public class RepositoryInfoControllerTest {

    @InjectMocks
    private RepositoryInfoController repositoryInfoController;

    @Mock
    private RepositoryInfoService repositoryInfoService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Test
    public void shouldReturnOK() throws Exception {
        //given
        given(repositoryInfoService.getRepositoryInfo("some owner", "some name", null)).willReturn(mock(RepositoryInfo.class));

        //when
        ResponseEntity<RepositoryInfo> responseEntity = repositoryInfoController.getRepositoryInfo("some owner", "some name");

        //then
        assertThat(responseEntity.getStatusCodeValue(), equalTo(200));
    }

    @Test
    public void shouldReturnNotFound() throws Exception {
        //given
        given(repositoryInfoService.getRepositoryInfo("some owner", "some name", null)).willThrow(NotFoundException.class);

        //when
        ResponseEntity<RepositoryInfo> responseEntity = repositoryInfoController.getRepositoryInfo("some owner", "some name");

        //then
        assertThat(responseEntity.getStatusCodeValue(), equalTo(404));
    }

    @Test
    public void shouldReturnInternalServerError() throws Exception {
        //given
        given(repositoryInfoService.getRepositoryInfo("some owner", "some name", null)).willThrow(Exception.class);

        //when
        ResponseEntity<RepositoryInfo> responseEntity = repositoryInfoController.getRepositoryInfo("some owner", "some name");

        //then
        assertThat(responseEntity.getStatusCodeValue(), equalTo(500));
    }
}
