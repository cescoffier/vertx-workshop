/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

/** @module vertx-microservice-workshop-js/data_storage_service */
!function (factory) {
  if (typeof require === 'function' && typeof module !== 'undefined') {
    factory();
  } else if (typeof define === 'function' && define.amd) {
    // AMD loader
    define('vertx-microservice-workshop-js/data_storage_service-proxy', [], factory);
  } else {
    // plain old include
    DataStorageService = factory();
  }
}(function () {

  /**
 Service exposed on the event bus to provide access to
 the stored Places.

 @class
  */
  var DataStorageService = function(eb, address) {

    var j_eb = eb;
    var j_address = address;
    var closed = false;
    var that = this;
    var convCharCollection = function(coll) {
      var ret = [];
      for (var i = 0;i < coll.length;i++) {
        ret.push(String.fromCharCode(coll[i]));
      }
      return ret;
    };

    /**
     Retrieves all places.

     @public
     @param resultHandler {function} the result handler 
     */
    this.getAllPlaces = function(resultHandler) {
      var __args = arguments;
      if (__args.length === 1 && typeof __args[0] === 'function') {
        if (closed) {
          throw new Error('Proxy is closed');
        }
        j_eb.send(j_address, {}, {"action":"getAllPlaces"}, function(err, result) { __args[0](err, result &&result.body); });
        return;
      } else throw new TypeError('function invoked with invalid arguments');
    };

    /**
     Retrieves all places belonging to the given category.

     @public
     @param category {string} the category 
     @param resultHandler {function} the result handler 
     */
    this.getPlacesForCategory = function(category, resultHandler) {
      var __args = arguments;
      if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
        if (closed) {
          throw new Error('Proxy is closed');
        }
        j_eb.send(j_address, {"category":__args[0]}, {"action":"getPlacesForCategory"}, function(err, result) { __args[1](err, result &&result.body); });
        return;
      } else throw new TypeError('function invoked with invalid arguments');
    };

    /**
     Retrieves all places belonging with the given tag.

     @public
     @param tag {string} the tag 
     @param resultHandler {function} the result handler 
     */
    this.getPlacesForTag = function(tag, resultHandler) {
      var __args = arguments;
      if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
        if (closed) {
          throw new Error('Proxy is closed');
        }
        j_eb.send(j_address, {"tag":__args[0]}, {"action":"getPlacesForTag"}, function(err, result) { __args[1](err, result &&result.body); });
        return;
      } else throw new TypeError('function invoked with invalid arguments');
    };

    /**
     Adds a place in the data storage service.

     @public
     @param place {Object} the place 
     @param resultHandler {function} handler called when the insertion is done. 
     */
    this.addPlace = function(place, resultHandler) {
      var __args = arguments;
      if (__args.length === 2 && typeof __args[0] === 'object' && typeof __args[1] === 'function') {
        if (closed) {
          throw new Error('Proxy is closed');
        }
        j_eb.send(j_address, {"place":__args[0]}, {"action":"addPlace"}, function(err, result) { __args[1](err, result &&result.body); });
        return;
      } else throw new TypeError('function invoked with invalid arguments');
    };

  };

  /**
   Method called to create a proxy (to consume the service).

   @memberof module:vertx-microservice-workshop-js/data_storage_service
   @param vertx {Vertx} vert.x 
   @param address {string} the address on the vent bus where the service is served. 
   @return {DataStorageService} the proxy on the {@link DataStorageService}
   */
  DataStorageService.createProxy = function(vertx, address) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] === 'string') {
      if (closed) {
        throw new Error('Proxy is closed');
      }
      j_eb.send(j_address, {"vertx":__args[0], "address":__args[1]}, {"action":"createProxy"});
      return;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  if (typeof exports !== 'undefined') {
    if (typeof module !== 'undefined' && module.exports) {
      exports = module.exports = DataStorageService;
    } else {
      exports.DataStorageService = DataStorageService;
    }
  } else {
    return DataStorageService;
  }
});