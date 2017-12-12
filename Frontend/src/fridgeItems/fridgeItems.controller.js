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
                    vm.fridgeItems=response.data;
                    vm.fridgeItems.forEach(function(value, index, array){
                        value.addingdate=new Date(value.addingdate);
                        if(value.expirationdate){
                            value.expirationdate=new Date(value.expirationdate);
                        }
                        if(value.openingdate){
                            value.openingdate=new Date(value.openingdate)
                        }
                    })
                    console.log(vm.fridgeItems);
                }).catch(function(err){
                    console.log(err);
                });
        }
    }
})();