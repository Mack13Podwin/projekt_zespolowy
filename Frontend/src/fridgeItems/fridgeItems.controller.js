(function(){
    'use strict';

    angular
        .module('app.fridgeItems')
        .controller('FridgeItemsController', FridgeItemsController);

    FridgeItemsController.$inject=['$scope', 'fridgeItemsService'];

    function FridgeItemsController($scope, fridgeItemsService){
        var vm=this;
        vm.fridgeItems=[];
        init();
        ////////////////////////
        function init(){
            fridgeItemsService.getProductsInFridge()
                .then(function(response){
                    console.log(response.data);
                    vm.fridgeItems=response.data;
                }).catch(function(err){
                    console.log(err);
                });
        }
    }
})();