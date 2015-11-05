package io.vertx.workshop.data;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.serviceproxy.ProxyHelper;

import java.util.List;

/**
 * Service exposed on the event bus to provide access to
 * the stored Places.
 */
@VertxGen
@ProxyGen
public interface DataStorageService {

  /**
   * Method called to create a proxy (to consume the service).
   *
   * @param vertx   vert.x
   * @param address the address on the vent bus where the service is served.
   * @return the proxy on the {@link DataStorageService}
   */
  static DataStorageService createProxy(Vertx vertx, String address) {
    return ProxyHelper.createProxy(DataStorageService.class, vertx, address);
  }

  /**
   * Retrieves all places.
   *
   * @param resultHandler the result handler
   */
  void getAllPlaces(Handler<AsyncResult<List<Place>>> resultHandler);

  /**
   * Retrieves all places belonging to the given category.
   *
   * @param category      the category
   * @param resultHandler the result handler
   */
  void getPlacesForCategory(String category, Handler<AsyncResult<List<Place>>> resultHandler);

  /**
   * Retrieves all places belonging with the given tag.
   *
   * @param tag           the tag
   * @param resultHandler the result handler
   */
  void getPlacesForTag(String tag, Handler<AsyncResult<List<Place>>> resultHandler);

  /**
   * Adds a place in the data storage service.
   *
   * @param place         the place
   * @param resultHandler handler called when the insertion is done.
   */
  void addPlace(Place place, Handler<AsyncResult<Void>> resultHandler);
}
