/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.vertx.workshop.groovy.data;
import groovy.transform.CompileStatic
import io.vertx.lang.groovy.InternalHelper
import io.vertx.core.json.JsonObject
import java.util.List
import io.vertx.workshop.data.Place
import io.vertx.groovy.core.Vertx
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
/**
 * Service exposed on the event bus to provide access to
 * the stored Places.
*/
@CompileStatic
public class DataStorageService {
  private final def io.vertx.workshop.data.DataStorageService delegate;
  public DataStorageService(Object delegate) {
    this.delegate = (io.vertx.workshop.data.DataStorageService) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * Method called to create a proxy (to consume the service).
   * @param vertx vert.x
   * @param address the address on the vent bus where the service is served.
   * @return the proxy on the {@link io.vertx.workshop.groovy.data.DataStorageService}
   */
  public static DataStorageService createProxy(Vertx vertx, String address) {
    def ret= InternalHelper.safeCreate(io.vertx.workshop.data.DataStorageService.createProxy((io.vertx.core.Vertx)vertx.getDelegate(), address), io.vertx.workshop.groovy.data.DataStorageService.class);
    return ret;
  }
  /**
   * Retrieves all places.
   * @param resultHandler the result handler
   */
  public void getAllPlaces(Handler<AsyncResult<List<Map<String, Object>>>> resultHandler) {
    this.delegate.getAllPlaces(new Handler<AsyncResult<List<Place>>>() {
      public void handle(AsyncResult<List<Place>> event) {
        AsyncResult<List<Map<String, Object>>> f
        if (event.succeeded()) {
          f = InternalHelper.<List<Map<String, Object>>>result(event.result().collect({
            io.vertx.workshop.data.Place element ->
            (Map<String, Object>)InternalHelper.wrapObject(element?.toJson())
          }) as List)
        } else {
          f = InternalHelper.<List<Map<String, Object>>>failure(event.cause())
        }
        resultHandler.handle(f)
      }
    });
  }
  /**
   * Retrieves all places belonging to the given category.
   * @param category the category
   * @param resultHandler the result handler
   */
  public void getPlacesForCategory(String category, Handler<AsyncResult<List<Map<String, Object>>>> resultHandler) {
    this.delegate.getPlacesForCategory(category, new Handler<AsyncResult<List<Place>>>() {
      public void handle(AsyncResult<List<Place>> event) {
        AsyncResult<List<Map<String, Object>>> f
        if (event.succeeded()) {
          f = InternalHelper.<List<Map<String, Object>>>result(event.result().collect({
            io.vertx.workshop.data.Place element ->
            (Map<String, Object>)InternalHelper.wrapObject(element?.toJson())
          }) as List)
        } else {
          f = InternalHelper.<List<Map<String, Object>>>failure(event.cause())
        }
        resultHandler.handle(f)
      }
    });
  }
  /**
   * Retrieves all places belonging with the given tag.
   * @param tag the tag
   * @param resultHandler the result handler
   */
  public void getPlacesForTag(String tag, Handler<AsyncResult<List<Map<String, Object>>>> resultHandler) {
    this.delegate.getPlacesForTag(tag, new Handler<AsyncResult<List<Place>>>() {
      public void handle(AsyncResult<List<Place>> event) {
        AsyncResult<List<Map<String, Object>>> f
        if (event.succeeded()) {
          f = InternalHelper.<List<Map<String, Object>>>result(event.result().collect({
            io.vertx.workshop.data.Place element ->
            (Map<String, Object>)InternalHelper.wrapObject(element?.toJson())
          }) as List)
        } else {
          f = InternalHelper.<List<Map<String, Object>>>failure(event.cause())
        }
        resultHandler.handle(f)
      }
    });
  }
  /**
   * Adds a place in the data storage service.
   * @param place the place (see <a href="../../../../../../../cheatsheet/Place.html">Place</a>)
   * @param resultHandler handler called when the insertion is done.
   */
  public void addPlace(Map<String, Object> place = [:], Handler<AsyncResult<Void>> resultHandler) {
    this.delegate.addPlace(place != null ? new io.vertx.workshop.data.Place(new io.vertx.core.json.JsonObject(place)) : null, resultHandler);
  }
}
