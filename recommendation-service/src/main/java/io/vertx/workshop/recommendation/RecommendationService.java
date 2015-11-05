package io.vertx.workshop.recommendation;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ProxyHelper;

/**
 * A service to recommend places.
 */
@VertxGen
@ProxyGen
public interface RecommendationService {

  /**
   * The method used to create proxy to consume the service.
   *
   * @param vertx   vert.x
   * @param address the address where the service is served
   * @return the proxy
   */
  static RecommendationService createProxy(Vertx vertx, String address) {
    return ProxyHelper.createProxy(RecommendationService.class, vertx, address);
  }

  /**
   * Votes for a place.
   * When a vote is placed, the new rating of the place are brodcasted to the event bus.
   *
   * @param name          the name of the place
   * @param plus          whether or not you liked the place.
   * @param resultHandler invoked when the action is completed
   */
  void vote(String name, boolean plus, Handler<AsyncResult<Void>> resultHandler);

  /**
   * Gets the number or votes.
   * @param name the name of the place
   * @param resultHandler the result handler. The votes are given in a JSON object using the "up" and "down" fields.
   */
  void get(String name, Handler<AsyncResult<JsonObject>> resultHandler);
}
