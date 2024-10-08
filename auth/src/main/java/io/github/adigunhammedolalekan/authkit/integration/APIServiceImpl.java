package io.github.adigunhammedolalekan.authkit.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;



public class APIServiceImpl implements APIService {

    enum HttpMethod {
        GET, POST
    }

    private final Logger LOGGER = LoggerFactory.getLogger(APIService.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final HttpClient httpClient;

    public APIServiceImpl(
            HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public <R> R post(String url, Map<String, String> body, Class<R> responseType, String...headers) throws ExecutionException, InterruptedException {
        return makeRequest(HttpMethod.POST,
                url,
                body,
                responseType,
                Collections.emptyMap(),
                headers);
    }

    @Override
    public <R> R get(String url, Class<R> responseType, String...headers) throws ExecutionException, InterruptedException {
        return makeRequest(HttpMethod.GET,
                url,
                Collections.emptyMap(),
                responseType,
                Collections.emptyMap(),
                headers);
    }

    @Override
    public <R> R getWithParams(String url, Map<String, String> queryParams, Class<R> responseType, String...headers) throws ExecutionException, InterruptedException {
        return makeRequest(HttpMethod.GET,
                url,
                Collections.emptyMap(),
                responseType,
                queryParams,
                headers);
    }

    private <R> R makeRequest(
            HttpMethod method,
            String url,
            Map<String, String> body,
            Class<R> responseType,
            Map<String, String> queryParams,
            String...headers) throws ExecutionException, InterruptedException {
        var fullUrl = URI.create(url + mapToQueryString(queryParams));
        var requestBuilder = HttpRequest.newBuilder()
                .uri(fullUrl)
                .version(HttpClient.Version.HTTP_2)
                .method(method.name(), HttpRequest.BodyPublishers.ofString(mapToQueryString(body)))
                .header("Accept", "application/json");
        if (headers.length > 0) {
            requestBuilder.headers(headers);
        }

        LOGGER.info("Sending request: url={}, urlParams={}, responseType={}",
                fullUrl, queryParams, responseType);

        return httpClient.sendAsync(requestBuilder.build(), HttpResponse.BodyHandlers.ofString())
                .thenApply(responseBody -> convertOrThrow(responseBody, responseType))
                .get();
    }

    private <R> R convertOrThrow(HttpResponse<String> response, Class<R> clazz) {
        if (!Arrays.asList(200, 201).contains(response.statusCode())) {
            throw new RuntimeException(response.body());
        }

        var body = response.body();
        LOGGER.info("Reading value from response body: {}", body);
        try {
            return MAPPER.readValue(body, clazz);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private String mapToQueryString(Map<String, String> queryParams) {
        return queryParams
                .entrySet()
                .stream()
                .map(entry -> urlEncode(entry.getKey()) + "=" + urlEncode(entry.getValue()))
                .collect(Collectors.joining("&"));
    }

    private String urlEncode(String input) {
        return URLEncoder.encode(input, StandardCharsets.UTF_8);
    }
}
