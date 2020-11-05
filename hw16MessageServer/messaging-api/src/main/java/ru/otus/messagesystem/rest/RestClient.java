package ru.otus.messagesystem.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RestClient {
    private static final Logger logger = LoggerFactory.getLogger(RestClient.class);

    private final String serverSocket;
    private final RestTemplate rest;
    private final HttpHeaders headers;

    public RestClient(String serverSocket) {
        this.serverSocket = serverSocket;
        this.rest = new RestTemplate();
        this.headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "*/*");
    }

    public ResponseEntity<String> post(String uri, String json) {
        HttpEntity<String> requestEntity = new HttpEntity<String>(json, headers);
        logger.info("Posting new message:\n {}", requestEntity.toString());
        ResponseEntity<String> response = rest.exchange(serverSocket + uri, HttpMethod.POST, requestEntity, String.class);
        logger.info("Received response:\n {}", response.toString());
        return response;
    }

}