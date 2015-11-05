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
var utils = require('vertx-js/util/utils');
var Vertx = require('vertx-js/vertx');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JDataStorageService = io.vertx.workshop.data.DataStorageService;
var Place = io.vertx.workshop.data.Place;

/**
 Service exposed on the event bus to provide access to
 the stored Places.

 @class
*/
var DataStorageService = function(j_val) {

  var j_dataStorageService = j_val;
  var that = this;

  /**
   Retrieves all places.

   @public
   @param resultHandler {function} the result handler 
   */
  this.getAllPlaces = function(resultHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_dataStorageService["getAllPlaces(io.vertx.core.Handler)"](function(ar) {
      if (ar.succeeded()) {
        resultHandler(utils.convReturnListSetDataObject(ar.result()), null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
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
      j_dataStorageService["getPlacesForCategory(java.lang.String,io.vertx.core.Handler)"](category, function(ar) {
      if (ar.succeeded()) {
        resultHandler(utils.convReturnListSetDataObject(ar.result()), null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
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
      j_dataStorageService["getPlacesForTag(java.lang.String,io.vertx.core.Handler)"](tag, function(ar) {
      if (ar.succeeded()) {
        resultHandler(utils.convReturnListSetDataObject(ar.result()), null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
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
      j_dataStorageService["addPlace(io.vertx.workshop.data.Place,io.vertx.core.Handler)"](place != null ? new Place(new JsonObject(JSON.stringify(place))) : null, function(ar) {
      if (ar.succeeded()) {
        resultHandler(null, null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_dataStorageService;
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
    return utils.convReturnVertxGen(JDataStorageService["createProxy(io.vertx.core.Vertx,java.lang.String)"](vertx._jdel, address), DataStorageService);
  } else throw new TypeError('function invoked with invalid arguments');
};

// We export the Constructor function
module.exports = DataStorageService;