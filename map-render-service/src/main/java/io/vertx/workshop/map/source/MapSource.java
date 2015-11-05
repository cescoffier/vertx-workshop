/**
 * Copyright 2015 the original author or authors.
 */
package io.vertx.workshop.map.source;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import io.vertx.workshop.map.BoundingBox;
import io.vertx.workshop.map.MapException;
import io.vertx.workshop.map.index.QTree;
import io.vertx.workshop.map.source.osm.OSMReader;

/**
 * Map Source is a class capable of loading some source map data into a structure we can use to render.
 *
 * @author Paulo Lopes
 */
public class MapSource extends OSMReader {

  private static final Logger LOG = Logger.getLogger(MapSource.class.getName());

  private QTree<Way> wayIndex;

  @SuppressWarnings("unchecked")
  public MapSource(String filename) throws MapException, ClassNotFoundException, IOException {
    long tRead = System.currentTimeMillis();
    File fIndex = new File(filename + ".idx.gz");
    if (fIndex.exists()) {
      ObjectInputStream in = new ObjectInputStream(new FileInputStream(fIndex));
      wayIndex = (QTree<Way>) in.readObject();
      in.close();
    } else {
      FileInputStream in = new FileInputStream(filename);
      load(in);
      in.close();
      ObjectOutputStream out = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(fIndex)));
      out.writeObject(wayIndex);
      out.close();
    }
    LOG.info("OSM loading done (" + ((System.currentTimeMillis() - tRead) / 1000f) + ") secs");
  }

  @SuppressWarnings("unchecked")
  public MapSource(InputStream in) throws ClassNotFoundException, IOException {
    long tRead = System.currentTimeMillis();
    ObjectInputStream oin = new ObjectInputStream(new GZIPInputStream(in));
    wayIndex = (QTree<Way>) oin.readObject();
    oin.close();
    LOG.info("OSM loading done (" + ((System.currentTimeMillis() - tRead) / 1000f) + ") secs");
  }

  public BoundingBox getBoundingBox() {
    return wayIndex.getBoundingBox();
  }

  public List<Way> getWaysInBoundingBox(BoundingBox bbox) {
    return wayIndex.get(bbox);
  }

  public List<Node> getNodesInBoundingBox(BoundingBox bbox) {
    throw new RuntimeException("Not implemented!");
  }

  @Override
  public void initIndex(BoundingBox bbox) {
    wayIndex = new QTree<>(bbox);
  }

  @Override
  public void indexWay(Way w) {
    wayIndex.add(w);
  }

  @Override
  public void indexNode(Node n) {

  }

//  public static void main(String[] args) throws Exception {
//    new MapSource("map-render/map-data/Antwerpen.osm");
//  }
}
