'use strict';

angular.module('eventappApp')
    .factory('Participant', function ($resource) {
        return $resource('api/participants/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    if (data.birthday != null){
                        var birthdayFrom = data.birthday.split("-");
                        data.birthday = new Date(new Date(birthdayFrom[0], birthdayFrom[1] - 1, birthdayFrom[2]));
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var birthday = new Date();
                    birthday.setUTCDate(data.birthday.getDate());
                    birthday.setUTCMonth(data.birthday.getMonth());
                    birthday.setUTCFullYear(data.birthday.getFullYear());
                    data.birthday = birthday;
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var birthday = new Date();
                    birthday.setUTCDate(data.birthday.getDate());
                    birthday.setUTCMonth(data.birthday.getMonth());
                    birthday.setUTCFullYear(data.birthday.getFullYear());
                    data.birthday = birthday;
                    return angular.toJson(data);
                }
            }
        });
    });
