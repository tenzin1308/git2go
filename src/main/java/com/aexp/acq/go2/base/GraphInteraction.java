package com.aexp.acq.go2.base;

import com.aexp.acq.go2.utils.BaseUtils;
import okhttp3.*;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

public abstract class GraphInteraction extends BaseComponent {

  private static final OkHttpClient client = new OkHttpClient();
  private static final MediaType JSON = MediaType.get("application/json");
  private static final String token = App.instance().getProperty("github.graphql.token");

  public GraphInteraction(String name) {
    super(name);
    if (BaseUtils.isNullOrEmpty(token)) {
      throw new IllegalStateException("GitHub GraphQL Token not provided");
    }
  }

  private Request.Builder createRequest(String url, Map<String, String> headers) {
    Request.Builder requestBuilder = new Request.Builder()
            .url(url)
            .header("Authorization", "Bearer " + token)
            .header("Content-Type", JSON.toString());

    if (headers != null && headers.isEmpty() == false) {
      for (Map.Entry<String, String> entry : headers.entrySet()) {
        requestBuilder = requestBuilder.header(entry.getKey(), entry.getValue());
      }
    }

    return requestBuilder;
  }

  protected final Object post(String url, Map<String, String> headers, MediaType reqMediaType, String query) {
    GraphResponse graphResponse = new GraphResponse();
    RequestBody requestBody = RequestBody.create(query, reqMediaType);
    Request request = createRequest(url, headers).post(requestBody).build();
    long then = Instant.now().toEpochMilli();

    try (Response response = client.newCall(request).execute()) {
      if (response.isSuccessful() && response.body() != null) {
        graphResponse.setResponse(response.body().string());
      }
      else {
        graphResponse.setResponse("");
      }
      graphResponse.setMessage(response.message());
      graphResponse.setStatusCode(response.code());
      graphResponse.setTime(response.receivedResponseAtMillis() - response.sentRequestAtMillis());

    }
    catch (IOException e) {
      graphResponse.setMessage(e.getClass().getSimpleName());
      graphResponse.setStatusCode(500);
      graphResponse.setResponse(e.getMessage());
      graphResponse.setTime(Instant.now().toEpochMilli() - then);
    }
    return graphResponse;
  }

}
