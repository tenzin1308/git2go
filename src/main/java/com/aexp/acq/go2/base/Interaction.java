package com.aexp.acq.go2.base;

import com.aexp.acq.go2.utils.BaseUtils;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class Interaction {

  private static final OkHttpClient client = new OkHttpClient();
  private static final String token = System.getenv("");
  private static final String githubApiVersion = System.getenv(""); // "application/vnd.github.v3+json"
  private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

  public Interaction() {
    if (token == null || BaseUtils.isNullOrEmpty(token)) {
      throw new IllegalStateException("GitHub Token not provided");
    }
  }

  private Request.Builder createRequest(String url, Map<String, String> headers) {
    Request.Builder requestBuilder = new Request.Builder()
            .url(url)
            .header("Authorization", "Bearer " + token)
            .header("Accept", githubApiVersion);

    if (headers != null && headers.isEmpty() == false) {
      for (Map.Entry<String, String> entry : headers.entrySet()) {
        requestBuilder = requestBuilder.header(entry.getKey(), entry.getValue());
      }
    }

    return requestBuilder;
  }

  protected final Object get(String url) throws IOException {
    Map<String, String> header = new HashMap<>();
    return get(url, header);
  }
  
  protected final Object get(String url, Map<String, String> headers) throws IOException {
    Request request = createRequest(url, headers).get().build();
    try (Response response = client.newCall(request).execute()) {
      if (response.isSuccessful() && response.body() != null) {
        return response.body().string();
      }
    }
    // TODO: Handle the error response
    return null;
  }

  protected final Object post(String url, String body) throws IOException {
    Map<String, String> header = new HashMap<>();
    return post(url, header, JSON, body);
  }

  protected final Object post(String url, Map<String, String> headers, MediaType reqMediaType, String body) throws IOException {
    RequestBody requestBody = RequestBody.create(body, reqMediaType);
    Request request = createRequest(url, headers).post(requestBody).build();

    try (Response response = client.newCall(request).execute()) {
      if (response.isSuccessful() && response.body() != null) {
        return response.body().string();
      }
    }
    return null;
  }

  protected final Object put(String url, String body) throws IOException {
    Map<String, String> header = new HashMap<>();
    return put(url, header, JSON, body);
  }

  protected final Object put(String url, Map<String, String> headers, MediaType reqMediaType, String body) throws IOException {
    RequestBody requestBody = RequestBody.create(body, reqMediaType);
    Request request = createRequest(url, headers).put(requestBody).build();

    try (Response response = client.newCall(request).execute()) {
      if (response.isSuccessful() && response.body() != null) {
        return response.body().string();
      }
    }
    return null;
  }

  protected final Object delete(String url) throws IOException {
    Map<String, String> header = new HashMap<>();
    return delete(url, header);
  }

  protected final Object delete(String url, Map<String, String> headers) throws IOException {
    Request request = createRequest(url, headers).delete().build();
    try (Response response = client.newCall(request).execute()) {
      if (response.isSuccessful() && response.body() != null) {
        return response.body().string();
      }
    }
    return null;
  }

}
