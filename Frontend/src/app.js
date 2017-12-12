(function(){
    'use strict';

    angular
        .module('app', ['ngRoute',
            'ngStorage',
            'app.login',
            'app.fridgeItems',
            'app.shoppingList',
            'app.suggestedRecipe']);
})();