/**
 * Copyright 2015 the original author or authors.
 */
package io.vertx.workshop.map;

import java.io.Serializable;

/**
 * BoundingBox implementation.
 *
 * @see <a href="https://en.wikipedia.org/w/index.php?title=Minimum_bounding_box">Minimum bounding box</a>
 * @author Paulo Lopes
 */
public class BoundingBox implements Serializable {

  private static final long serialVersionUID = 1L;

  private double minLat;
  private double minLon;
  private double maxLat;
  private double maxLon;

  /**
   * Create a new Bounding Box filled with 0s
   */
  public BoundingBox() {
    // default Bounding box
  }

  /**
   * Create a Bounding box with the given coordinates.
   *
   * @param minlat min latitude
   * @param minlon min longitude
   * @param maxlat max latitude
   * @param maxlon max longitude
   */
  public BoundingBox(double minlat, double minlon, double maxlat, double maxlon) {
    this.minLat = minlat;
    this.minLon = minlon;
    this.maxLat = maxlat;
    this.maxLon = maxlon;
  }

  public double getMinLat() {
    return minLat;
  }

  public void setMinLat(double minLat) {
    this.minLat = minLat;
  }

  public double getMinLon() {
    return minLon;
  }

  public void setMinLon(double minLon) {
    this.minLon = minLon;
  }

  public double getMaxLat() {
    return maxLat;
  }

  public void setMaxLat(double maxLat) {
    this.maxLat = maxLat;
  }

  public double getMaxLon() {
    return maxLon;
  }

  public void setMaxLon(double maxLon) {
    this.maxLon = maxLon;
  }

  public double getNorth() {
    return maxLat;
  }

  public double getSouth() {
    return minLat;
  }

  public double getWest() {
    return minLon;
  }

  public double getEast() {
    return maxLon;
  }

  public void setNorth(double north) {
    maxLat = north;
  }

  public void setSouth(double south) {
    minLat = south;
  }

  public void setWest(double west) {
    minLon = west;
  }

  public void setEast(double east) {
    maxLon = east;
  }

  public boolean intersects(BoundingBox b) {
    return
        (minLat <= b.maxLat) &&
            (maxLat >= b.minLat) &&
            (minLon <= b.maxLon) &&
            (maxLon >= b.minLon);
  }

  @Override
  public String toString() {
    return "{" + minLat + ":" + minLon + "," + maxLat + ":" + maxLon + "}";
  }

  public BoundingBox getNWQuadrant() {
    double hLatDiff = (maxLat - minLat) / 2.0;
    double hLonDiff = (maxLon - minLon) / 2.0;

    return new BoundingBox(minLat, minLon, minLat + hLatDiff, minLon + hLonDiff);
  }

  public BoundingBox getNEQuadrant() {
    double hLatDiff = (maxLat - minLat) / 2.0;
    double hLonDiff = (maxLon - minLon) / 2.0;

    return new BoundingBox(minLat, minLon + hLonDiff, minLat + hLatDiff, maxLon);
  }

  public BoundingBox getSWQuadrant() {
    double hLatDiff = (maxLat - minLat) / 2.0;
    double hLonDiff = (maxLon - minLon) / 2.0;

    return new BoundingBox(minLat + hLatDiff, minLon, maxLat, minLon + hLonDiff);
  }

  public BoundingBox getSEQuadrant() {
    double hLatDiff = (maxLat - minLat) / 2.0;
    double hLonDiff = (maxLon - minLon) / 2.0;

    return new BoundingBox(minLat + hLatDiff, minLon + hLonDiff, maxLat, maxLon);
  }
}
