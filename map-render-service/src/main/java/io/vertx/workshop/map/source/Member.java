/**
 * Copyright 2015 the original author or authors.
 */
package io.vertx.workshop.map.source;

import java.io.Serializable;

/**
 * OSM Member
 *
 * @author Paulo Lopes
 */
class Member implements Serializable {

  private static final long serialVersionUID = 1L;

  Node node;
  Way way;
  String role;
  Member next;
}
