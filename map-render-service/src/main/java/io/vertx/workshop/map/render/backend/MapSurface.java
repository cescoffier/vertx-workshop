/**
 * Copyright 2015 the original author or authors.
 */
package io.vertx.workshop.map.render.backend;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.KEY_COLOR_RENDERING;
import static java.awt.RenderingHints.KEY_INTERPOLATION;
import static java.awt.RenderingHints.KEY_RENDERING;
import static java.awt.RenderingHints.KEY_STROKE_CONTROL;
import static java.awt.RenderingHints.KEY_TEXT_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY;
import static java.awt.RenderingHints.VALUE_INTERPOLATION_BICUBIC;
import static java.awt.RenderingHints.VALUE_RENDER_QUALITY;
import static java.awt.RenderingHints.VALUE_STROKE_NORMALIZE;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_GASP;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import io.vertx.workshop.map.BoundingBox;
import io.vertx.workshop.map.Coordinate;

import javax.imageio.ImageIO;

/**
 * Map Surface is a rendering surface where maps can be rendered.
 *
 * @author Paulo Lopes
 */
public class MapSurface {

  private static final Map<RenderingHints.Key, Object> RENDER_HINTS = new HashMap<>();

  private final Image surface;
  private final Graphics2D graphics;

  // image metadata (immutable)
  public final Coordinate offset;
  public final BoundingBox bounds;

  public final int zoomLevel;
  public final GeneralPath path;

  static {
    RENDER_HINTS.put(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
    RENDER_HINTS.put(KEY_COLOR_RENDERING, VALUE_COLOR_RENDER_QUALITY);
    RENDER_HINTS.put(KEY_INTERPOLATION, VALUE_INTERPOLATION_BICUBIC);
    RENDER_HINTS.put(KEY_RENDERING, VALUE_RENDER_QUALITY);
    RENDER_HINTS.put(KEY_STROKE_CONTROL, VALUE_STROKE_NORMALIZE);
    RENDER_HINTS.put(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_GASP);
  }

  public MapSurface(int width, int height, Color bg, int zoom_level, Coordinate offset, BoundingBox bounds) {
    surface = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
    graphics = (Graphics2D) surface.getGraphics();
    path = new GeneralPath();

    this.zoomLevel = zoom_level;
    this.offset = offset;
    this.bounds = bounds;

    graphics.setBackground(bg);
    graphics.clearRect(0, 0, width, height);

    graphics.setRenderingHints(RENDER_HINTS);
  }

  public Graphics2D getGraphics() {
    return graphics;
  }

  public void write(final String filename) throws IOException {
    ImageIO.write((RenderedImage) surface, "PNG", new FileOutputStream(filename));
  }

  public void write(final OutputStream out) throws IOException {
    ImageIO.write((RenderedImage) surface, "PNG", out);
  }

  public void flush() {
    surface.flush();
  }
}
