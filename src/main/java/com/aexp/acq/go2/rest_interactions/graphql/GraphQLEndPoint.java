package com.aexp.acq.go2.rest_interactions.graphql;

import com.aexp.acq.go2.base.App;
import com.aexp.acq.go2.base.GraphInteraction;
import okhttp3.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class GraphQLEndPoint extends GraphInteraction {

  private static final Logger logger = LoggerFactory.getLogger(GraphQLEndPoint.class);
  private static final MediaType JSON = MediaType.get("application/json");
  private static final String target = App.instance().getProperty("github.graphql.base.url");

  public GraphQLEndPoint(String name) {
    super(name);

  }

  @Override
  protected Object process(Object... vargs) {
    String query = (String)vargs[0];

    Map<String, String> header = new HashMap<>();
    return post(target, header, JSON, query);
  }

}
