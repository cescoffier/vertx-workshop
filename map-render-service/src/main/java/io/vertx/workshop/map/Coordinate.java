/**
 * Copyright 2015 the original author or authors.
 */
package io.vertx.workshop.map;

import java.io.Serializable;

/**
 * Implement a coordinate
 * 
 * @see <a href="https://en.wikipedia.org/wiki/Coordinate_system">Coordinate System</a>
 * @author Paulo Lopes
 */
public class Coordinate implements Serializable {

  private static final long serialVersionUID = 1L;

  public final double x;
  public final double y;

  public Coordinate(final double x, final double y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public String toString() {
    return "<" + x + ":" + y + ">";
  }
}
