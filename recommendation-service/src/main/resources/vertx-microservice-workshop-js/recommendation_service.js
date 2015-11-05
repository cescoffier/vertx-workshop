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

/** @module vertx-microservice-workshop-js/recommendation_service */
var utils = require('vertx-js/util/utils');
var Vertx = require('vertx-js/vertx');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JRecommendationService = io.vertx.workshop.recommendation.RecommendationService;

/**
 A service to recommend places.

 @class
*/
var RecommendationService = function(j_val) {

  var j_recommendationService = j_val;
  var that = this;

  /**
   Votes for a place.
   When a vote is placed, the new rating of the place are brodcasted to the event bus.

   @public
   @param name {string} the name of the place 
   @param plus {boolean} whether or not you liked the place. 
   @param resultHandler {function} invoked when the action is completed 
   */
  this.vote = function(name, plus, resultHandler) {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] ==='boolean' && typeof __args[2] === 'function') {
      j_recommendationService["vote(java.lang.String,boolean,io.vertx.core.Handler)"](name, plus, function(ar) {
      if (ar.succeeded()) {
        resultHandler(null, null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Gets the number or votes.

   @public
   @param name {string} the name of the place 
   @param resultHandler {function} the result handler. The votes are given in a JSON object using the "up" and "down" fields. 
   */
  this.get = function(name, resultHandler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_recommendationService["get(java.lang.String,io.vertx.core.Handler)"](name, function(ar) {
      if (ar.succeeded()) {
        resultHandler(utils.convReturnJson(ar.result()), null);
      } else {
        resultHandler(null, ar.cause());
      }
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_recommendationService;
};

/**
 The method used to create proxy to consume the service.

 @memberof module:vertx-microservice-workshop-js/recommendation_service
 @param vertx {Vertx} vert.x 
 @param address {string} the address where the service is served 
 @return {RecommendationService} the proxy
 */
RecommendationService.createProxy = function(vertx, address) {
  var __args = arguments;
  if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] === 'string') {
    return utils.convReturnVertxGen(JRecommendationService["createProxy(io.vertx.core.Vertx,java.lang.String)"](vertx._jdel, address), RecommendationService);
  } else throw new TypeError('function invoked with invalid arguments');
};

// We export the Constructor function
module.exports = RecommendationService;