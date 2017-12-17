(function(){
    'use strict';

    angular
        .module('app.shoppingList')
        .controller('ShoppingListController', ShoppingListController);

    ShoppingListController.$inject=['$scope','$http','$location','loginService'];

    function ShoppingListController($scope,$http,$location,loginService){
        var vm=this;

        init();
        
        //////////////////////////////
        function init(){
            
            console.log("getting shoppin list")
            $http.get('/backend/ui/shoppinglist/'+loginService.getUserFridgeId()).then(function(response){
                console.log(response.data)
            }).catch(function(err){
                console.log(err);
            })
        }
    }
})();