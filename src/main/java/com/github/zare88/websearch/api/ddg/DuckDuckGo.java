package com.github.zare88.websearch.api.ddg;

import com.google.gson.Gson;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class DuckDuckGo {

    public static final String API_BASE_URL = "https://api.duckduckgo.com/";
    public static final String BASE_URL = "https://duckduckgo.com/";
    public static final String USER_AGENT = "Mozilla/5.0";

    private static DuckDuckGoResponse convertToResponse(String responseStr) {
        @SuppressWarnings("unchecked")
        Map<String, String> responseMap = new Gson().fromJson(responseStr, Map.class);
        return new DuckDuckGoResponse(responseMap.get("AbstractText"), responseMap.get("AbstractURL"), responseMap.get("Image")
                , queryImage(responseMap.get("AbstractText")));
    }

    private static CompletableFuture<byte[]> queryImage(String relativeImageURL) {
        var request = HttpRequest.newBuilder()
                .setHeader("User-Agent", USER_AGENT)
                .uri(URI.create(String.format("%s/%s", API_BASE_URL, relativeImageURL)))
                .GET()
                .build();
        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofByteArray())
                .thenApply(HttpResponse::body);
    }

    public String getFormattedText(String keyword, String text) {
        Objects.requireNonNull(keyword);
        if (keyword.trim().equals("")) {
            throw new IllegalStateException("Keyword is empty");
        }
        if (text.length() > 800) {
            text = text.substring(0, 800) + "...";
        }
        return String.format("%s%n%n%s%n%n%s", keyword, text,
                BASE_URL + URLEncoder.encode(keyword, StandardCharsets.UTF_8));
    }

    public CompletableFuture<DuckDuckGoResponse> query(String keyword) {
        Objects.requireNonNull(keyword);
        if (keyword.trim().equals("")) {
            throw new IllegalStateException("Keyword is empty");
        }
        var request = HttpRequest.newBuilder()
                .setHeader("User-Agent", USER_AGENT)
                .uri(URI.create(String.format("%s?q=%s&no_html=1&skip_disambig=1&format=json&no_redirect=1",
                        API_BASE_URL,
                        URLEncoder.encode(keyword, StandardCharsets.UTF_8))))
                .GET()
                .build();

        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(str -> convertToResponse(str.body()));
    }
}
