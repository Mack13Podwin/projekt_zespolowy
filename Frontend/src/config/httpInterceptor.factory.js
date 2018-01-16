(function(){
    'use strict';

    angular
        .module('app')
        .factory('httpInterceptor', httpInterceptor);

    httpInterceptor.$inject=['$injector'];

    function httpInterceptor($injector){
        return {
            'request': function(config){
                var loginService=$injector.get('loginService');
                if(loginService.loggedIn() && loginService.getUserToken()){
                    config.headers.Authorization=loginService.getUserToken();
                }
                return config;
            }
        }
    }

})();