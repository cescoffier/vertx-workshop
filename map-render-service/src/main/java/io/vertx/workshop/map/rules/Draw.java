/**
 * Copyright 2015 the original author or authors.
 */
package io.vertx.workshop.map.rules;

import java.awt.Color;

/**
 * LinkedList Struct for Draws
 *
 * @author Paulo Lopes
 */
public class Draw {

  public static final int UNKNOWN = 0;
  public static final int LINE = 1;
  public static final int POLYGONE = 2;
  public static final int TEXT = 3;

  public final int type;

  private int minzoom;
  private int maxzoom;
  private Color color;
  private String pattern;
  private float width;

  Draw next;

  public Draw(int type) {
    this.type = type;
    maxzoom = 99;
  }

  public void setColor(Color c) {
    this.color = c;
  }

  public Color getColor() {
    return color;
  }

  public void setWidth(float w) {
    this.width = w;
  }

  public float getWidth() {
    return width;
  }

  public void setPattern(String pattern) {
    this.pattern = pattern;
  }

  public void setZoom(int min, int max) {
    this.minzoom = min;
    this.maxzoom = max;
  }

  public int getMinZoom() {
    return minzoom;
  }

  public int getMaxZoom() {
    return maxzoom;
  }

  public Draw next() {
    return next;
  }

  public String getPattern() {
    return pattern;
  }
}
