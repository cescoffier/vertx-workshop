/**
 * Copyright 2015 the original author or authors.
 */
package io.vertx.workshop.map.source;

import java.io.Serializable;
import java.util.Map;

/**
 * OSM Relation
 *
 * @author Paulo Lopes
 */
class Relation implements Serializable {

	private static final long serialVersionUID = 1L;

	int id;
	Map<String, String> tags;
	Member member;
}
