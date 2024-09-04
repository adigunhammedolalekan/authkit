package io.github.adigunhammedolalekan.authkit.integration;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface APIService {

    <R> R post(String url, Map<String, String> body, Class<R> responseType, String...headers) throws ExecutionException, InterruptedException;

    <R> R get(String url, Class<R> responseType, String...headers) throws ExecutionException, InterruptedException;

    <R> R getWithParams(String url, Map<String, String> queryParams, Class<R> responseType, String...headers) throws ExecutionException, InterruptedException;
}
