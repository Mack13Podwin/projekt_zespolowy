(function(){
    'use strict';

    angular
        .module('app.fridgeItems')
        .service('fridgeItemsService', fridgeItemsService);

    fridgeItemsService.$inject=['$http', 'loginService'];

    function fridgeItemsService($http, loginService){
        var service={
            getProductsInFridge: getProductsInFridge
        }
        return service;

        function getProductsInFridge(){
            return $http.get('/backend/ui/inside/'+loginService.getUserFridgeId());
        }
    }

})();