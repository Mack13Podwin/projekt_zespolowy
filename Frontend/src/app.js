(function(){
    'use strict';

    angular
        .module('app', ['ngRoute',
            'app.home',
            'app.user',
            'app.fridgeItems',
            'app.shoppingList',
            'app.suggestedRecipe']);
})();