'use strict';

/* Controllers */

app.controller('POIListController', function ($scope, $http) {

  // TODO Configuration:
  // Change here if you are using the docker-machine: http:////192.168.99.100:8000
  // Change here if you are using localhost: http://localhost:8000
  var proxyURL = "http:////192.168.99.100:8000";

  // TODO Change recommendation service address to your own
  var recommendationServiceAddress = "devoxx.recommendations";
  var recommendationBroadcastAddress = "devoxx.recommendations.announce";

  // place map in antwerpen center
  var map = L.map('map').setView([51.21796, 4.42079], 13);
  var marker;


  L.tileLayer(proxyURL + '/{z}/{x}/{y}.png', {
    maxZoom: 18,
    attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>'
  }).addTo(map);

  // bridge directly to the recommendation service
  var eb = new EventBus(window.location.protocol + '//' + window.location.host + '/eventbus');
  var recommendation = new RecommendationService(eb, recommendationServiceAddress);

  // initialize the POIs
  $scope.pois = [];

  // connect to the event bus
  eb.onopen = function () {
    // request the resource from the server
    $http.get('http://localhost:8080/places').success(function (data) {
      // load recommendations
      data.forEach(function (el) {
        $scope.pois.push(el);

        recommendation.get(el.name, function (err, res) {
          if (!err) {
            $scope.$apply(function () {
              el.thumbsUp = res.up || el.thumbsUp;
              el.thumbsDown = res.down || el.thumbsDown;
              el.thumbs = (el.thumbsUp || 0) - (el.thumbsDown || 0);
            });
          }
        });
      });

      eb.registerHandler(recommendationBroadcastAddress,function (err, msg) {
        if (!err) {
          $scope.$apply(function () {
            $scope.pois.filter(function (val) {
              return val.name === msg.body.name;
            }).forEach(function (el) {
              el.thumbsUp = msg.body.up || el.thumbsUp;
              el.thumbsDown = msg.body.down || el.thumbsDown;
              el.thumbs = (el.thumbsUp || 0) - (el.thumbsDown || 0);
            });
          });
        }
      });
    });
  };

  // declare helpers
  $scope.showInMap = function (poi) {
    if (marker) {
      marker.setLatLng([poi.latitude, poi.longitude]);
    } else {
      marker = L.marker([poi.latitude, poi.longitude]).addTo(map);
    }

    marker.bindPopup("<b>" + poi.name + "</b><br />" + poi.address).openPopup();
    map.setView([poi.latitude, poi.longitude], 16);
  };

  $scope.thumbs = function (poi, up) {
    recommendation.vote(poi.name, up, function (err) {
      if (err) {
        console.error(err);
      }
    });
  };
});