(function(){
    'use strict';

    angular
        .module('app')
        .config(config);

    function config($routeProvider, $locationProvider){
        $locationProvider.hashPrefix("");
        $routeProvider
            .when('/home', {
                templateUrl: 'src/home/home.html',
                controller: 'HomeController',
                controllerAs: 'vm'
            })
            .when('/fridgeItems', {
                templateUrl: 'src/fridgeItems/fridgeItems.html',
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