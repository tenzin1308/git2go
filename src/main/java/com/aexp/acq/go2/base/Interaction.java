package com.aexp.acq.go2.base;

import okhttp3.OkHttpClient;

public abstract class Interaction {

  private static final OkHttpClient client = new OkHttpClient();
  private static final String token = System.getenv("");

  public Interaction() {
    if(token == null) {
      throw new IllegalStateException("GitHub Token not provided");
    }
  }


}
