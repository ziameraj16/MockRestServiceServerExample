package com.zia.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Sample Rest Client.
 */
@Service
public class RestService {

    public static final String REQUEST_URL = "https://google.com";

    @Autowired private RestTemplate restTemplate;

    public String callService() {
        return restTemplate.getForObject(REQUEST_URL, String.class);
    }
}
