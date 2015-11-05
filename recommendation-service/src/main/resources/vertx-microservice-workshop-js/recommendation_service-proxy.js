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
!function (factory) {
  if (typeof require === 'function' && typeof module !== 'undefined') {
    factory();
  } else if (typeof define === 'function' && define.amd) {
    // AMD loader
    define('vertx-microservice-workshop-js/recommendation_service-proxy', [], factory);
  } else {
    // plain old include
    RecommendationService = factory();
  }
}(function () {

  /**
 A service to recommend places.

 @class
  */
  var RecommendationService = function(eb, address) {

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
        if (closed) {
          throw new Error('Proxy is closed');
        }
        j_eb.send(j_address, {"name":__args[0], "plus":__args[1]}, {"action":"vote"}, function(err, result) { __args[2](err, result &&result.body); });
        return;
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
        if (closed) {
          throw new Error('Proxy is closed');
        }
        j_eb.send(j_address, {"name":__args[0]}, {"action":"get"}, function(err, result) { __args[1](err, result &&result.body); });
        return;
      } else throw new TypeError('function invoked with invalid arguments');
    };

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
      if (closed) {
        throw new Error('Proxy is closed');
      }
      j_eb.send(j_address, {"vertx":__args[0], "address":__args[1]}, {"action":"createProxy"});
      return;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  if (typeof exports !== 'undefined') {
    if (typeof module !== 'undefined' && module.exports) {
      exports = module.exports = RecommendationService;
    } else {
      exports.RecommendationService = RecommendationService;
    }
  } else {
    return RecommendationService;
  }
});