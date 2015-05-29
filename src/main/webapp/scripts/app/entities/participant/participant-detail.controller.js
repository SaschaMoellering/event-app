'use strict';

angular.module('eventappApp')
    .controller('ParticipantDetailController', function ($scope, $stateParams, Participant, Event) {
        $scope.participant = {};
        $scope.load = function (id) {
            Participant.get({id: id}, function(result) {
              $scope.participant = result;
            });
        };
        $scope.load($stateParams.id);
    });
