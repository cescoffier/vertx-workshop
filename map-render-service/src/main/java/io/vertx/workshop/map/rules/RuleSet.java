/**
 * Copyright 2015 the original author or authors.
 */
package io.vertx.workshop.map.rules;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import io.vertx.workshop.map.MapException;
import org.xml.sax.SAXException;

import io.vertx.workshop.map.rules.osm.OSMRules;

/**
 * Defines drawing rules for the renderer. This Object defines drawing rules for a Renderer.
 *
 * @author Paulo Lopes
 */
public class RuleSet extends OSMRules {

  private static final int TAG_CMP_NOT_EQUAL = 0;
  private static final int TAG_CMP_EQUAL = 1;
  private static final int TAG_CMP_ANY = 2;
  private static final int TAG_CMP_MISSING = 3;

  private final Rule root;
  private Color bgColor = Color.WHITE;

  public RuleSet(String filename) throws MapException, FileNotFoundException {
    this(new FileInputStream(filename));
  }

  public RuleSet(InputStream stream) throws MapException {
    try {
      Rule o_root = null;
      Rule curr = null;
      Rule next;

      for (Rule r = read(stream); r != null; ) {
        next = r.next();

        if (r.getDraw() != null) {
          if (o_root == null) {
            o_root = r;
            curr = o_root;
          } else {
            curr.addRule(r);
            curr = r;
          }
        }
        r = next;
      }
      root = o_root;
    } catch (SAXException | ParserConfigurationException | IOException e) {
      throw new MapException(e);
    }
  }

  @Override
  protected void setBgColor(Color bgColor) {
    this.bgColor = bgColor;
  }

  public Color getBgColor() {
    return bgColor;
  }

  public Rule root() {
    return root;
  }

  private static int stringInStrings(String string, String[] strings) {
    for (int i = 0; i < strings.length; i++) {
      if (string.equals(strings[i]))
        return TAG_CMP_EQUAL;
      if ("*".equals(strings[i]))
        return TAG_CMP_ANY;
      if ("~".equals(strings[i]))
        return TAG_CMP_MISSING;
    }
    return TAG_CMP_NOT_EQUAL;
  }

  /**
   * function: matchRule
   * <p>
   * Check if an element matches a rule.
   */
  public static boolean matchRule(Rule rule, Map<String, String> tags) {
    int k, v;

    for (Map.Entry<String, String> t : tags.entrySet()) {
      k = stringInStrings(t.getKey(), rule.keys);
      v = stringInStrings(t.getValue(), rule.values);

      if (k == TAG_CMP_EQUAL && v == TAG_CMP_EQUAL)
        return true;
      if (k == TAG_CMP_EQUAL && v == TAG_CMP_ANY)
        return true;
      if (k == TAG_CMP_NOT_EQUAL && v == TAG_CMP_MISSING)
        return true;
    }
    return false;
  }

  /**
   * function: checkRule
   * <p>
   * Check if an element matches to a rule and to all it's parents.
   */
  public static boolean checkRule(Rule rule, Map<String, String> tags) {

    for (Rule r = rule.getParent(); r != null; r = r.getParent()) {
      if (matchRule(r, tags) == r.negate) {
        return false;
      }
    }

    return matchRule(rule, tags);

  }
}
