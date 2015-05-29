'use strict';

angular.module('eventappApp')
    .controller('ParticipantController', function ($scope, Participant, Event, ParseLinks) {
        $scope.participants = [];
        $scope.events = Event.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Participant.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.participants.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 1;
            $scope.participants = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Participant.get({id: id}, function(result) {
                $scope.participant = result;
                $('#saveParticipantModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.participant.id != null) {
                Participant.update($scope.participant,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Participant.save($scope.participant,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Participant.get({id: id}, function(result) {
                $scope.participant = result;
                $('#deleteParticipantConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Participant.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteParticipantConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $('#saveParticipantModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.participant = {firstname: null, lastname: null, birthday: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
