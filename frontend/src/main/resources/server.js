var Router = require('vertx-web-js/router');
var SockJSHandler = require('vertx-web-js/sock_js_handler');
var StaticHandler = require('vertx-web-js/static_handler');
var DataStorageService = require('devoxx-workshop-js/data_storage_service');

// create a http router
var router = Router.router(vertx);

// create services
var store = DataStorageService.createProxy(vertx, 'devoxx.places');

// Allow events for the designated addresses in/out of the event bus bridge
var opts = {
  inboundPermitteds:  [{address: 'devoxx.recommendations'}],
  outboundPermitteds: [{address: 'devoxx.recommendations.announce'}]
};

// Create the event bus bridge and add it to the router.
router.route('/eventbus/*').handler(SockJSHandler.create(vertx).bridge(opts).handle);

// handle request to places
router.get('/places').handler(function (ctx) {
  store.getAllPlaces(function (res, err) {
    if (err) {
      ctx.fail(err);
    } else {
      ctx.response().putHeader('Content-Type', 'application/json').end(JSON.stringify(res));
    }
  })
});

// Serve the static resources
router.route().handler(StaticHandler.create().handle);

vertx.createHttpServer().requestHandler(router.accept).listen(8080);