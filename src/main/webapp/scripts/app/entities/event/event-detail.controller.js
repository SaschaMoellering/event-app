'use strict';

angular.module('eventappApp')
    .controller('EventDetailController', function ($scope, $stateParams, Event, Participant) {
        $scope.event = {};
        $scope.load = function (id) {
            Event.get({id: id}, function(result) {
              $scope.event = result;
            });
        };
        $scope.load($stateParams.id);
    });
