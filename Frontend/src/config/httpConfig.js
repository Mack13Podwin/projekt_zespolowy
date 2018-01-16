(function(){
    'use strict';

    angular
        .module('app')
        .config(httpConfig);

    httpConfig.$inject=['$httpProvider'];

    function httpConfig($httpProvider){
        $httpProvider.interceptors.push('httpInterceptor');
    }
    
})();