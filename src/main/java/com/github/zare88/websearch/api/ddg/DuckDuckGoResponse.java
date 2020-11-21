package com.github.zare88.websearch.api.ddg;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class DuckDuckGoResponse {
    private String abstractText;
    private String abstractURL;
    private String imageURL;
    private CompletableFuture<byte[]> imageData;

    public String getAbstractText() {
        return abstractText;
    }

    public void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }

    public String getAbstractURL() {
        return abstractURL;
    }

    public void setAbstractURL(String abstractURL) {
        this.abstractURL = abstractURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public CompletableFuture<byte[]> getImageData() {
        return imageData;
    }

    public void setImageData(CompletableFuture<byte[]> imageData) {
        this.imageData = imageData;
    }

    @Override
    public String toString() {
        return "DuckDuckGoResponse{" +
                "abstractText='" + abstractText + '\'' +
                ", abstractURL='" + abstractURL + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DuckDuckGoResponse that = (DuckDuckGoResponse) o;
        return abstractText.equals(that.abstractText) &&
                abstractURL.equals(that.abstractURL) &&
                Objects.equals(imageURL, that.imageURL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(abstractText, abstractURL, imageURL);
    }
}
