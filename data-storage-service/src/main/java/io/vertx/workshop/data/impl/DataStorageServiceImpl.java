package io.vertx.workshop.data.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.workshop.data.DataStorageService;
import io.vertx.workshop.data.Place;
import io.vertx.ext.mongo.MongoClient;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link DataStorageService}.
 */
public class DataStorageServiceImpl implements DataStorageService {

  public static final String COLLECTION = "places";
  public static final Logger LOGGER = Logger.getLogger("Data Storage Service");

  private final MongoClient mongo;
  private final Vertx vertx;

  public DataStorageServiceImpl(Vertx vertx, JsonObject config) {
    this.mongo = MongoClient.createShared(vertx, config, "places");
    this.vertx = vertx;
    LOGGER.info("Data Storage Service instantiated");
  }

  @Override
  public void getAllPlaces(Handler<AsyncResult<List<Place>>> resultHandler) {
    LOGGER.info("Retrieving all the places");
    long begin = System.currentTimeMillis();
    mongo.find(COLLECTION,
        new JsonObject(),
        ar -> {
          if (ar.failed()) {
            resultHandler.handle(Future.failedFuture(ar.cause()));
          } else {
            List<Place> places = ar.result().stream()
                .map(Place::new).collect(Collectors.toList());
            LOGGER.info(places.size() + " places have been retrieved");
            resultHandler.handle(Future.succeededFuture(places));
            report(System.currentTimeMillis() - begin);
          }
        }
    );
  }

  public void getPlacesForCategory(String category,
                                   Handler<AsyncResult<List<Place>>> resultHandler) {
    mongo.find(COLLECTION,
        new JsonObject().put("category", category),
        ar -> {
          if (ar.failed()) {
            resultHandler.handle(Future.failedFuture(ar.cause()));
          } else {
            List<Place> places = ar.result().stream()
                .map(Place::new).collect(Collectors.toList());
            resultHandler.handle(Future.succeededFuture(places));
          }
        }
    );
  }


  @Override
  public void getPlacesForTag(String tag, Handler<AsyncResult<List<Place>>> resultHandler) {
    mongo.find(COLLECTION,
        new JsonObject().put("tag", tag),
        ar -> {
          if (ar.failed()) {
            resultHandler.handle(Future.failedFuture(ar.cause()));
          } else {
            List<Place> places = ar.result().stream()
                .map(Place::new).collect(Collectors.toList());
            resultHandler.handle(Future.succeededFuture(places));
          }
        }
    );
  }

  @Override
  public void addPlace(Place place, Handler<AsyncResult<Void>> resultHandler) {
    mongo.insert(COLLECTION, place.toJson(), s -> {
      if (s.failed()) {
        resultHandler.handle(Future.failedFuture(s.cause()));
      } else {
        resultHandler.handle(Future.succeededFuture());
      }
    });
  }

  public void close() {
    mongo.close();
  }


  private void report(long time) {
    vertx.eventBus().send("metrics", new JsonObject().put("source", "mongo.query").put("value", time));
  }
}
