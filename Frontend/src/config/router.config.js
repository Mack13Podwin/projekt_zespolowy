(function(){
    'use strict';

    angular
        .module('app')
        .config(config);

    function config($routeProvider, $locationProvider){
        $locationProvider.hashPrefix("");
        $routeProvider
            .when('/login', {
                templateUrl: 'src/login/login.html',
                controller: 'LoginController',
                controllerAs: 'vm'
            })
            .when('/home', {
                templateUrl: 'src/home/home.html'
            })
            .when('/fridgeItems', {
                templateUrl: 'src/fridgeItems/fridgeItems.html',
                controller: 'FridgeItemsController',
                controllerAs: 'vm'
            })
            .when('/shoppingList', {
                templateUrl: 'src/shoppingList/shoppingList.html',
            })
            .when('/suggestedRecipe', {
                templateUrl: 'src/suggestedRecipe/suggestedRecipe.html',
            })
            .otherwise({
                redirectTo: '/home'
            });
    }
})();