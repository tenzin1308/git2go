package com.aexp.acq.go2.base;

import com.aexp.acq.go2.utils.BaseUtils;
import okhttp3.*;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public abstract class Interaction extends BaseComponent {

  private static final OkHttpClient client = new OkHttpClient();
  private static final String token = App.instance().getProperty("GITHUB_TOKEN"); // System.getenv("");
  private static final String githubApiVersion = App.instance().getProperty("GITHUB_API_VERSION"); // System.getenv("");
  private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

  public Interaction(String name) {
    super(name);
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

  private RestResponse handleResponse(Response response) {
    RestResponse restResponse = new RestResponse();
    restResponse.setMessage(response.message());
    restResponse.setStatus(response.code());
    restResponse.setTime(response.receivedResponseAtMillis() - response.sentRequestAtMillis());
    return restResponse;
  }

  private RestResponse handleException(IOException e, long then) {
    RestResponse restResponse = new RestResponse();
    restResponse.setStatus(500);
    restResponse.setErrorCode("IOException");
    restResponse.setMessage(e.getMessage());
    restResponse.setTime(Instant.now().toEpochMilli() - then);
    return restResponse;
  }

  protected final Object get(String url) {
    Map<String, String> header = new HashMap<>();
    return get(url, header);
  }

  protected final Object get(String url, Map<String, String> headers) {
    RestResponse restResponse;
    Request request = createRequest(url, headers).get().build();
    long then = Instant.now().toEpochMilli();

    try (Response response = client.newCall(request).execute()) {
      restResponse = handleResponse(response);
    }
    catch (IOException e) {
      restResponse = handleException(e, then);
    }
    return restResponse;
  }

  protected final Object post(String url, String body) {
    Map<String, String> header = new HashMap<>();
    return post(url, header, JSON, body);
  }

  protected final Object post(String url, Map<String, String> headers, MediaType reqMediaType, String body) {
    RestResponse restResponse;
    RequestBody requestBody = RequestBody.create(body, reqMediaType);
    Request request = createRequest(url, headers).post(requestBody).build();
    long then = Instant.now().toEpochMilli();

    try (Response response = client.newCall(request).execute()) {
      restResponse = handleResponse(response);
    }
    catch (IOException e) {
      restResponse = handleException(e, then);
    }
    return restResponse;
  }

  protected final Object put(String url, String body) {
    Map<String, String> header = new HashMap<>();
    return put(url, header, JSON, body);
  }

  protected final Object put(String url, Map<String, String> headers, MediaType reqMediaType, String body) {
    RestResponse restResponse;
    RequestBody requestBody = RequestBody.create(body, reqMediaType);
    Request request = createRequest(url, headers).put(requestBody).build();
    long then = Instant.now().toEpochMilli();

    try (Response response = client.newCall(request).execute()) {
      restResponse = handleResponse(response);
    }
    catch (IOException e) {
      restResponse = handleException(e, then);
    }
    return restResponse;
  }

  protected final Object delete(String url) {
    Map<String, String> header = new HashMap<>();
    return delete(url, header);
  }

  protected final Object delete(String url, Map<String, String> headers) {
    RestResponse restResponse;
    Request request = createRequest(url, headers).delete().build();
    long then = Instant.now().toEpochMilli();

    try (Response response = client.newCall(request).execute()) {
      restResponse = handleResponse(response);
    }
    catch (IOException e) {
      restResponse = handleException(e, then);
    }
    return restResponse;
  }

}
