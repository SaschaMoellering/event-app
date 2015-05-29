'use strict';

angular.module('eventappApp')
    .factory('Event', function ($resource) {
        return $resource('api/events/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    if (data.date != null) data.date = new Date(data.date);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
