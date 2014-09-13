'use strict';

var consoleApp = angular.module('ConsoleApp', []);

consoleApp.controller('ConsoleCtrl', function($scope, $http, $interval) {
  
  $scope.orderNumber = 1000;
  
  $scope.submitOrder = function() {
    $http.post("order?orderNumber=" + $scope.orderNumber + "&apikey=bogus");
  };
  
  $scope.orders = [];
  
  $scope.times = [];
  
  $scope.timeString = function(time) {
    return new Date(time).toLocaleTimeString();
  };
  
  $scope.currentTime = Date.now();
  
  var update = function() {
    $scope.currentTime = Date.now();
    
    $http.get("order?count=100&apikey=bogus")
        .success(function(data) {
            $scope.orders = [];
            $scope.times = [];
            for (var i in data.orders) {
                $scope.orders.push(data.orders[i].orderNumber);
                $scope.times.push(data.orders[i].timeCreated)
            }});
    
  }
  
  $interval(update, 1000);
  
});
