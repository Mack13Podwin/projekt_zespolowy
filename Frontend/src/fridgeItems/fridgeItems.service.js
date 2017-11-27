(function(){
    'use strict';

    angular
        .module('app.fridgeItems')
        .service('fridgeItemsService', fridgeItemsService);

    fridgeItemsService.$inject=['$http', 'userService'];

    function fridgeItemsService($http, userService){
        var service={
            getProductsInFridge: getProductsInFridge
        }
        return service;

        function getProductsInFridge(){
            return $http.get('/backend/ui/inside/'+userService.getCurrentFridgeId());
        }
    }

})();