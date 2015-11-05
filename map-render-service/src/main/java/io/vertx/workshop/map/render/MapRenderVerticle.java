/**
 * Copyright 2015 the original author or authors.
 */
package io.vertx.workshop.map.render;

import io.vertx.workshop.map.render.backend.Renderer;
import io.vertx.workshop.map.rules.RuleSet;
import io.vertx.workshop.map.source.MapSource;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Map Tile Render Micro-Service
 * @author Paulo Lopes
 */
public class MapRenderVerticle extends AbstractVerticle {

  // ThreadSafe map renderer
  private static final Renderer renderer;

  static {
    try {

      final String rule = System.getProperty("rule", "rule.xml");
      final String index = System.getProperty("index", "antwerpen.osm.idx.gz");

      RuleSet ruleset = new RuleSet(MapRenderVerticle.class.getClassLoader().getResourceAsStream(rule));
      MapSource map = new MapSource(MapRenderVerticle.class.getClassLoader().getResourceAsStream(index));
      renderer = new Renderer(ruleset, map);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void start() {

    vertx.eventBus().consumer("map-render", msg -> {
      try {
        final MultiMap headers = msg.headers();

        int z = Integer.parseInt(headers.get("z"));
        int x = Integer.parseInt(headers.get("x"));
        int y = Integer.parseInt(headers.get("y"));

        final ByteArrayOutputStream out = new ByteArrayOutputStream(32 * 1024);
        renderer.drawTile(out, x, y, z);

        msg.reply(Buffer.buffer(out.toByteArray()));

      } catch (NumberFormatException | NullPointerException | IOException e) {
        e.printStackTrace();
        msg.reply(null);
      }
    });
  }
}
