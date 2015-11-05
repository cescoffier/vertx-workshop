/**
 * Copyright 2015 the original author or authors.
 */
package io.vertx.workshop.map.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Map Tile Server Micro-Service
 *
 * @author Paulo Lopes
 */
public class MapServerVerticle extends AbstractVerticle {

  // Regular Expression to match requests
  private static final Pattern TILE = Pattern.compile("^/(\\d+)/(\\d+)/(\\d+)\\.png$");

  private final SimpleDateFormat ISODATE;

  public MapServerVerticle() {
    ISODATE = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
    ISODATE.setTimeZone(TimeZone.getTimeZone("UTC"));
  }

  @Override
  public void start() {

    final int port = config().getInteger("port", 8000);
    final int cache = config().getInteger("cache", 86400);

    vertx.createHttpServer().requestHandler(req -> {
      if (req.method() == HttpMethod.GET) {
        final Matcher matcher = TILE.matcher(req.path());

        if (matcher.matches()) {
          String z = matcher.group(1);
          String x = matcher.group(2);
          String y = matcher.group(3);

          final String etag = "\"" + z + "-" + x + "-" + y + "\"";

          req.response()
              .putHeader("etag", etag)
              .putHeader("cache-control", "public, max-age=" + cache);

          if (isFresh(etag, req)) {

            // some caching code here
            req.response()
                .setStatusCode(304)
                .end();

          } else {
            //Replace next line by "vertx.createHttpClient().getNow(8001, "localhost"," to use the local version:
            vertx.createHttpClient().getNow(80, "map0render0service-vertxdemos.rhcloud.com",
                "/render/" + x + "/" + y + "/" + z, response -> {
                  if (response.statusCode() == 200) {
                    response.bodyHandler(buffer -> {
                          req.response()
                              .putHeader("last-modified", ISODATE.format(new Date()))
                              .putHeader("content-type", "image/png")
                              .end(buffer);
                        }
                    );
                  } else {
                    req.response().setStatusCode(500).end("Cannot retrieve map tile");
                  }
                });
          }
          return;
        }
      }
      // if it reached this step it is a bad request
      req.response().setStatusCode(400).end();

    }).listen(port);
  }

  private boolean isFresh(final String etag, final HttpServerRequest request) {
    // defaults
    boolean etagMatches = true;
    boolean notModified = true;

    // fields
    String modifiedSince = request.getHeader("if-modified-since");
    String noneMatch = request.getHeader("if-none-match");
    String[] noneMatchTokens = null;

    // unconditional request
    if (modifiedSince == null && noneMatch == null) {
      return false;
    }

    // parse if-none-match
    if (noneMatch != null) {
      noneMatchTokens = noneMatch.split(" *, *");
    }

    // if-none-match
    if (noneMatchTokens != null) {
      etagMatches = false;
      for (String s : noneMatchTokens) {
        if (etag.equals(s) || "*".equals(noneMatchTokens[0])) {
          etagMatches = true;
          break;
        }
      }
    }

    // if-modified-since
    if (modifiedSince != null) {
      try {
        Date modifiedSinceDate = ISODATE.parse(modifiedSince);
        notModified = System.currentTimeMillis() <= modifiedSinceDate.getTime();
      } catch (ParseException e) {
        notModified = false;
      }
    }

    return etagMatches && notModified;
  }
}
