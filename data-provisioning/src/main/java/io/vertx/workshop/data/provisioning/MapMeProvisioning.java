package io.vertx.workshop.data.provisioning;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.workshop.data.Place;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class MapMeProvisioning extends AbstractVerticle {

  private String mongoURL;

  @Override
  public void start() {
    mongoURL = config().getString("mongoURL");
    provision();
  }


  private void provision() {
    HttpClient client = vertx.createHttpClient();
    //URL for devoxx.
    client.getNow(80, "mapme.com", "/api/map/910f1efe-d403-481e-a87e-bd8c9df7a131/places", response ->
        response.bodyHandler(body -> {
              JsonObject json =
                  new JsonObject(body
                      .toString("utf-8"));
          extractPlaces(json,
              this::populateDatabase);
            }
        )
    );
  }

  private void populateDatabase(Collection<Place> places) {
    MongoClient mongo = MongoClient.createShared(vertx,
        new JsonObject().put("db_name", "places").put("connection_string", mongoURL),
        "places");

    places.stream().forEach(place ->
        mongo.insert("places", place.toJson(), result -> {
          if (result.failed()) {
            System.err.println("I was not able to insert '" + place.getName() + "' : " + result.cause().getMessage());
          } else {
            System.out.println("Place '" + place.getName() + "' inserted");
          }
        }));
  }

  private void extractPlaces(JsonObject json, Handler<Collection<Place>> resultHandler) {
    Map<String, Place> placesByName = new HashMap<>();
    JsonObject categories = json.getJsonObject("categories");
    categories.stream().forEach(entry -> {
      String category = entry.getKey();
      JsonObject cat = (JsonObject) entry.getValue();
      JsonObject tags = cat.getJsonObject("tags");
      tags.stream().forEach(tagEntry -> {
        String tag = tagEntry.getKey();
        JsonArray places = ((JsonObject) tagEntry.getValue()).getJsonArray("places");
        places.stream().forEach(p -> {
          Place place = createPlaceFromMapMe((JsonObject) p, tag, category);
          if (placesByName.containsKey(place.getName())) {
            placesByName.get(place.getName()).addTag(tag);
          } else {
            placesByName.put(place.getName(), place);
          }
        });
      });
    });

    resultHandler.handle(placesByName.values());
  }

  private Place createPlaceFromMapMe(JsonObject json, String tag, String category) {
    Place place = new Place();
    return place
        .setAddress(json.getString("addressDisplay", ""))
        .setCategory(category)
        .setDescription(json.getString("description", ""))
        .setLatitude(json.getDouble("lat", -1.0))
        .setLongitude(json.getDouble("lon", -1.0))
        .setName(json.getString("companyName"))
        .setTags(Collections.singletonList(tag));
  }
}
