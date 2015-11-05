package io.vertx.workshop.recommendation.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;
import io.vertx.workshop.recommendation.RecommendationService;

public class RecommendationServiceImpl implements RecommendationService {

  // TODO Change to your own.
  public static final String RECOMMENDATIONS_ADDRESS = "devoxx.recommendations.announce";
  private final RedisClient redis;
  private final EventBus eventBus;

  public RecommendationServiceImpl(Vertx vertx, JsonObject config) {
    this.eventBus = vertx.eventBus();
    this.redis = RedisClient.create(vertx, new RedisOptions(config));
  }

  @Override
  public void vote(String name, boolean plus, Handler<AsyncResult<Void>> handler) {
    redis.hincrby(name, plus ? "up" : "down", 1, hincrby -> {
      // TODO to implement
      // TODO don't forget to publish the new ratings to RECOMMENDATIONS_ADDRESS
    });
  }

  @Override
  public void get(String name, Handler<AsyncResult<JsonObject>> handler) {
    redis.hgetall(name, hgetall -> {
      // TODO to implement
    });
  }

  public void close() {
    redis.close(v -> {
    });
  }
}
