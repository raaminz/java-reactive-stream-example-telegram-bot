package com.github.zare88.websearch.api.ddg;

import java.util.concurrent.CompletableFuture;

public record DuckDuckGoResponse(String abstractText, String abstractURL,
                                 String imageURL, CompletableFuture<byte[]> imageData) {
}
