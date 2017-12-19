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
            .when('/remindPassword', {
                templateUrl: 'src/remindPassword/remindPassword.html',
                controller: 'RemindPasswordController',
                controllerAs: 'vm'
            })
            /*.when('/setEmail', {
                templateUrl: 'src/setEmail/setEmail.html',
                controller: 'SetEmailController',
                controllerAs: 'vm'
            })*/
            .when('/changePassword', {
                templateUrl: 'src/changePassword/changePassword.html',
                controller: 'ChangePasswordController',
                controllerAs: 'vm'
            })
            .when('/changeLogin', {
                templateUrl: 'src/changeLogin/changeLogin.html',
                controller: 'ChangeLoginController',
                controllerAs: 'vm'
            })
            .when('/home', {
                templateUrl: 'src/home/home.html',
                controller: 'HomeController',
                controllerAs: 'vm'
            })
            .when('/fridgeItems', {
                templateUrl: 'src/fridgeItems/fridgeItems.html',
                controller: 'FridgeItemsController',
                controllerAs: 'vm'
            })
            .when('/shoppingList', {
                templateUrl: 'src/shoppingList/shoppingList.html',
                controller: 'ShoppingListController',
                controllerAs: 'vm'
            })
            .when('/suggestedRecipe', {
                templateUrl: 'src/suggestedRecipe/suggestedRecipe.html',
                controller: 'SuggestedRecipeController',
                controllerAs: 'vm'
            })
            .otherwise({
                redirectTo: '/home'
            });
    }
})();