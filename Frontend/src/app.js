(function(){
    'use strict';

    angular
        .module('app', ['ngRoute',
            'ngStorage',
            'app.login',
            'app.remindPassword',
            'app.changePassword',
            'app.changeLogin',
            'app.fridgeItems',
            'app.shoppingList',
            'app.suggestedRecipe']);
})();