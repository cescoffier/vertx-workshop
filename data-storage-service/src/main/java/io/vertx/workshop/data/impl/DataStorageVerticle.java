package io.vertx.workshop.data.impl;

import io.vertx.core.AbstractVerticle;
import io.vertx.workshop.data.DataStorageService;
import io.vertx.serviceproxy.ProxyHelper;

/**
 * The verticle instantiating the service.
 */
public class DataStorageVerticle extends AbstractVerticle {

  private DataStorageServiceImpl service;

  @Override
  public void start() throws Exception {
    service = new DataStorageServiceImpl(vertx, config());
    //TODO Change address to something more specific
    ProxyHelper.registerService(DataStorageService.class, vertx, service, "devoxx.places");
  }

  @Override
  public void stop() throws Exception {
    service.close();
  }
}
