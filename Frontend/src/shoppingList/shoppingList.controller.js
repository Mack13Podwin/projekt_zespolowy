(function(){
    'use strict';

    angular
        .module('app.shoppingList')
        .controller('ShoppingListController', ShoppingListController);

    ShoppingListController.$inject=['$scope','$http','$location','loginService'];

    function ShoppingListController($scope,$http,$location,loginService){
        var vm=this;
        vm.items=[]
        init();
        
        //////////////////////////////
        function init(){
            
            console.log("getting shoppin list")
            $http.get('/backend/ui/shoppinglist').then(function(response){
                console.log(response.data)
                vm.items=response.data
            }).catch(function(err){
                console.log(err);
            })
        }
    }
})();