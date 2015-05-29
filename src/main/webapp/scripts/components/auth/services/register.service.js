'use strict';

angular.module('eventappApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


