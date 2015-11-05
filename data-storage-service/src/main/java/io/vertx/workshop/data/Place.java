package io.vertx.workshop.data;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

import java.util.*;

/**
 * The place structure.
 *
 * It's a data object that can be serialized to JSON (and built from JSON). Data objects can be used in event bus
 * proxies.
 */
@DataObject(generateConverter = true)
public class Place {

  private String name;

  private String description;

  private String category;

  private Set<String> tags = new TreeSet<>();

  private double longitude;

  private double latitude;

  private String address;

  public Place() {
    // Empty constructor
  }

  public Place(Place other) {
    this.name = other.name;
    this.description = other.description;
    this.category = other.category;
    this.tags = other.tags;
    this.longitude = other.longitude;
    this.latitude = other.latitude;
    this.address = other.address;
  }

  public Place(JsonObject json) {
    PlaceConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    PlaceConverter.toJson(this, json);
    return json;
  }

  public String getAddress() {
    return address;
  }

  public Place setAddress(String address) {
    this.address = address;
    return this;
  }

  public String getCategory() {
    return category;
  }

  public Place setCategory(String category) {
    this.category = category;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public Place setDescription(String description) {
    this.description = description;
    return this;
  }

  public double getLatitude() {
    return latitude;
  }

  public Place setLatitude(double latitude) {
    this.latitude = latitude;
    return this;
  }

  public double getLongitude() {
    return longitude;
  }

  public Place setLongitude(double longitude) {
    this.longitude = longitude;
    return this;
  }

  public String getName() {
    return name;
  }

  public Place setName(String name) {
    this.name = name;
    return this;
  }

  public Set<String> getTags() {
    return tags;
  }

  public Place setTags(List<String> tags) {
    this.tags = new TreeSet<>(tags);
    return this;
  }

  public Place addTag(String tag) {
    tags.add(tag);
    return this;
  }

  public Place removeTag(String tag) {
    tags.remove(tag);
    return this;
  }
}
