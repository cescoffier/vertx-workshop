/**
 * Copyright 2015 the original author or authors.
 */
package io.vertx.workshop.map.source;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * OSM Node
 *
 * @author Paulo Lopes
 */
public class Node implements Serializable {

	private static final long serialVersionUID = 1L;

	private final double lat;
	private final double lon;
	private int layer;

	private Map<String, String> tags;

	public Node(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public void insertTag(String key, String value) {
		
		if(tags == null) tags = new HashMap<>();
		tags.put(key, value);
	}
}
