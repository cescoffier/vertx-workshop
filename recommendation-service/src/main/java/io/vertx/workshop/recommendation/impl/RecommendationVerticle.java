package io.vertx.workshop.recommendation.impl;

import io.vertx.core.AbstractVerticle;
import io.vertx.serviceproxy.ProxyHelper;
import io.vertx.workshop.recommendation.RecommendationService;

public class RecommendationVerticle extends AbstractVerticle {

  private RecommendationServiceImpl service;

  @Override
  public void start() throws Exception {
    service = new RecommendationServiceImpl(vertx, config());
    //TODO Change address to your own.
    ProxyHelper.registerService(RecommendationService.class, vertx, service, "devoxx.recommendations");
  }

  @Override
  public void stop() throws Exception {
    service.close();
  }
}
