'use strict';

var consoleApp = angular.module('ConsoleApp', []);

consoleApp.controller('ConsoleCtrl', function($scope, $http, $interval) {
  
  $scope.orderNumber = 1000;
  
  $scope.submitOrder = function() {
    $scope.orders.push($scope.orderNumber);
    $scope.times.push(Date.now());
  };
  
  $scope.orders = [];
  
  $scope.times = [];
  
  $scope.timeString = function(time) {
    return new Date(time).toLocaleTimeString();
  };
  
  $scope.currentTime = Date.now();
  
  var update = function() {
    $scope.currentTime = Date.now();
    
    for (var t in $scope.times) {
      if (($scope.times[t] + (5 * 60 * 1000)) < $scope.currentTime) {
        $scope.times = $scope.times.slice(1);
        $scope.orders = $scope.orders.slice(1);
      }
    }
  }
  
  $interval(update, 1000);
  
});
