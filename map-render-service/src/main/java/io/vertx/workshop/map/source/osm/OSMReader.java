/**
 * Copyright 2015 the original author or authors.
 */
package io.vertx.workshop.map.source.osm;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import io.vertx.workshop.map.source.Node;
import io.vertx.workshop.map.source.Way;
import io.vertx.workshop.map.MapException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import io.vertx.workshop.map.BoundingBox;

/**
 * OSM XML Parser for the OSM format
 *
 * @author Paulo Lopes
 */
public abstract class OSMReader extends DefaultHandler {
	
	Map<Long, Node> nodeidx = new HashMap<>();

	private Node cNode = null;
	private Way cWay = null;
	
	private static final Logger LOG = Logger.getLogger(OSMReader.class.getName());
	
	public void load(InputStream stream) throws MapException {
		try {
			// Create a builder factory
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setValidating(false);
			// Create the builder and parse the file
			factory.newSAXParser().parse(stream, this);
		
			nodeidx.clear();
			nodeidx = null;
		} catch(SAXException | IOException | ParserConfigurationException e) {
			throw new MapException(e);
		}
	}
	
	@Override
	@SuppressWarnings("boxing")
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		// Parsing Bounds
		if ("bounds".equals(qName)) {
//			LOG.fine("Parsing bounds");
			initIndex(new BoundingBox(
					Double.parseDouble(attributes.getValue("minlat")),
					Double.parseDouble(attributes.getValue("minlon")),
					Double.parseDouble(attributes.getValue("maxlat")),
					Double.parseDouble(attributes.getValue("maxlon"))));
		}
		// Parsing bound (OSM 0.6) using osmosis
		if ("bound".equals(qName)) {
			String[] box = attributes.getValue("box").split(",");
			initIndex(new BoundingBox(
					Double.parseDouble(box[0]),
					Double.parseDouble(box[1]),
					Double.parseDouble(box[2]),
					Double.parseDouble(box[3])));
		}
		// Parsing Node
		else if ("node".equals(qName)) {
			long id = Long.parseLong(attributes.getValue("id"));
			cNode = new Node(
					Double.parseDouble(attributes.getValue("lat")),
					Double.parseDouble(attributes.getValue("lon")));

			// Insert Node local hash
			nodeidx.put(id, cNode);
		}
		// Parsing Tags
		else if ("tag".equals(qName)) {

			if (cNode == null && cWay == null) // End if there is nothing to add the tag to
				return;

			String k, v;

			k = attributes.getValue("k").intern();
			v = attributes.getValue("v").intern();
			// attributes.getValue("created_by");
			// attributes.getValue("source");
			if ("layer".equals(k)) {
				int layer;
				try {
					if(v.charAt(0) == '+') v = v.substring(1);
					layer = Integer.parseInt(v);
				} catch(NumberFormatException nfe) {
					LOG.severe("Not a number: " + v);
					layer = 1;
				}
				if (cNode != null) {
					cNode.setLayer(layer);
				} else {
					cWay.setLayer(layer);
				}
			} else if ("name".equals(k)) {
				if (cWay != null) {
					cWay.setName(v);
				}
			}

			if (cNode != null)
				cNode.insertTag(k, v);
			else if (cWay != null)
				cWay.insertTag(k, v);
		}
		// Parsing Way
		else if ("way".equals(qName)) {
			cWay = new Way();
		}
		// Parsing WayNode
		else if ("nd".equals(qName)) {
			long ref = Long.parseLong(attributes.getValue("ref"));

			if (ref != 0) {
				Node n;

				n = nodeidx.get(ref);
				if (n == null) {
					LOG.severe("No node with reference " + ref + " found!");
					return;
				}

				// Insert WayNode
				cWay.addWayNode(n);
				cNode = null;
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if ("node".equals(qName)) {
			if(cNode != null) {
				indexNode(cNode);
			}
			cNode = null;
		} else if ("way".equals(qName)) {
			if(cWay != null) {
				indexWay(cWay);
			}
			cWay = null;
		}
	}
	
	public abstract void initIndex(BoundingBox bbox);
	
	public abstract void indexWay(Way w);
	
	public abstract void indexNode(Node n);
}
