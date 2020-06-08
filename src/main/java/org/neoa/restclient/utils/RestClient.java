package org.neoa.restclient.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class RestClient {

    private final RestTemplate restTemplate;

    @Autowired
    public RestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> ResponseEntity<T> execute(HttpRequest<T> request) {
        //log.debug("Executing request {}", request);
        try {
            return restTemplate.exchange(request.getUrl(),
                    request.getMethod(),
                    createHttpEntity(request),
                    request.getResponseType(),
                    request.getUrlVars());
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getResponseHeaders(), e.getStatusCode());
        }
    }
    private HttpEntity<?> createHttpEntity(HttpRequest request) {
        if (request.getPostBody() != null) {
            return new HttpEntity<>(request.getPostBody(), request.getHeaders());
        } else {
            return new HttpEntity<>(request.getHeaders());
        }
    }

}
