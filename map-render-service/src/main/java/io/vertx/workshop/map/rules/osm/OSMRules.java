/**
 * Copyright 2015 the original author or authors.
 */
package io.vertx.workshop.map.rules.osm;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import io.vertx.workshop.map.rules.Rule;
import io.vertx.workshop.map.rules.Draw;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * LinkedList Struct for Rules
 *
 * @author Paulo Lopes
 */
public abstract class OSMRules extends DefaultHandler {

  private int depth;
  private Rule rule;

  // parsing helpers
  private static final Logger LOG = Logger.getLogger(OSMRules.class.getName());
  private static final int MAXSTACK = 20;
  // Pointers to work with
  private Rule currentRule;
  private Rule[] ruleStack;

  public Rule read(InputStream stream) throws SAXException, IOException, ParserConfigurationException {

    depth = -1;
    rule = null;

    long tRead = System.currentTimeMillis();

    // NULL rule stack
    ruleStack = new Rule[MAXSTACK];

    // Create a builder factory
    SAXParserFactory factory = SAXParserFactory.newInstance();
    factory.setValidating(false);
    // Create the builder and parse the file
    factory.newSAXParser().parse(stream, this);
    // free resources
    stream.close();

    LOG.info("OSM Rules parsing done (" + ((System.currentTimeMillis() - tRead) / 1000f) + ") secs");
    ruleStack = null;
    return rule;
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    // Parsing Rules
    if ("rules".equals(qName)) {
      // Init Ruleset
      rule = null;
      String bg = attributes.getValue("background");
      if (bg.length() > 7) {
        setBgColor(new Color(
            Integer.parseInt(bg.substring(1, 3), 16),
            Integer.parseInt(bg.substring(3, 5), 16),
            Integer.parseInt(bg.substring(5, 7), 16),
            Integer.parseInt(bg.substring(7, 9), 16)));
      } else {
        setBgColor(new Color(
            Integer.parseInt(bg.substring(1, 3), 16),
            Integer.parseInt(bg.substring(3, 5), 16),
            Integer.parseInt(bg.substring(5, 7), 16), 255));
      }
    }
    // Parsing Rule
    else if ("rule".equals(qName)) {
      depth++;

      // Create Rule
      int type = Rule.UNKNOWN;

      // Populate Rule
      String e = attributes.getValue("e");
      if (e != null && e.contains("way")) {
        type |= Rule.WAY;
      }
      if (e != null && e.contains("node")) {
        type |= Rule.NODE;
      }

      String[] key = null, value = null;

      String k = attributes.getValue("k");
      if (k != null) {
        key = k.split("\\|");
      }
      String v = attributes.getValue("v");
      if (v != null) {
        value = v.split("\\|");
      }
      if (key != null && value != null) {
        Rule new_rule = new Rule(type, key, value);

        // Insert Rule to chain
        if (rule == null)
          rule = new_rule;
        else
          currentRule.addRule(new_rule);
        currentRule = new_rule;

        // Adding to stack
        ruleStack[depth] = currentRule;
      }
    }
    // Parsing Else
    else if ("else".equals(qName)) {
      // the else rule is *always* after a rule, this means that the
      // current stack position
      // should contain the previous rule
      // Create Rule
      Rule new_rule = new Rule(
          ruleStack[depth].type,
          ruleStack[depth].keys,
          ruleStack[depth].values,
          true);

      depth++;

      // Insert Rule to chain (rule cannot be null)
      currentRule.addRule(new_rule);
      currentRule = new_rule;

      // Adding to stack
      ruleStack[depth] = currentRule;
    }
    // Parsing Polygone, etc.
    else if ("polygone".equals(qName) || "line".equals(qName) || "text".equals(qName)) {
      // Create Draw
      int type = Draw.UNKNOWN;

      // Populate Draw
      if ("polygone".equals(qName))
        type = Draw.POLYGONE;
      else if ("line".equals(qName))
        type = Draw.LINE;
      else if ("text".equals(qName))
        type = Draw.TEXT;

      Draw draw = new Draw(type);

      String color = attributes.getValue("color");
      if (color != null) {
        draw.setColor(new Color(
            Integer.parseInt(color.substring(1, 3), 16),
            Integer.parseInt(color.substring(3, 5), 16),
            Integer.parseInt(color.substring(5, 7), 16)));
      }
      String width = attributes.getValue("width");
      if (width != null) {
        draw.setWidth(Float.parseFloat(width));
      }
      draw.setPattern(attributes.getValue("pattern"));
      String zoom = attributes.getValue("zoom");
      if (zoom != null) {
        String[] zooms = zoom.split(":");
        draw.setZoom(Integer.parseInt(zooms[0]), Integer.parseInt(zooms[1]));
      }

      // Insert Draw
      ruleStack[depth].appendDraw(draw);
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    if ("rule".equals(qName)) {
      // Fetching Parent from stack
      if (depth > 0) {
        ruleStack[depth].setParent(ruleStack[depth - 1]);
      }

      depth--;
    } else if ("else".equals(qName)) {
      // Fetching Parent from stack
      if (depth > 0) {
        ruleStack[depth].setParent(ruleStack[depth - 1]);
      }

      depth--;
    }
  }

  protected abstract void setBgColor(Color c);
}
