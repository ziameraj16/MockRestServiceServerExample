package com.zia.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.client.MockRestServiceServer.createServer;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


/**
 * Mock Rest Service Server Example
 */
@ContextConfiguration(locations = {"classpath:/applicationContext-test.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class MockRestServiceServerExample {

    public static final String REQUEST_URL = "https://google.com";

    @Autowired private RestService restService;
    @Autowired private RestTemplate restTemplate;

    @Test
    public void test200() {
        MockRestServiceServer mockServer = createServer(restTemplate);
        mockServer.expect(requestTo(REQUEST_URL))
                .andExpect(method(GET))
                .andRespond(withSuccess("{\"status\": \"SUCCESS\",\"reference\":\"0987654321\"}"
                        , MediaType.APPLICATION_JSON));

        String response = restService.callService();
        assertEquals("{\"status\": \"SUCCESS\",\"reference\":\"0987654321\"}", response);
        mockServer.verify();
    }

    @Test
    public void test404() {
        MockRestServiceServer mockServer = createServer(restTemplate);
        mockServer.expect(requestTo(REQUEST_URL))
                .andExpect(method(GET))
                .andRespond(withStatus(NOT_FOUND));

        try {
            restService.callService();
        } catch (HttpClientErrorException e) {
            assertEquals(e.getStatusCode(), NOT_FOUND);
        }
        mockServer.verify();
    }

    @Test
    public void test500() {
        MockRestServiceServer mockServer = createServer(restTemplate);
        mockServer.expect(requestTo(REQUEST_URL))
                .andExpect(method(GET))
                .andRespond(withServerError());

        try {
            restService.callService();
        } catch (HttpServerErrorException e) {
            assertEquals(e.getStatusCode(), INTERNAL_SERVER_ERROR);
        }
        mockServer.verify();
    }
}
