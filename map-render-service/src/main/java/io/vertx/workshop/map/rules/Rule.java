/**
 * Copyright 2015 the original author or authors.
 */
package io.vertx.workshop.map.rules;

/**
 * Defines a drawing rule for the #RuleSet.
 *
 * @author Paulo Lopes
 * @since 1.0
 */
public class Rule {

  public static final int UNKNOWN = 0;
  public static final int WAY = 1;
  public static final int NODE = 2;
  public static final int RELATION = 4;

  /* an array of key strings */
  public final String[] keys;
  /* an array of value strings */
  public final String[] values;
  /* the type of the rule */
  public final int type;
  /* the mode of the rule */
  public final boolean negate;

  /* Next terminal rule */
  private Rule next;

  private Rule parent;

  private Draw draw;

  /**
   * Creates a new Rule
   *
   * @param type  type of the rule
   * @param key   the key of the rule
   * @param value the value of the rule
   */
  public Rule(int type, String[] key, String[] value) {
    this(type, key, value, false);
  }

  /**
   * Create a new Rule
   *
   * @param type   type of the rule
   * @param key    the key of the rule
   * @param value  the value of the rule
   * @param negate true for else rules, false for normal rules
   */
  public Rule(int type, String[] key, String[] value, boolean negate) {
    this.type = type;
    this.keys = new String[key.length];
    for (int i = 0; i < keys.length; i++) {
      keys[i] = key[i].intern();
    }
    this.values = new String[value.length];
    for (int i = 0; i < values.length; i++) {
      values[i] = value[i].intern();
    }
    this.negate = negate;
  }

  public void addRule(Rule rule) {
    this.next = rule;
  }

  public void setParent(Rule parent) {
    this.parent = parent;
  }

  public Rule getParent() {
    return parent;
  }

  public void appendDraw(Draw draw) {
    Draw curr = this.draw;
    Draw prev = null;

    while (curr != null) {
      prev = curr;
      curr = curr.next;
    }
    if (prev != null)
      prev.next = draw;
    else
      this.draw = draw;

    draw.next = null;
  }

  public Draw getDraw() {
    return draw;
  }

  public Rule next() {
    return next;
  }
}
