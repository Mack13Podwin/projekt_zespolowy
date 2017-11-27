(function(){
    'use strict';

    angular
        .module('app', ['ngRoute',
            'app.home','app.fridgeItems','app.shoppingList', 'app.suggestedRecipe']);
})();