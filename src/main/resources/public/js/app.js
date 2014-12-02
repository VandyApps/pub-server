'use strict';

var consoleApp = angular.module('ConsoleApp', []);

consoleApp.controller('ConsoleCtrl', function($scope, $http, $interval) {
  
  $scope.orderNumber = 1000;
  
  $scope.password = "";
  
  $scope.submitOrder = function() {
    if ($scope.orderNumber !== "") {
	  var url = window.location.href;
	  var apikey = url.substring(url.indexOf("?") + 1);
      apikey = CryptoJS.SHA1(apikey + $scope.orderNumber);
      $http.post("order?orderNumber=" + $scope.orderNumber + "&apikey=" + apikey)
          .success(function(data) { console.log(data) });
      $scope.orderNumber = "";
    }
  };
  
  $scope.orders = [];
  
  $scope.times = [];
  
  $scope.timeString = function(time) {
    return new Date(time).toLocaleTimeString();
  };
  
  $scope.currentTime = Date.now();
  
  var update = function() {
    $scope.currentTime = Date.now();
    
    $http.get("order?count=100")
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
