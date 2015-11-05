/**
 * Copyright 2015 the original author or authors.
 */
package io.vertx.workshop.map.index;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import io.vertx.workshop.map.BoundingBox;

/**
 * QuadTree implementation.
 *
 * @author Paulo Lopes
 * @see <a href="https://en.wikipedia.org/wiki/Quadtree">Quadtree</a>.
 *
 * @param <T> a type extending BoundingBox
 */
public class QTree<T extends BoundingBox> implements Serializable {

  private static final long serialVersionUID = 1L;

  private final BoundingBox bb;
  private final int treeDepth;
  private final QTree<T> nw;
  private final QTree<T> ne;
  private final QTree<T> sw;
  private final QTree<T> se;
  private List<T> elements;

  /**
   * Create a QuadTree partition using a max depth level of 6.
   *
   * @param bbox the space to partition.
   */
  public QTree(BoundingBox bbox) {
    this(6, bbox);
  }

  /**
   * Create a QuadTree with a specific level of depth. Note that the implementation is not Lazy, meaning that all quads
   * are instantiated at the time of construction. Having this knowledge in mind be aware choosing a high level of
   * depth will have the side effect of using much more memory and high levels of recursion.
   *
   * @param maxTreeDepth the max allowed sub partition level
   * @param bbox the space to partition
   */
  public QTree(int maxTreeDepth, BoundingBox bbox) {
    bb = bbox;
    treeDepth = maxTreeDepth;

    if (treeDepth > 1) {
      nw = new QTree<>(treeDepth - 1, bb.getNWQuadrant());
      ne = new QTree<>(treeDepth - 1, bb.getNEQuadrant());
      sw = new QTree<>(treeDepth - 1, bb.getSWQuadrant());
      se = new QTree<>(treeDepth - 1, bb.getSEQuadrant());
    } else {
      nw = null;
      ne = null;
      sw = null;
      se = null;
    }
  }

  /**
   * Add an element to the tree.
   *
   * @param t the element
   */
  public void add(T t) {
    if (treeDepth == 1) {
      if (t.intersects(bb)) {
        if (elements == null) {
          elements = new LinkedList<>();
        }
        elements.add(t);
      } else {
        throw new UnsupportedOperationException("Element outside BoundingBox: " + t);
      }
      return;
    }

    boolean bNW = t.intersects(nw.getBoundingBox());
    boolean bNE = t.intersects(ne.getBoundingBox());
    boolean bSW = t.intersects(sw.getBoundingBox());
    boolean bSE = t.intersects(se.getBoundingBox());

    int nCount = 0;

    if (bNW) {
      nCount++;
    }
    if (bNE) {
      nCount++;
    }
    if (bSW) {
      nCount++;
    }
    if (bSE) {
      nCount++;
    }

    if (nCount > 1) {
      if (elements == null) {
        elements = new LinkedList<>();
      }
      elements.add(t);
      return;
    }
    if (nCount == 0) {
      throw new UnsupportedOperationException("Element outside BoundingBox: " + t);
    }

    if (bNW) {
      nw.add(t);
    }
    if (bNE) {
      ne.add(t);
    }
    if (bSW) {
      sw.add(t);
    }
    if (bSE) {
      se.add(t);
    }
  }

  /**
   * Remove a element from the tree.
   *
   * @param t the element
   * @return true if the element existed before in the tree
   */
  public boolean remove(T t) {
    if (elements != null && elements.remove(t)) {
      return true;
    }

    if (treeDepth > 1) {
      if (nw.remove(t)) {
        return true;
      }

      if (ne.remove(t)) {
        return true;
      }

      if (sw.remove(t)) {
        return true;
      }

      if (se.remove(t)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Clear all data from the tree.
   */
  public void clear() {
    if (elements != null) elements.clear();

    if (treeDepth > 1) {
      nw.clear();
      ne.clear();
      sw.clear();
      se.clear();
    }
  }

  /**
   * Accessor for the max level for this tree.
   *
   * @return the level.
   */
  public int getMaxTreeDepth() {
    return treeDepth;
  }

  public List<T> getAll() {
    List<T> l = new LinkedList<>();
    if (elements != null) {
      l.addAll(elements);
    }

    if (treeDepth > 1) {
      l.addAll(nw.getAll());
      l.addAll(ne.getAll());
      l.addAll(sw.getAll());
      l.addAll(se.getAll());
    }

    return l;
  }

  /**
   * Get all elements in the tree that are contained in the given bounding box. The test for inclusion is intersect,
   * this means that if just 1 point of a element falls inside the given bounding box it the element will be returned.
   *
   * @param bbox the filtering window
   * @return all elements that intersect the window
   */
  public List<T> get(BoundingBox bbox) {
    List<T> v = new LinkedList<>();

    if (bb.intersects(bbox)) {
      if (elements != null) {
        for (T t : elements) {
          if (t.intersects(bbox)) {
            v.add(t);
          }
        }
      }
      if (treeDepth > 1) {
        v.addAll(nw.get(bbox));
        v.addAll(ne.get(bbox));
        v.addAll(sw.get(bbox));
        v.addAll(se.get(bbox));
      }
    }

    return v;
  }

  /**
   * Accessor for the bounding box.
   *
   * @return the bounding box.
   */
  public BoundingBox getBoundingBox() {
    return bb;
  }
}
