'use strict';

angular.module('eventappApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
