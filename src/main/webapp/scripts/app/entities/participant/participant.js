'use strict';

angular.module('eventappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('participant', {
                parent: 'entity',
                url: '/participant',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'eventappApp.participant.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/participant/participants.html',
                        controller: 'ParticipantController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('participant');
                        return $translate.refresh();
                    }]
                }
            })
            .state('participantDetail', {
                parent: 'entity',
                url: '/participant/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'eventappApp.participant.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/participant/participant-detail.html',
                        controller: 'ParticipantDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('participant');
                        return $translate.refresh();
                    }]
                }
            });
    });
