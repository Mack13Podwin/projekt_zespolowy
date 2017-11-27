(function(){
    'use strict';

    angular
        .module('app.user')
        .service('userService', userService);

    userService.$inject=[];

    function userService(){
        var currentFridgeId;
        var service={
            setCurrentFridgeId: setCurrentFridgeId,
            getCurrentFridgeId: getCurrentFridgeId,
            loggedIn: loggedIn
        }
        return service;

        function setCurrentFridgeId(id){
            currentFridgeId=id;
            console.log(!!currentFridgeId);
        }

        function getCurrentFridgeId(){
            return currentFridgeId;
        }

        function loggedIn(){
            return !!currentFridgeId;
        }
    }

})();