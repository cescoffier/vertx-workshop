/**
 * Copyright 2015 the original author or authors.
 */
package io.vertx.workshop.map.render;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.workshop.map.render.backend.Renderer;
import io.vertx.workshop.map.rules.RuleSet;
import io.vertx.workshop.map.source.MapSource;
import io.vertx.ext.web.Router;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Map Tile Render Micro-Service
 *
 * @author Paulo Lopes
 */
public class WebMapRenderVerticle extends AbstractVerticle {

  private static final Renderer renderer;

  static {
    try {

      final String rule = System.getProperty("rule", "rule.xml");
      final String index = System.getProperty("index", "antwerpen.osm.idx.gz");

      RuleSet ruleset = new RuleSet(WebMapRenderVerticle.class.getClassLoader().getResourceAsStream(rule));
      MapSource map = new MapSource(WebMapRenderVerticle.class.getClassLoader().getResourceAsStream(index));
      renderer = new Renderer(ruleset, map);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void start() {
    Router router = Router.router(vertx);
    router.route("/render/:x/:y/:z").handler(
        context -> {
          int x = Integer.parseInt(context.request().getParam("x"));
          int y = Integer.parseInt(context.request().getParam("y"));
          int z = Integer.parseInt(context.request().getParam("z"));
          System.out.println("Requested tile: x=" + x + ", y=" + y + ", z=" + z);

          final ByteArrayOutputStream out = new ByteArrayOutputStream(32 * 1024);
          try {
            renderer.drawTile(out, x, y, z);
          } catch (IOException e) {
            context.response().setStatusCode(400).end(e.getMessage());
          }
          context.response().end(Buffer.buffer(out.toByteArray()));
        }
    );

    vertx.createHttpServer().requestHandler(router::accept).listen(
        // Important for openshift:
        Integer.getInteger("http.port", 8001), System.getProperty("http.address", "localhost"));
  }
}
